package com.android.enoticoncreaterkotlin.model

import android.os.Parcel
import android.os.Parcelable

class GifText() : Parcelable {

    var id: Long = 0//文字ID
    var hint: String? = null//提示内容
    var text: String? = null//文字内容
    var startFrame: Int = 0//开始帧
    var endFrame: Int = 0//结束帧

    constructor(parcel: Parcel) : this() {
        id = parcel.readLong()
        hint = parcel.readString()
        text = parcel.readString()
        startFrame = parcel.readInt()
        endFrame = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(hint)
        parcel.writeString(text)
        parcel.writeInt(startFrame)
        parcel.writeInt(endFrame)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GifText> {
        override fun createFromParcel(parcel: Parcel): GifText {
            return GifText(parcel)
        }

        override fun newArray(size: Int): Array<GifText?> {
            return arrayOfNulls(size)
        }
    }


}