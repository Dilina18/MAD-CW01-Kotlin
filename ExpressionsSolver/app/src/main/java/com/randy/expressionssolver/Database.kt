package com.randy.expressionssolver


// singleton class to store game data
object Database {

    private var pastGames: ArrayList<GameResult>? = null

    // initialize sample game data
    private fun initData() {
//        pastGames!!.add(GameResult(5,  2, 10))
//        pastGames!!.add(GameResult(10, 4, 23))
//        pastGames!!.add(GameResult(15, 1, 28))
//        pastGames!!.add(GameResult(20, 5, 52))
//        pastGames!!.add(GameResult(25, 3, 68))
    }

    // method to get games list
    fun getPastGames(): ArrayList<GameResult>? {
        return pastGames
    }

    // avoid re-initialize game data
    init {
        if (null == pastGames) {
            pastGames = ArrayList()
            initData()
        }
    }
}
