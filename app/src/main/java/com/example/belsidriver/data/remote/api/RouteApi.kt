package com.example.belsidriver.data.remote.api

import com.example.belsidriver.data.remote.dto.CreateRouteRequestDto
import com.example.belsidriver.data.remote.dto.RouteDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface RouteApi {

    @POST("api/v1/routes")
    suspend fun createRoute(@Body request: CreateRouteRequestDto): RouteDto

    @GET("api/v1/routes")
    suspend fun getRoutes(
        @Query("driverId") driverId: String? = null,
        @Query("status") status: String? = null,
        @Query("plannedDate") plannedDate: String? = null,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): List<RouteDto>

    @GET("api/v1/routes/{routeId}")
    suspend fun getRoute(@Path("routeId") routeId: String): RouteDto

    @PUT("api/v1/routes/{routeId}")
    suspend fun updateRoute(
        @Path("routeId") routeId: String,
        @Body request: CreateRouteRequestDto
    ): RouteDto

    @POST("api/v1/routes/{routeId}/start")
    suspend fun startRoute(@Path("routeId") routeId: String): RouteDto

    @POST("api/v1/routes/{routeId}/complete")
    suspend fun completeRoute(@Path("routeId") routeId: String): RouteDto

    @DELETE("api/v1/routes/{routeId}")
    suspend fun deleteRoute(@Path("routeId") routeId: String)
}
