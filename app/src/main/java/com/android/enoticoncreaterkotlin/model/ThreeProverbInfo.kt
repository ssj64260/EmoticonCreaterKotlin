package com.android.enoticoncreaterkotlin.model

import android.os.Parcel
import android.os.Parcelable
import com.litesuits.orm.db.annotation.PrimaryKey
import com.litesuits.orm.db.annotation.Table
import com.litesuits.orm.db.enums.AssignType

@Table("ThreeProverb")
class ThreeProverbInfo() : Parcelable {

    @PrimaryKey(AssignType.AUTO_INCREMENT)
    var id: Long = 0
    var title: String? = null
    var firstProverb: String? = null
    var secondProverb: String? = null
    var thirdProverb: String? = null
    var useTimes: Long = 0

    constructor(parcel: Parcel) : this() {
        id = parcel.readLong()
        title = parcel.readString()
        firstProverb = parcel.readString()
        secondProverb = parcel.readString()
        thirdProverb = parcel.readString()
        useTimes = parcel.readLong()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(title)
        parcel.writeString(firstProverb)
        parcel.writeString(secondProverb)
        parcel.writeString(thirdProverb)
        parcel.writeLong(useTimes)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ThreeProverbInfo> {
        override fun createFromParcel(parcel: Parcel): ThreeProverbInfo {
            return ThreeProverbInfo(parcel)
        }

        override fun newArray(size: Int): Array<ThreeProverbInfo?> {
            return arrayOfNulls(size)
        }
    }

}