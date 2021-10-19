package com.clinicapp.models

import com.squareup.moshi.JsonClass
@JsonClass(generateAdapter = true)
class HairAnalysisRequest (
        val patient_id:Long
)