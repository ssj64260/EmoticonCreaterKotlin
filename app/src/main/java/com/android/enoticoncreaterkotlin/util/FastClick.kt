package com.android.enoticoncreaterkotlin.util

class FastClick {

    companion object {
        private var lastClickTime: Long = 0
        private var exitClickTime: Long = 0

        fun isExitClick(): Boolean {
            val time = System.currentTimeMillis()
            if (time - exitClickTime < 2000) {
                return true
            }
            exitClickTime = time
            return false
        }
    }
}