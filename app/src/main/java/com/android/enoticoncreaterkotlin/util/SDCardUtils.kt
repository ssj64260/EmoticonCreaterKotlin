package com.android.enoticoncreaterkotlin.util

import android.content.Context
import android.os.Build
import android.os.Environment
import android.os.StatFs

class SDCardUtils {

    companion object {
        fun getSDCardPublicDir(type: String): String {
            return Environment
                    .getExternalStoragePublicDirectory(type)
                    .absolutePath
        }

        //  /storage/emulated/0
        fun getSDCardDir(): String {
            return getSDCardPublicDir(Environment.DIRECTORY_DCIM)
        }

        //  /data/data/<application package>/cache
        fun getCacheDir(context: Context): String {
            return context.cacheDir.path
        }

        //  /data/data/<application package>/files
        fun getFilesDir(context: Context): String {
            return context.filesDir.path
        }

        //  /data/data/<application package>/databases
        fun getDataBaseDir(context: Context, dbName: String): String {
            return context.getDatabasePath(dbName).path
        }

        //  /storage/emulated/0/Android/data/你的应用包名/cache/（APP卸载后，数据会被删除）
        fun getExternalCacheDir(context: Context): String {
            return context.externalCacheDir!!.path
        }

        //  /storage/emulated/0/Android/data/你的应用的包名/files/（APP卸载后，数据会被删除）
        fun getExternalFilesDir(context: Context): String {
            return context.getExternalFilesDir(null)!!.path
        }

        //自动选择Flies路径，若SD卡存在并且不能移除则用SD卡存储
        fun getAutoFilesPath(context: Context): String {
            val filesPath: String
            if (ExistSDCard() && !SDCardRemovable()) {
                filesPath = getExternalFilesDir(context)
            } else {
                filesPath = getFilesDir(context)
            }
            return filesPath
        }

        //自动选择Cache路径，若SD卡存在并且不能移除则用SD卡存储
        fun getAutoCachePath(context: Context): String {
            val cachePath: String
            if (ExistSDCard() && !SDCardRemovable()) {
                cachePath = getExternalCacheDir(context)
            } else {
                cachePath = getCacheDir(context)
            }
            return cachePath
        }

        //检查SD卡是否存在
        fun ExistSDCard(): Boolean {
            return Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()
        }

        //检查SD卡是否能被移除
        fun SDCardRemovable(): Boolean {
            return Environment.isExternalStorageRemovable()
        }

        //获取内部存储剩余空间
        fun getFreeSpace(): Long {
            return Environment.getExternalStorageDirectory().freeSpace
        }

        //获取内部存储可用空间
        fun getUsableSpace(): Long {
            return Environment.getExternalStorageDirectory().usableSpace
        }

        //获取内部存储总空间
        fun getTotalSpace(): Long {
            return Environment.getExternalStorageDirectory().totalSpace
        }

        /**
         * 计算SD卡的剩余空间
         *
         * @return 返回-1，说明没有安装sd卡
         */
        fun getFreeDiskSpace(): Long {
            val status = Environment.getExternalStorageState()
            var freeSpace: Long = 0
            if (status == Environment.MEDIA_MOUNTED) {
                try {
                    val path = Environment.getExternalStorageDirectory()
                    val stat = StatFs(path.path)
                    val blockSize: Long
                    val availableBlocks: Long
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                        blockSize = stat.blockSizeLong
                        availableBlocks = stat.availableBlocksLong
                    } else {
                        blockSize = stat.blockSize.toLong()
                        availableBlocks = stat.availableBlocks.toLong()
                    }

                    freeSpace = availableBlocks * blockSize
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            } else {
                return -1
            }
            return freeSpace
        }
    }
}