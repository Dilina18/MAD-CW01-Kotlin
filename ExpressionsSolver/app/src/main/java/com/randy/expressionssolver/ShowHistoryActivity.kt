package com.randy.expressionssolver

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ShowHistoryActivity : AppCompatActivity() {

    private var historyRecyclerView: RecyclerView? = null
    private var gamesAdapter:GameRecyclerViewAdapter? = null
    private var database = Database
    private var isDataLoaded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_history)

        initViews() // call method to init views

        // load saved  bundle data
        if (null != savedInstanceState){
            val loaded = savedInstanceState.getBoolean("isLoaded")
            isDataLoaded = loaded
        }

        handleOrientation() // calling method to setup adapter

    }

    //initialize android views
    private fun initViews(){
        historyRecyclerView = findViewById(R.id.historyList)
    }

    // method to set game adapter settings
    // to watch past games
    private fun setAdapterView(orientation:String){
        var layoutManager:RecyclerView.LayoutManager? = null
        gamesAdapter = GameRecyclerViewAdapter(this,"showingHistory")

        historyRecyclerView?.adapter = gamesAdapter
        if (orientation == "portrait"){
            layoutManager = LinearLayoutManager(this)
        }
        else if (orientation == "landscape"){
            layoutManager = GridLayoutManager(this, 3)
        }
        historyRecyclerView?.layoutManager = layoutManager

        // call function load load shared preferences
        // and check if data already loaded
        if (!isDataLoaded){
            loadPreferences()
            isDataLoaded = true
        }

        database.getPastGames()?.let { gamesAdapter!!.setGames(it) }
    }

    // method to set adapter settings
    // based on orientation
    private fun handleOrientation(){
        val orientation = this.resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            setAdapterView("portrait")
        }
        else{
            setAdapterView("landscape")
        }
    }

    // this function load shared preference data
    // to successfully load and show game history data
    private fun loadPreferences(){
        database.getPastGames()?.clear()
        val pref = getSharedPreferences("HISTORY_DATA",Context.MODE_PRIVATE)
        val totalGames = pref.getInt("totalGames", 0)

        for (i in 0 until totalGames) {
            val totalCorrect = pref.getInt("$i totalCorrect", 0)
            val totalWrong = pref.getInt("$i totalWrong", 0)
            val totalTime = pref.getInt("$i totalTime", 0)
            val game = GameResult(totalCorrect, totalWrong, totalTime)
            database.getPastGames()?.add(game)
        }
    }

    // override onSaveInstanceState method
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean("isLoaded", isDataLoaded)
        super.onSaveInstanceState(outState)
    }
}