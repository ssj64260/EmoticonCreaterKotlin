package com.android.enoticoncreaterkotlin.app

import android.app.Application
import com.android.enoticoncreaterkotlin.config.Constants
import com.tencent.bugly.crashreport.CrashReport

/**
 * application
 */

class APP : Application() {

    companion object {
        lateinit var INSTANCE: APP private set
    }

    override fun onCreate() {
        super.onCreate()

        INSTANCE = this

        CrashReport.initCrashReport(applicationContext, Constants.BUGLY_APP_ID, false)
    }

}
