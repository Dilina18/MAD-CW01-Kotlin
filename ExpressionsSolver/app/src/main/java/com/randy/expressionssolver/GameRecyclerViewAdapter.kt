package com.randy.expressionssolver

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

// extend Adapter class & customize
class GameRecyclerViewAdapter(private val mContext: Context, private val parentActivity: String) :
    RecyclerView.Adapter<GameRecyclerViewAdapter.GamesViewHolder>(){

    // hold object data for every game
    private var results: ArrayList<GameResult> = ArrayList()


    class GamesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        // declare views in a single recycler item
        private var parentCard:CardView? = null
         var txtGrade:TextView? = null
         var txtAverage:TextView? = null
         var txtTime:TextView? = null
        var imgGrade:ImageView? = null

        // initialize above views
        init {
            parentCard = itemView.findViewById(R.id.parentCard)
            txtGrade = itemView.findViewById(R.id.txtGrade)
            txtAverage = itemView.findViewById(R.id.txtAverage)
            txtTime = itemView.findViewById(R.id.txtTime)
            imgGrade = itemView.findViewById(R.id.imgGrade)
        }

    }

    // override function to set respective data on create ShowHistoryActivity
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GamesViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.history_list_item, parent, false)
        return GamesViewHolder(view)
    }

    // Bind object data to views
    override fun onBindViewHolder(holder: GamesViewHolder, position: Int) {
        holder.txtGrade?.text = results[position].getGrade()
        holder.imgGrade?.setImageResource(results[position].getImage())
        holder.txtAverage?.text = results[position].getPercentage().toString() + "%"
        holder.txtTime?.text = results[position].getTime().toString() + " sec"
    }

    // return result list size
    override fun getItemCount(): Int {
        return results.size
    }

    // function to set result arraylist
    fun setGames(results: ArrayList<GameResult>) {
        this.results = results
        notifyDataSetChanged()
    }
}
