package com.android.enoticoncreaterkotlin.util

import android.content.Context
import android.text.format.Formatter
import java.io.File
import java.io.FileInputStream

class FileUtils {

    companion object {
        fun createdirectory(path: String): Boolean {
            val file = File(path)
            return if (!file.exists()) {
                file.mkdirs()
            } else {
                true
            }
        }

        //获取文件大小
        @Throws(Exception::class)
        fun getFileSize(f: File): Long {
            var s: Long = 0
            if (f.exists()) {
                val fis: FileInputStream
                fis = FileInputStream(f)
                s = fis.available().toLong()
            }
            return s
        }

        //获取文件夹大小
        @Throws(Exception::class)
        fun getDirSize(f: File): Long {
            var size: Long = 0
            val flist = f.listFiles()
            for (i in flist!!.indices) {
                if (flist[i].isDirectory) {
                    size += getDirSize(flist[i])
                } else {
                    size += flist[i].length()
                }
            }
            return size
        }

        //转换文件大小单位(B/KB/MB/GB)
        fun formatFileSize(context: Context, size: Long): String {
            return Formatter.formatFileSize(context, size)
        }

        //获取文件个数
        fun getlist(f: File): Long {// 递归求取目录文件个数
            var size: Long = 0
            val flist = f.listFiles()
            size = flist!!.size.toLong()
            for (i in flist.indices) {
                if (flist[i].isDirectory) {
                    size = size + getlist(flist[i])
                    size--
                }
            }
            return size
        }

        fun createFile(folderPath: String, fileName: String): File {
            val destDir = File(folderPath)
            if (!destDir.exists()) {
                destDir.mkdirs()
            }
            return File(folderPath, fileName)
        }
    }

}