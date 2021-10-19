package com.clinicapp.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchPatientsResponse(
    @Json(name = "success")
    val status:Boolean,

    val patients:List<Patient>,

    val message:String?

) {
}