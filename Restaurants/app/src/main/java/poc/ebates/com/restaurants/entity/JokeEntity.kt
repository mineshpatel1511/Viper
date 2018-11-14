package com.ebates.restaurants.poc.entity

import android.os.Parcel
import android.os.Parcelable

import com.google.gson.annotations.SerializedName

data class JokeEntity(val id: Int, @SerializedName("joke") val text: String) : Parcelable {
  constructor(parcel: Parcel) : this(parcel.readInt(), parcel.readString())

  override fun writeToParcel(parcel: Parcel, flags: Int) {
    parcel.writeInt(id)
    parcel.writeString(text)
  }

  override fun describeContents(): Int = 0

  companion object CREATOR : Parcelable.Creator<JokeEntity> {
    override fun createFromParcel(parcel: Parcel): JokeEntity = JokeEntity(parcel)

    override fun newArray(size: Int): Array<JokeEntity?> = arrayOfNulls(size)
  }
}
