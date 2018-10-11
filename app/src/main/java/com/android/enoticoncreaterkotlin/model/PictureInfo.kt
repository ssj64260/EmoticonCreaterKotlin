package com.android.enoticoncreaterkotlin.model

import android.os.Parcel
import android.os.Parcelable

class PictureInfo() : Parcelable {

    var resourceId: Int = 0
    var title: String? = null
    var filePath: String? = null

    constructor(parcel: Parcel) : this() {
        resourceId = parcel.readInt()
        title = parcel.readString()
        filePath = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(resourceId)
        parcel.writeString(title)
        parcel.writeString(filePath)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PictureInfo> {
        override fun createFromParcel(parcel: Parcel): PictureInfo {
            return PictureInfo(parcel)
        }

        override fun newArray(size: Int): Array<PictureInfo?> {
            return arrayOfNulls(size)
        }
    }


}