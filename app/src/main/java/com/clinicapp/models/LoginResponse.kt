package com.clinicapp.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginResponse(
    val success:Boolean,

    @Json(name = "access_token")
    val token:String?,

    val message:String?
)

