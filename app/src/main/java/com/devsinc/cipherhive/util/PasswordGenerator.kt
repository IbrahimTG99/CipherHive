package com.devsinc.cipherhive.util

class PasswordGenerator(
    private var length: Int,
    private var includeUpperCaseLetters: Boolean,
    private var includeLowerCaseLetters: Boolean,
    private var includesymbols: Boolean,
    private var includenumbers: Boolean
) {

    constructor() : this(10, true, true, true, true)

    private val upperCase = 0
    private val lowerCase = 1
    private val numbers = 2
    private val symbols = 3

    fun generatePassword(): String {
        var password = ""
        val list = ArrayList<Int>()
        if (includeUpperCaseLetters) {
            list.add(upperCase)
        }
        if (includeLowerCaseLetters) {
            list.add(lowerCase)
        }
        if (includenumbers) {
            list.add(numbers)
        }
        if (includesymbols) {
            list.add(symbols)
        }

        for (i in 1..length) {
            if (list.isNotEmpty()) {
                when (list.random()) {
                    upperCase -> password += ('A'..'Z').random().toString()
                    lowerCase -> password += ('a'..'z').random().toString()
                    numbers -> password += ('0'..'9').random().toString()
                    symbols -> password += listOf(
                        '!', '@', '#', '$', '%', '&', '*', '+', '=', '-', '~', '?', '/', '_'
                    ).random().toString()
                }
            }
        }
        return password
    }
}
