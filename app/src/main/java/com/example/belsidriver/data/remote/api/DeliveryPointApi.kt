package com.example.belsidriver.data.remote.api

import com.example.belsidriver.data.remote.dto.DeliveryPointDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface DeliveryPointApi {

    @GET("api/v1/routes/{routeId}/points")
    suspend fun getPoints(@Path("routeId") routeId: String): List<DeliveryPointDto>

    @GET("api/v1/routes/{routeId}/points/{pointId}")
    suspend fun getPoint(
        @Path("routeId") routeId: String,
        @Path("pointId") pointId: String
    ): DeliveryPointDto

    @Multipart
    @POST("api/v1/routes/{routeId}/points/{pointId}/arrive")
    suspend fun arriveAtPoint(
        @Path("routeId") routeId: String,
        @Path("pointId") pointId: String,
        @Part photo: MultipartBody.Part,
        @Part("latitude") latitude: RequestBody? = null,
        @Part("longitude") longitude: RequestBody? = null
    ): DeliveryPointDto

    @Multipart
    @POST("api/v1/routes/{routeId}/points/{pointId}/deliver")
    suspend fun deliverAtPoint(
        @Path("routeId") routeId: String,
        @Path("pointId") pointId: String,
        @Part photo: MultipartBody.Part,
        @Part("latitude") latitude: RequestBody? = null,
        @Part("longitude") longitude: RequestBody? = null
    ): DeliveryPointDto

    @POST("api/v1/routes/{routeId}/points/{pointId}/depart")
    suspend fun departFromPoint(
        @Path("routeId") routeId: String,
        @Path("pointId") pointId: String
    ): DeliveryPointDto
}
