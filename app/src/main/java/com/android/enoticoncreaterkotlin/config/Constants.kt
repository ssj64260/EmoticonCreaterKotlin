package com.android.enoticoncreaterkotlin.config

import java.io.File

class Constants {
    companion object {
        const val BUGLY_APP_ID = "06b4b6de0f"//TODO 更改Bugly App Id

        val PATH_BASE = "${File.separator}表情包生成器${File.separator}"
        val PATH_TRIPLE_SEND = "${PATH_BASE}表情三连发${File.separator}"
        val PATH_SECRET = "${PATH_BASE}告诉你个秘密${File.separator}"
        val PATH_ONE_EMOTICON = "${PATH_BASE}一个表情${File.separator}"
        val PATH_GIF = "${PATH_BASE}GIF${File.separator}"
        val PATH_MATURE = "${PATH_BASE}你已经很成熟了${File.separator}"
        val PATH_ALL_WICKED = "${PATH_BASE}全员恶人${File.separator}"

        const val KEY_RETURN_DATA = "key_return_data"//ActivityResult
    }
}