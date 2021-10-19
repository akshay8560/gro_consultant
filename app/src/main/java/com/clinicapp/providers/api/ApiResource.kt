package com.clinicapp.providers.api

import com.clinicapp.models.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*


interface ApiResource {
    @POST("clinic/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST("patient/register")
    fun addPatient(@Body request: AddPatientRequest): Call<AddPatientResponse>

    @POST("patient/search")
    fun searchPatient(@Body request: AddPatientRequest): Call<SearchPatientsResponse>

    @POST("hair_analysis/create")
    fun createHairAnalysis(@Body request: HairAnalysisRequest): Call<HairAnalysisResponse>

    @Multipart
    @POST("hair_analysis/{analysisId}/images/upload")
    fun uploadImage(@Part mediaTye:MultipartBody.Part, @Part subType:MultipartBody.Part, @Part image:MultipartBody.Part,@Path( "analysisId") analysisID:Long): Call<UploadImageResponse>
}