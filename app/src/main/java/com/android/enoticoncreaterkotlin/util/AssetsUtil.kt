package com.android.enoticoncreaterkotlin.util

import android.content.Context
import java.io.IOException
import java.nio.charset.Charset

class AssetsUtil {

    companion object {
        fun getAssetsTxtByName(context: Context, name: String): String {
            return try {
                val inputStream = context.assets.open(name)
                val size = inputStream.available()

                val buffer = ByteArray(size)
                inputStream.read(buffer)
                inputStream.close()

                String(buffer, Charset.forName("UTF-8"))

            } catch (e: IOException) {
                e.printStackTrace()
                ""
            }
        }
    }

}