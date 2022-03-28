package com.randy.expressionssolver

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import java.util.*

class GameActivity : AppCompatActivity() {

    // declare android views
    private lateinit var txtExpLeft: TextView
    private lateinit var txtExpRight: TextView
    private lateinit var txtMsgLabel: TextView
    private lateinit var txtAnswer: TextView
    private lateinit var txtTimer: TextView
    private lateinit var txtCorrect: TextView
    private lateinit var txtIncorrect: TextView
    private lateinit var btnGreater: Button
    private lateinit var btnEqual: Button
    private lateinit var btnLess: Button
    private lateinit var resultLayout: RelativeLayout
    private lateinit var timeLeftFormatted:String
    private var alertDialog:AlertDialog? = null
    private var timer: CountDownTimer? = null

    // initialize class variables
    private var isAlert:Boolean = false
    private var correct: Int = 0
    private var wrong: Int = 0
    private var valLeft: Double = 0.0
    private var valRight: Double = 0.0
    private var forExtra:Int = 0
    private val startingTime: Long = 50000
    private var timeLeftMills = startingTime
    private var endTime: Long = 0
    private var isGameOver:Boolean = false
    private var totalTime:Int = 50000/1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        // calling method to initialize android widgets
        initViews()

        loadData(savedInstanceState) // calling method to keep data consistency

        // set expressions values & views
        setGameView()

        // check if game over
        if (!isGameOver){
            startTimer() //calling method to start timer
        }

        // handle grater than btn operation
        btnGreater.setOnClickListener {
            onClickGreater(valLeft, valRight)
        }

        // handle Equal btn operation
        btnEqual.setOnClickListener {
            onClickEqual(valLeft, valRight)
        }

        // handle Less than btn operation
        btnLess.setOnClickListener {
            onClickLess(valLeft, valRight)
        }

    }

    // method to initialize all widgets
    private fun initViews(){
        txtExpLeft = findViewById(R.id.txtExpLeft)
        txtExpRight = findViewById(R.id.txtExpRight)
        txtMsgLabel = findViewById(R.id.txtMsgLabel)
        txtAnswer = findViewById(R.id.txtAnswer)
        txtTimer = findViewById(R.id.txtTimer)
        txtCorrect = findViewById(R.id.txtCorrect)
        txtIncorrect = findViewById(R.id.txtIncorrect)
        btnGreater = findViewById(R.id.btnGreater)
        btnEqual = findViewById(R.id.btnEqual)
        btnLess = findViewById(R.id.btnLess)
        resultLayout = findViewById(R.id.resultLayout)
    }

    // method to set values for game activity widgets
    // uses Operations class to handle arithmetic operations
    private fun setGameView(){
        val operations = Operations()
        val expLeft: String = operations.uniqueWholeNumberExpGenerator()
        valLeft = operations.getValExp()
        val expRight: String = operations.uniqueWholeNumberExpGenerator()
        valRight = operations.getValExp()

        txtExpLeft.text = expLeft
        txtExpRight.text = expRight


    }

    // method to handle grater btn functionality
    private fun onClickGreater(toCompare: Double, compareWith: Double){
        if (toCompare > compareWith){
            systemFeedback(true)
        }
        else{
            systemFeedback(false)
        }
    }

    // method to handle Equal btn functionality
    private fun onClickEqual(toCompare: Double, compareWith: Double){
        if (toCompare == compareWith){
            systemFeedback(true)
        }
        else{
            systemFeedback(false)
        }
    }

    // method to handle Less btn functionality
    private fun onClickLess(toCompare: Double, compareWith: Double){
        if (toCompare < compareWith){
            systemFeedback(true)
        }
        else{
            systemFeedback(false)
        }
    }

    // method to update labels on submission
    private fun systemFeedback(isCorrect: Boolean){
        if (!txtMsgLabel.isShown){
            txtMsgLabel.visibility = View.VISIBLE
        }
        if (isCorrect){
            txtAnswer.text = getString(R.string.labelCorrect)
            txtAnswer.setTextColor(Color.GREEN)
            correct += 1
            forExtra += 1
            setGameView()
        }
        else {
            txtAnswer.text = getString(R.string.labelWrong)
            txtAnswer.setTextColor(Color.RED)
            wrong += 1
            setGameView()
        }
    }

    // method to update data on game over
    private fun setGameOver(){
        txtTimer.setBackgroundResource(R.color.accent2)
        txtTimer.text = getString(R.string.labelGO)
        txtCorrect.text = correct.toString()
        txtIncorrect.text = wrong.toString()
        resultLayout.visibility = (View.VISIBLE)
        btnGreater.isEnabled = false
        btnEqual.isEnabled = false
        btnLess.isEnabled = false
    }

    // override onBackPressed method
    override fun onBackPressed() {
        if (!isGameOver){
            showGameAlert()
            isAlert = true
        }
        else {
            super.onBackPressed()
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }

    // override onSaveInstanceState method
    override fun onSaveInstanceState(outState: Bundle) {

        outState.putBoolean("isAlert", isAlert)

        // state timer data
        outState.putLong("millisLeft", timeLeftMills)
        outState.putLong("endTime", endTime)
        outState.putBoolean("endTimer", isGameOver)
        outState.putString("txtTimer", txtTimer.text.toString())
        outState.putInt("totalTime", totalTime)

        // state expression data
        outState.putString("expLeft", txtExpLeft.text.toString())
        outState.putDouble("valLeft", valLeft)
        outState.putString("expRight", txtExpRight.text.toString())
        outState.putString("format", timeLeftFormatted)
        outState.putDouble("valRight", valRight)

        // state feedback data
        outState.putString("answer", txtAnswer.text.toString())

        //state result data
        outState.putString("txtCorrect", txtCorrect.text.toString())
        outState.putInt("correct", correct)
        outState.putString("txtIncorrect", txtIncorrect.text.toString())
        outState.putInt("wrong", wrong)
        outState.putInt("forExtra", forExtra)

        super.onSaveInstanceState(outState)
    }

    // override onRestoreInstanceState method
    // to keep data consistency after configuration changes
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        loadData(savedInstanceState) // calling method to keep data consistency
        if (isGameOver){
            setGameOver()
        }
        else{
            timer?.cancel()
            totalTime = savedInstanceState.getInt("totalTime")
            isGameOver = savedInstanceState.getBoolean("endTimer")
            timeLeftMills = savedInstanceState.getLong("millisLeft")
            endTime = savedInstanceState.getLong("endTime")
            timeLeftMills = endTime - System.currentTimeMillis()
            startTimer()
            updateCountDownText() // update timer text after rotation
        }
        updateCountDownText()
        handleOrientation()
        super.onRestoreInstanceState(savedInstanceState)
    }


    // method to load current instance of data
    // avoid loss data consistency
    private fun loadData(savedInstanceState: Bundle?){
        if (null != savedInstanceState){
            // get stated data
            val expLeft: String? = savedInstanceState.getString("expLeft")
            val valLeft: Double = savedInstanceState.getDouble("valLeft")
            val expRight: String? = savedInstanceState.getString("expRight")
            val valRight: Double = savedInstanceState.getDouble("valRight")
            val answer: String? = savedInstanceState.getString("answer")
            val txtCorrect:String? = savedInstanceState.getString("txtCorrect")
            val txtIncorrect:String? = savedInstanceState.getString("txtIncorrect")
            val correct:Int = savedInstanceState.getInt("correct")
            val wrong:Int = savedInstanceState.getInt("wrong")
            val forExtra:Int = savedInstanceState.getInt("forExtra")
            val formatted:String? = savedInstanceState.getString("format")
            val txtTimer:String? = savedInstanceState.getString("txtTimer")
            val isGameOver:Boolean = savedInstanceState.getBoolean("endTimer")
            val isAlert:Boolean = savedInstanceState.getBoolean("isAlert")

            this.isAlert = isAlert

            // left exp label and values
            if (null != expLeft){
                this.txtExpLeft.text = expLeft
                this.valLeft = valLeft
            }
            // right exp label and values
            if (null != expRight){
                this.txtExpRight.text = expRight
                this.valRight = valRight
            }
            // answer label and text
            if (null != answer){
                this.txtAnswer.text = answer
                updateLabelColor(answer) // updating text color
            }
            // correct & incorrect text
            if (null != txtCorrect && null != txtIncorrect){
                this.txtCorrect.text = txtCorrect
                this.txtIncorrect.text = txtIncorrect
            }
            // correct & incorrect values
            this.correct = correct
            this.wrong = wrong
            this.forExtra = forExtra
            if (formatted != null) {
                this.timeLeftFormatted = formatted
            }
            this.txtTimer.text = txtTimer
            this.isGameOver = isGameOver
        }
    }

    // method to update countdown timer text
    private fun updateCountDownText() {
        if (!isGameOver){
            val minutes = (timeLeftMills / 1000).toInt() / 60
            val seconds = (timeLeftMills / 1000).toInt() % 60
            // format timer text
            timeLeftFormatted =
                java.lang.String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
            txtTimer.text = timeLeftFormatted
        }

    }

    // method to update label colors
    private fun updateLabelColor(answer:String){
        if (!txtMsgLabel.isShown){
            if (answer != ""){
                txtMsgLabel.visibility = View.VISIBLE
            }
        }
        if (answer == "Correct!"){
            txtAnswer.setTextColor(Color.GREEN)
        }
        else {
            txtAnswer.setTextColor(Color.RED)
        }
    }

    // method to handle & update extra time
    // for every 5 correct answers
    private fun startTimer() {
        endTime = System.currentTimeMillis() + timeLeftMills
        timer = object : CountDownTimer(timeLeftMills, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftMills = millisUntilFinished
                updateCountDownText()

                // check for 5 correct answers
                // to add extra time
                if (forExtra != 0 && correct % 5 == 0){
                    timer?.cancel()
                    timeLeftMills += 10000
                    totalTime += 10
                    startTimer()
                    updateCountDownText()
                    forExtra = 0
                }

            }
            override fun onFinish() {
                setGameOver()
                isGameOver = true
                alertDialog?.dismiss()
                isAlert = false
                saveOnFinish() // calling function to save game data
            }
        }
        timer?.start()
    }

    // override onDestroy method
    // cancel background countdown timer after orientation
    override fun onDestroy() {
        timer?.cancel()
        super.onDestroy()
    }

    // function to save data using shared preferences
    // after end of each game
    private fun saveOnFinish(){
        val preference = getSharedPreferences("HISTORY_DATA", Context.MODE_PRIVATE)
        val editor = preference.edit()

        val totalGames = preference.getInt("totalGames", 0)

        editor.putInt("totalGames", totalGames+1)
        editor.putInt("$totalGames totalCorrect", correct)
        editor.putInt("$totalGames totalWrong", wrong)
        editor.putInt("$totalGames totalTime", totalTime)
        editor.apply()
        Toast.makeText(applicationContext, "Time Up!", Toast.LENGTH_SHORT).show()
    }

    // handle game quite alert options on configuration changes
    private fun handleOrientation(){
        val orientation = this.resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (isAlert){
                Handler().post { showGameAlert()}
            }
        }
        else {
            if (isAlert){
                Handler().post { showGameAlert()}
            }
        }
    }

    // function to setup quite game alert options
    private fun showGameAlert() {
        alertDialog = AlertDialog.Builder(this, R.style.AlertDialogStyle) //set icon
            .setIcon(R.drawable.quite_alert) //set title
            .setTitle("Game is Running!") //set message
            .setCancelable(false)
            .setMessage(R.string.game_running_txt)
            // positive btn functionality
            .setPositiveButton(
                "YES"
            ) { _, _ ->
                val intent = Intent(this, MainActivity::class.java) //intent to GameActivity
                startActivity(intent)
                timer?.cancel()
                isGameOver = false
            } //set negative button
            .setNegativeButton(
                "NO"
            ) { _, _ ->
                Toast.makeText(applicationContext, "Continued!", Toast.LENGTH_SHORT).show()
                isAlert = false
            }
            .show()
    }
}
