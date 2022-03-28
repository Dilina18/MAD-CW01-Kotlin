package com.randy.expressionssolver

import kotlin.random.Random

class Operations {

    private var valExp = 0.0 // solved val of current expression
    private val generatedExp: ArrayList<String> = ArrayList() // contain generated expressions

    // method to generate number of terms to use randomly
    private fun termsCountGenerator(): Int {
        return Random.nextInt(4) + 1
    }

    // method to get a list of random numbers to perform in the expression
    // length of the list is depend on generated term count (line 10)
    private fun termsListGenerator(termsCount: Int): ArrayList<String> {
        val numbers: ArrayList<Int> = ArrayList()
        val numbersToPerform: ArrayList<String> = ArrayList()
        for (i in 0 until termsCount) {
            var x: Int
            do {
                val random = Random.nextInt(20)+1
                x = random
            } while (numbers.contains(x))
            numbers.add(x)
            numbersToPerform.add(x.toString())
        }
        return numbersToPerform
    }

    // method to return a list operators used in the expression
    // note- (operators count = terms count -1 ) in a standard expression
    private fun operatorListGenerator(termsCount: Int): ArrayList<String> {
        val operators: ArrayList<String> = ArrayList()
        operators.add("+")
        operators.add("/")
        operators.add("*")
        operators.add("-")
        val operatorsToPerform: ArrayList<String> = ArrayList()
        for (i in 0 until termsCount - 1) {
            val random = Random.nextInt(4)
            operatorsToPerform.add(operators[random])
        }
        return operatorsToPerform
    }

    // method returns the calculated value for a given expression data
    private fun expressionEvaluator(values: ArrayList<String>, operators: ArrayList<String>) {
        if (values.size == 1) {
            valExp = values[0].toDouble() // set answer
        } else {
            var startVal = operatorConvertor(
                values[0].toDouble(), values[1].toDouble(),
                operators[0]
            )
            for (i in 1 until operators.size) {
                startVal = operatorConvertor(
                    startVal, values[i + 1].toDouble(),
                    operators[i]
                )
            }
            valExp = startVal // set answer
        }
    }

    // method return formatted expression for a given data (terms, operators)
    private fun expressionFormatter(values: ArrayList<String>, operators: ArrayList<String>): String {
        expressionEvaluator(values, operators)
        return if (values.size == 1) {
            java.lang.String.valueOf(values[0])
        } else {
            var finalExp = values[0] + operators[0] + values[1]
            for (i in 1 until operators.size) {
                val currentExp = operators[i] + values[i + 1]
                finalExp = bracketFormatter(finalExp) + currentExp
            }
            finalExp
        }
    }

    // method to format parentheses for a given string
    private fun bracketFormatter(exp: String): String {
        return "($exp)"
    }

    // method to do a arithmetic operation
    // by converting string operators
    private fun operatorConvertor(a: Double, b: Double, operator: String): Double {
        return when (operator) {
            "*" -> {
                a * b
            }
            "+" -> {
                a + b
            }
            "-" -> {
                a - b
            }
            "/" -> {
                a / b
            }
            else -> 1.0
        }
    }

    // returns the calculated val of last generated expression
    fun getValExp(): Double {
        return valExp
    }

    // method to returns an expression under following conditions
    // the evaluation of any sub part of exp is <= 100
    // the evaluation of any sub part of exp is an integer(whole number)
    private fun wholeNumberExpValidator(): String {
        var startVal: Double
        var numbers: ArrayList<String>
        var operators: ArrayList<String>
        do {
            val termsCount: Int = termsCountGenerator()
            numbers = termsListGenerator(termsCount)
            operators = operatorListGenerator(termsCount)
            if (numbers.size == 1) {
                startVal = numbers[0].toInt().toDouble()
            } else {
                startVal = operatorConvertor(numbers[0].toDouble(), numbers[1].toDouble(), operators[0])
                for (i in 1 until operators.size) {
                    startVal = operatorConvertor(startVal, numbers[i + 1].toDouble(), operators[i])
                }
            }

        } while (startVal % 1 != 0.0 || startVal >= 100)
        expressionEvaluator(numbers, operators)
        return expressionFormatter(numbers, operators)
    }

    // method to return an unique exp every time
    fun uniqueWholeNumberExpGenerator(): String{
        if (generatedExp.size == 2){
            generatedExp.clear()
        }
        var exp: String
        do {
            exp = wholeNumberExpValidator()
        } while (generatedExp.contains(exp))
        generatedExp.add(exp)
        return exp
    }
}
