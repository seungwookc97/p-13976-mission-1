package com.global.rq

class Rq(cmd: String) {
    val action : String
    private val paramMap = mutableMapOf<String, String>()

    init {
        val cmdBits = cmd.split("?", limit = 2)

        action = cmdBits[0].trim()

        if(cmdBits.size == 2) {
            val queryStr = cmdBits[1].trim()
            val queryBits = queryStr.split("&")

            for (queryBit in queryBits) {
                val queryParamBits = queryBit.split("=", limit = 2)
                if (queryParamBits.size != 2) {
                    continue

                }

                val key = queryParamBits[0].trim()
                val value = queryParamBits[1].trim()
                paramMap[key] = value
            }
        }
    }

    private fun getParamValue(name: String): String? {
        return paramMap[name]
    }

    fun getParamValue(name : String, defaultValue: String): String {
        return getParamValue(name) ?: defaultValue
    }

    fun getParamValueAsInt(name: String, defaultValue: Int): Int {
        val value = getParamValue(name)
        return if (value == null) {
            defaultValue
        } else {
            try {
                value.toInt()
            } catch (e: NumberFormatException) {
                defaultValue
            }
        }
    }

}