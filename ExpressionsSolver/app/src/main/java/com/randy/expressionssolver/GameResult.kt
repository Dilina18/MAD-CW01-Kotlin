package com.randy.expressionssolver

import kotlin.math.roundToInt

// use this class to create instance on finishing each game
class GameResult(private var correct: Int, private var wrong:Int, private var time:Int) {

    // method to get image based on grade
    fun getImage(): Int{
        val average:Double = ((correct.toDouble()/(correct.toDouble()+wrong.toDouble()))*100)
        return when {
            average >= 90.0 -> {
                R.drawable.gold_ic
            }
            average >= 75.0 -> {
                R.drawable.silver_ic
            }
            average >= 50.0 -> {
                R.drawable.bronze_ic
            }
            else -> {
                R.drawable.try_ic
            }
        }
    }

    // method to get average based on answers
    fun getPercentage(): Double {
        if (correct > 0 || wrong > 0 ){
            return ((correct.toDouble() / (correct.toDouble() + wrong.toDouble())) * 100).roundToInt().toDouble()
        }
        return 0.0
    }

    // method to set grade name based on average
    fun getGrade():String{
        val average:Double = ((correct.toDouble()/(correct.toDouble()+wrong.toDouble()))*100)
        return when {
            average >= 90.0 -> {
                "Excellent!"
            }
            average >= 75.0 -> {
                "Great!"
            }
            average >= 50.0 -> {
                "Good!"
            }
            else -> {
                "Try!"
            }
        }
    }

    // method to return total time spent on a game round
    fun getTime(): Int {
        return time
    }
}