package com.android.enoticoncreaterkotlin.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings

class AppManager {

    companion object {
        private const val SCHEME = "package"

        //调用系统InstalledAppDetails界面所需的Extra名称(用于Android 2.1及之前版本)
        private const val APP_PKG_NAME_21 = "com.android.settings.ApplicationPkgName"

        //调用系统InstalledAppDetails界面所需的Extra名称(用于Android 2.2)
        private const val APP_PKG_NAME_22 = "pkg"

        //InstalledAppDetails所在包名
        private const val APP_DETAILS_PACKAGE_NAME = "com.android.settings"

        //InstalledAppDetails类名
        private const val APP_DETAILS_CLASS_NAME = "com.android.settings.InstalledAppDetails"

        /**
         * 调用系统InstalledAppDetails界面显示已安装应用程序的详细信息。 对于Android 2.3（Api Level
         * 9）以上，使用SDK提供的接口； 2.3以下，使用非公开的接口（查看InstalledAppDetails源码）。
         *
         * @param packageName 应用程序的包名
         */
        fun showInstalledAppDetails(context: Context, packageName: String, vararg requestCode: Int) {
            val intent = Intent()
            val apiLevel = Build.VERSION.SDK_INT
            if (apiLevel >= Build.VERSION_CODES.GINGERBREAD) { // 2.3（ApiLevel 9）以上，使用SDK提供的接口
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                val uri = Uri.fromParts(SCHEME, packageName, null)
                intent.data = uri
            } else {
                // 2.3以下，使用非公开的接口（查看InstalledAppDetails源码）
                // 2.2和2.1中，InstalledAppDetails使用的APP_PKG_NAME不同。
                val appPkgName = if (apiLevel == 8) APP_PKG_NAME_22 else APP_PKG_NAME_21
                intent.action = Intent.ACTION_VIEW
                intent.setClassName(APP_DETAILS_PACKAGE_NAME, APP_DETAILS_CLASS_NAME)
                intent.putExtra(appPkgName, packageName)
            }

            if (requestCode.isNotEmpty()) {
                (context as Activity).startActivityForResult(intent, requestCode[0])
            } else {
                context.startActivity(intent)
            }

        }
    }
}