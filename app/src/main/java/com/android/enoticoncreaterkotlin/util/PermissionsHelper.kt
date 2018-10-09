package com.android.enoticoncreaterkotlin.util

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.enoticoncreaterkotlin.app.APP
import com.android.enoticoncreaterkotlin.widget.dialog.AlertDialogFragment

class PermissionsHelper private constructor(builder: Builder) {

    companion object {
        private val REQUEST_TO_SETTING = 1000
        private const val APP_NAME = "表情包生成器-Kotlin"
        private val CONTACTS_TIPS = "在设置-应用-$APP_NAME-权限中开启通讯录权限，以便正常使用该功能"
        private val PHONE_TIPS = "在设置-应用-$APP_NAME-权限中开启电话权限，以便正常使用该功能"
        private val CALENDAR_TIPS = "在设置-应用-$APP_NAME-权限中开启日历权限，以便正常使用该功能"
        private val CAMERA_TIPS = "在设置-应用-$APP_NAME-权限中开启相机权限，以便正常使用该功能"
        private val ACCESS_LOCATION_TIPS = "在设置-应用-$APP_NAME-权限中开启位置信息权限，以便正常使用该功能"
        private val EXTERNAL_STORAGE_TIPS = "在设置-应用-$APP_NAME-权限中开启存储权限，以便正常使用该功能"
        private val RECORD_AUDIO_TIPS = "在设置-应用-$APP_NAME-权限中开启麦克风权限，以便正常使用该功能"
        private val SMS_TIPS = "在设置-应用-$APP_NAME-权限中开启短信权限，以便正常使用该功能"
        private val BODY_SENSORS_TIPS = "在设置-应用-$APP_NAME-权限中开启身体传感器权限，以便正常使用该功能"
        private val DEFAULT_TIPS = "在设置-应用-$APP_NAME-权限中开启相应的权限，以便正常使用该功能"
    }

    private val permissionList: MutableList<String>
    private val errorTipsList: MutableList<String>

    private val mPermissionsResult: OnPermissionsResult?

    private var mPosition: Int = 0//当前请求权限位置

    init {
        permissionList = builder.permissionList
        errorTipsList = builder.errorTipsList
        mPermissionsResult = builder.permissionsResult

        var i = 0
        while (i < permissionList.size) {
            val permission = permissionList[i]
            if (hasBeenGranted(permission)) {
                permissionList.removeAt(i)
                errorTipsList.removeAt(i)
                i--
            }
            i++
        }
    }

    private fun chekcPermission(activity: Activity) {
        if (mPosition < permissionList.size) {
            val permission = permissionList[mPosition]

            if (hasBeenGranted(permission)) {
                requestNextPermissions(activity)
            } else {
                showTipsDialog(activity)
            }
        }
    }

    private fun hasBeenGranted(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(APP.INSTANCE, permission) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestNextPermissions(activity: Activity) {
        mPosition++

        requestPermissions(activity)
    }

    fun requestPermissions(activity: Activity) {
        if (mPosition < permissionList.size) {
            ActivityCompat.requestPermissions(activity, arrayOf(permissionList[mPosition]), mPosition)
        } else {
            mPermissionsResult?.allPermissionGranted()
        }
    }

    private fun showTipsDialog(activity: Activity) {
        val dialogClick = DialogInterface.OnClickListener { dialog, which ->
            dialog.dismiss()
            if (DialogInterface.BUTTON_POSITIVE == which) {
                AppManager.showInstalledAppDetails(activity, activity.packageName, REQUEST_TO_SETTING)
            } else if (DialogInterface.BUTTON_NEGATIVE == which) {
                mPermissionsResult?.cancelToSettings()
            }
        }

        val dialog = AlertDialogFragment()
        dialog.isCancelable = false
        dialog.setTitle("权限申请")
        dialog.setConfirmButton("去设置", dialogClick)
        dialog.setCancelButton("取消", dialogClick)
        dialog.setMessage(errorTipsList[mPosition])

        if (activity is AppCompatActivity) {
            dialog.show(activity.supportFragmentManager, "PermissionTipsDialog")
        } else {
            mPermissionsResult?.cancelToSettings()
        }
    }

    fun activityResult(activity: Activity, requestCode: Int) {
        if (REQUEST_TO_SETTING == requestCode) {
            chekcPermission(activity)
        }
    }

    fun requestPermissionsResult(activity: Activity, grantResults: IntArray) {
        if (grantResults.size > 0) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                showTipsDialog(activity)
            } else {
                requestNextPermissions(activity)
            }
        }
    }


    class Builder {

        val permissionList: ArrayList<String> = ArrayList()
        val errorTipsList: ArrayList<String> = ArrayList()
        var permissionsResult: OnPermissionsResult? = null

        //日历
        fun readCalendar(): Builder {
            if (!permissionList.contains(Manifest.permission.READ_CALENDAR)) {
                permissionList.add(Manifest.permission.READ_CALENDAR)
                errorTipsList.add(CALENDAR_TIPS)
            }
            return this
        }

        fun writeCalendar(): Builder {
            if (!permissionList.contains(Manifest.permission.WRITE_CALENDAR)) {
                permissionList.add(Manifest.permission.WRITE_CALENDAR)
                errorTipsList.add(CALENDAR_TIPS)
            }
            return this
        }

        //相机
        fun camera(): Builder {
            if (!permissionList.contains(Manifest.permission.CAMERA)) {
                permissionList.add(Manifest.permission.CAMERA)
                errorTipsList.add(CAMERA_TIPS)
            }
            return this
        }

        //通讯录
        fun readContacts(): Builder {
            if (!permissionList.contains(Manifest.permission.READ_CONTACTS)) {
                permissionList.add(Manifest.permission.READ_CONTACTS)
                errorTipsList.add(CONTACTS_TIPS)
            }
            return this
        }

        fun writeContacts(): Builder {
            if (!permissionList.contains(Manifest.permission.WRITE_CONTACTS)) {
                permissionList.add(Manifest.permission.WRITE_CONTACTS)
                errorTipsList.add(CONTACTS_TIPS)
            }
            return this
        }

        fun getAccounts(): Builder {
            if (!permissionList.contains(Manifest.permission.GET_ACCOUNTS)) {
                permissionList.add(Manifest.permission.GET_ACCOUNTS)
                errorTipsList.add(CONTACTS_TIPS)
            }
            return this
        }

        //位置信息
        fun accessFineLocation(): Builder {
            if (!permissionList.contains(Manifest.permission.ACCESS_FINE_LOCATION)) {
                permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION)
                errorTipsList.add(ACCESS_LOCATION_TIPS)
            }
            return this
        }

        fun accessCoarseLocation(): Builder {
            if (!permissionList.contains(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION)
                errorTipsList.add(ACCESS_LOCATION_TIPS)
            }
            return this
        }

        //麦克风
        fun recordAudio(): Builder {
            if (!permissionList.contains(Manifest.permission.RECORD_AUDIO)) {
                permissionList.add(Manifest.permission.RECORD_AUDIO)
                errorTipsList.add(RECORD_AUDIO_TIPS)
            }
            return this
        }

        //电话
        fun readPhoneState(): Builder {
            if (!permissionList.contains(Manifest.permission.READ_PHONE_STATE)) {
                permissionList.add(Manifest.permission.READ_PHONE_STATE)
                errorTipsList.add(PHONE_TIPS)
            }
            return this
        }

        fun callPhone(): Builder {
            if (!permissionList.contains(Manifest.permission.CALL_PHONE)) {
                permissionList.add(Manifest.permission.CALL_PHONE)
                errorTipsList.add(PHONE_TIPS)
            }
            return this
        }

        fun readCallLog(): Builder {
            if (!permissionList.contains(Manifest.permission.READ_CALL_LOG)) {
                permissionList.add(Manifest.permission.READ_CALL_LOG)
                errorTipsList.add(PHONE_TIPS)
            }
            return this
        }

        fun writeCallLog(): Builder {
            if (!permissionList.contains(Manifest.permission.WRITE_CALL_LOG)) {
                permissionList.add(Manifest.permission.WRITE_CALL_LOG)
                errorTipsList.add(PHONE_TIPS)
            }
            return this
        }

        fun useSip(): Builder {
            if (!permissionList.contains(Manifest.permission.USE_SIP)) {
                permissionList.add(Manifest.permission.USE_SIP)
                errorTipsList.add(PHONE_TIPS)
            }
            return this
        }

        fun processOutgoingCalls(): Builder {
            if (!permissionList.contains(Manifest.permission.PROCESS_OUTGOING_CALLS)) {
                permissionList.add(Manifest.permission.PROCESS_OUTGOING_CALLS)
                errorTipsList.add(PHONE_TIPS)
            }
            return this
        }

        //短信
        fun sendSms(): Builder {
            if (!permissionList.contains(Manifest.permission.SEND_SMS)) {
                permissionList.add(Manifest.permission.SEND_SMS)
                errorTipsList.add(SMS_TIPS)
            }
            return this
        }

        fun receiveSms(): Builder {
            if (!permissionList.contains(Manifest.permission.RECEIVE_SMS)) {
                permissionList.add(Manifest.permission.RECEIVE_SMS)
                errorTipsList.add(SMS_TIPS)
            }
            return this
        }

        fun readSms(): Builder {
            if (!permissionList.contains(Manifest.permission.READ_SMS)) {
                permissionList.add(Manifest.permission.READ_SMS)
                errorTipsList.add(SMS_TIPS)
            }
            return this
        }

        fun receiveWapPush(): Builder {
            if (!permissionList.contains(Manifest.permission.RECEIVE_WAP_PUSH)) {
                permissionList.add(Manifest.permission.RECEIVE_WAP_PUSH)
                errorTipsList.add(SMS_TIPS)
            }
            return this
        }

        fun receiveMms(): Builder {
            if (!permissionList.contains(Manifest.permission.RECEIVE_MMS)) {
                permissionList.add(Manifest.permission.RECEIVE_MMS)
                errorTipsList.add(SMS_TIPS)
            }
            return this
        }

        //存储
        fun readExternalStorage(): Builder {
            if (!permissionList.contains(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE)
                errorTipsList.add(EXTERNAL_STORAGE_TIPS)
            }
            return this
        }

        fun writeExternalStorage(): Builder {
            if (!permissionList.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                errorTipsList.add(EXTERNAL_STORAGE_TIPS)
            }
            return this
        }

        //身体传感器
        fun bodySensors(): Builder {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT && !permissionList.contains(Manifest.permission.BODY_SENSORS)) {
                permissionList.add(Manifest.permission.BODY_SENSORS)
                errorTipsList.add(BODY_SENSORS_TIPS)
            }
            return this
        }

        fun add(permissions: String): Builder {
            if (!permissionList.contains(permissions)) {
                permissionList.add(permissions)
                errorTipsList.add(DEFAULT_TIPS)
            }
            return this
        }

        fun setPermissionsResult(permissionsResult: OnPermissionsResult): Builder {
            this.permissionsResult = permissionsResult
            return this
        }

        fun bulid(): PermissionsHelper {
            return PermissionsHelper(this)
        }
    }

    interface OnPermissionsResult {
        fun allPermissionGranted()
        fun cancelToSettings()
    }
}