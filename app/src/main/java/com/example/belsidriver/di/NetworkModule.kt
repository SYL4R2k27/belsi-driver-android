package com.example.belsidriver.di

import com.example.belsidriver.data.local.preferences.TokenStorage
import com.example.belsidriver.data.remote.api.AuthApi
import com.example.belsidriver.data.remote.api.DeliveryPointApi
import com.example.belsidriver.data.remote.api.DriverApi
import com.example.belsidriver.data.remote.api.RouteApi
import com.example.belsidriver.data.remote.api.ShiftApi
import com.example.belsidriver.data.remote.interceptor.AuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    // TODO: Replace with actual server URL
    private const val BASE_URL = "http://10.0.2.2:8000/"

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        encodeDefaults = true
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(tokenStorage: TokenStorage): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(tokenStorage))
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, json: Json): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi =
        retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideShiftApi(retrofit: Retrofit): ShiftApi =
        retrofit.create(ShiftApi::class.java)

    @Provides
    @Singleton
    fun provideRouteApi(retrofit: Retrofit): RouteApi =
        retrofit.create(RouteApi::class.java)

    @Provides
    @Singleton
    fun provideDeliveryPointApi(retrofit: Retrofit): DeliveryPointApi =
        retrofit.create(DeliveryPointApi::class.java)

    @Provides
    @Singleton
    fun provideDriverApi(retrofit: Retrofit): DriverApi =
        retrofit.create(DriverApi::class.java)
}
