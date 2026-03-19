package com.example.belsidriver.data.remote.api

import com.example.belsidriver.data.remote.dto.ShiftDto
import okhttp3.MultipartBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ShiftApi {

    @Multipart
    @POST("api/v1/shifts/start")
    suspend fun startShift(@Part photo: MultipartBody.Part): ShiftDto

    @Multipart
    @POST("api/v1/shifts/{shiftId}/end")
    suspend fun endShift(
        @Path("shiftId") shiftId: String,
        @Part photo: MultipartBody.Part
    ): ShiftDto

    @GET("api/v1/shifts/current")
    suspend fun getCurrentShift(): ShiftDto

    @GET("api/v1/shifts")
    suspend fun getShifts(
        @Query("driverId") driverId: String? = null,
        @Query("from") from: String? = null,
        @Query("to") to: String? = null,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): List<ShiftDto>
}
