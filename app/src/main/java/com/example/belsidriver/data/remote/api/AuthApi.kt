package com.example.belsidriver.data.remote.api

import com.example.belsidriver.data.remote.dto.LoginRequestDto
import com.example.belsidriver.data.remote.dto.LoginResponseDto
import com.example.belsidriver.data.remote.dto.RefreshRequestDto
import com.example.belsidriver.data.remote.dto.RefreshResponseDto
import com.example.belsidriver.data.remote.dto.UserDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApi {

    @POST("api/v1/auth/login")
    suspend fun login(@Body request: LoginRequestDto): LoginResponseDto

    @POST("api/v1/auth/refresh")
    suspend fun refresh(@Body request: RefreshRequestDto): RefreshResponseDto

    @POST("api/v1/auth/logout")
    suspend fun logout()

    @GET("api/v1/auth/me")
    suspend fun getCurrentUser(): UserDto
}
