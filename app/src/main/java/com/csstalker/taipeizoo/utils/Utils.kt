package com.csstalker.taipeizoo.utils


class Utils private constructor() {

    fun checkDisplayText(text: String?): String {
        return text?.trim { it <= ' ' } ?: ""
    }

    companion object {

        private var instance: Utils? = Utils()

        fun getInstance(): Utils {
            if (instance == null)
                instance = Utils()

            return instance as Utils
        }
    }


}
