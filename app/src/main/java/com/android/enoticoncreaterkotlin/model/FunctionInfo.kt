package com.android.enoticoncreaterkotlin.model

class FunctionInfo private constructor(var name: String) {

    companion object {
        const val NAME_TRIPLE_SEND = "表情三连发"
        const val NAME_SECRET = "告诉你个秘密"
        const val NAME_ONE_EMOTICON = "一个表情"
        const val NAME_GIF = "GIF"
        const val NAME_MATURE = "你已经很成熟了"

        fun createList(): ArrayList<FunctionInfo> {
            val functionList = ArrayList<FunctionInfo>()
            functionList.add(FunctionInfo(NAME_TRIPLE_SEND))
            functionList.add(FunctionInfo(NAME_SECRET))
            functionList.add(FunctionInfo(NAME_ONE_EMOTICON))
            functionList.add(FunctionInfo(NAME_GIF))
            functionList.add(FunctionInfo(NAME_MATURE))
            return functionList
        }
    }

    var resourceId: Int = 0
}