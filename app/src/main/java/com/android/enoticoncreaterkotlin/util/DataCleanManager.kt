package com.android.enoticoncreaterkotlin.util

import android.content.Context
import android.os.Environment
import java.io.File

class DataCleanManager {

    companion object {
        //清除本应用内部缓存(/data/data/com.xxx.xxx/cache)
        fun cleanInternalCache(context: Context) {
            deleteAllFiles(context.cacheDir)
        }

        //清除外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache)
        fun cleanExternalCache(context: Context) {
            if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                deleteFilesByDirectory(context.externalCacheDir)
            }
        }

        //按名字清除本应用数据库
        fun cleanDatabaseByName(context: Context, dbName: String) {
            context.deleteDatabase(dbName)
        }

        //清除/data/data/com.xxx.xxx/files下的内容
        fun cleanFiles(context: Context) {
            deleteFilesByDirectory(context.filesDir)
        }

        //清除外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/files)
        fun cleanExternalFlies(context: Context) {
            if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                deleteFilesByDirectory(context.getExternalFilesDir(null))
            }
        }

        //清除自定义路径下的文件，使用需小心，请不要误删。而且只支持目录下的文件删除
        fun cleanCustomCache(filePath: String) {
            deleteFilesByDirectory(File(filePath))
        }

        //清除本应用所有的数据
        fun cleanApplicationData(context: Context, vararg filepath: String) {
            cleanInternalCache(context)
            cleanExternalCache(context)
            cleanFiles(context)
            cleanExternalFlies(context)
            for (filePath in filepath) {
                cleanCustomCache(filePath)
            }
        }

        //删除目录下所有文件包括子目录的文件
        fun deleteAllFiles(directory: File?) {
            if (directory != null && directory.exists() && directory.isDirectory) {
                for (item in directory.listFiles()!!) {
                    if (!item.isDirectory) {
                        item.delete()
                    } else {
                        deleteAllFiles(item)
                    }
                }
            }
        }

        /** * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理  */
        fun deleteFilesByDirectory(directory: File?) {
            if (directory != null && directory.exists() && directory.isDirectory) {
                for (item in directory.listFiles()!!) {
                    if (!item.isDirectory) {
                        item.delete()
                    }
                }
            }
        }

        //根据关键字删除文件
        private fun deleteFilesByDirectory(directory: File?, key: String) {
            if (directory != null && directory.exists() && directory.isDirectory) {
                for (item in directory.listFiles()!!) {
                    if (!item.isDirectory && item.name.indexOf(key) != -1) {
                        item.delete()
                    }
                }
            }
        }
    }

}