package com.example.belsidriver.data.remote.api

import com.example.belsidriver.data.remote.dto.DriverSummaryDto
import retrofit2.http.GET
import retrofit2.http.Path

interface DriverApi {

    @GET("api/v1/drivers")
    suspend fun getDrivers(): List<DriverSummaryDto>

    @GET("api/v1/drivers/{driverId}")
    suspend fun getDriver(@Path("driverId") driverId: String): DriverSummaryDto
}
