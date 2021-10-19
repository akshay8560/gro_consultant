package com.clinicapp.models

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
class Patient(
    val id:Long,
    val firstName:String?,
    val lastName:String?,
    val email:String?,
    val age:String?,
    val dob:String?,
    val gender:String?,
    val phone:String?,
    val clinicID:Long
) : Parcelable {

    fun getName():String {
        return "$firstName $lastName";
    }

    fun isFemale():Boolean {
        return this.gender == "F";
    }


}