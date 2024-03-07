package com.example.piggy_bank_assistant.domain

import android.os.Parcel
import android.os.Parcelable

data class Category(
    val id: Int = UNDEFINED_ID,
    val name: String,
    val amount: Float,
    val color: Int
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readFloat(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeFloat(amount)
        parcel.writeInt(color)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Category> {
        override fun createFromParcel(parcel: Parcel): Category {
            return Category(parcel)
        }

        override fun newArray(size: Int): Array<Category?> {
            return arrayOfNulls(size)
        }

        const val UNDEFINED_ID = 0
        fun getDefaultInstance(): Category = Category(UNDEFINED_ID,"", 0.0f, 0)
    }
}
