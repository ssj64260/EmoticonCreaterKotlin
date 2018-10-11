package com.android.enoticoncreaterkotlin.util

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.core.content.FileProvider
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class ImageUtils {
    companion object {
        fun saveToGif(os: ByteArrayOutputStream, path: String, fileName: String): File {
            val file = File(path, fileName)
            try {
                val out = FileOutputStream(file)
                out.write(os.toByteArray())
                out.flush()
                out.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return file
        }

        fun saveBitmapToJpg(bitmap: Bitmap, path: String, pictureName: String): File {
            return saveBitmapToJpg(bitmap, path, pictureName, 100)
        }

        fun saveBitmapToJpg(bitmap: Bitmap, path: String, pictureName: String, quality: Int): File {
            val file = File(path, pictureName)
            try {
                val out = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)
                out.flush()
                out.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return file
        }

        fun getUriFromFile(context: Context, file: File): Uri {
            val uri: Uri
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                uri = FileProvider.getUriForFile(context, context.packageName + ".fileProvider", file)
            } else {
                uri = Uri.fromFile(file)
            }
            return uri
        }

        fun getImageContentUri(context: Context, imageFile: File): Uri? {
            val filePath = imageFile.absolutePath
            val cursor = context.contentResolver.query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    arrayOf(MediaStore.Images.Media._ID),
                    MediaStore.Images.Media.DATA + "=? ",
                    arrayOf(filePath), null)

            return if (cursor != null && cursor.moveToFirst()) {
                val id = cursor.getInt(cursor
                        .getColumnIndex(MediaStore.MediaColumns._ID))
                val baseUri = Uri.parse("content://media/external/images/media")
                Uri.withAppendedPath(baseUri, "" + id)
            } else {
                if (imageFile.exists()) {
                    val values = ContentValues()
                    values.put(MediaStore.Images.Media.DATA, filePath)
                    context.contentResolver.insert(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                } else {
                    null
                }
            }
        }

        //根据content://media/external/images/media/***获取真实地址
        fun getContentImage(context: Context, uri: Uri): String {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = context.contentResolver.query(uri, proj, null, null, null)
            return if (cursor != null) {
                val actualImageColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                cursor.moveToFirst()
                cursor.getString(actualImageColumnIndex)
            } else {
                ""
            }
        }
    }

}