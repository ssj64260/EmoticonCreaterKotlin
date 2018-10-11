package com.android.enoticoncreaterkotlin.model

import android.os.Parcel
import android.os.Parcelable

class GifTheme() : Parcelable {
    var id: Long = 0//主题ID
    var name: String? = null//主题名称
    var fileName: String? = null//主题文件名
    var textSize: Float = 0.toFloat()//文字大小
    var maxLength: Int = 0//文字最大数量
    var duration: Int = 0//每张图间隔时间
    var textList: List<GifText>? = null//文字列表

    constructor(parcel: Parcel) : this() {
        id = parcel.readLong()
        name = parcel.readString()
        fileName = parcel.readString()
        textSize = parcel.readFloat()
        maxLength = parcel.readInt()
        duration = parcel.readInt()
        textList = parcel.createTypedArrayList(GifText)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(name)
        parcel.writeString(fileName)
        parcel.writeFloat(textSize)
        parcel.writeInt(maxLength)
        parcel.writeInt(duration)
        parcel.writeTypedList(textList)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GifTheme> {
        override fun createFromParcel(parcel: Parcel): GifTheme {
            return GifTheme(parcel)
        }

        override fun newArray(size: Int): Array<GifTheme?> {
            return arrayOfNulls(size)
        }
    }


}