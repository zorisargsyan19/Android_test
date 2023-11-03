package com.dexatel.mobileotp

class HelperFunctions {

    companion object {
        fun getRandomInvalidArmenianNumber(): String {
            val numbers = "0123456789"
            val randomNumber = (1..6)
                .map { numbers.random() }
                .joinToString("")
            return "18$randomNumber"
        }
    }
}