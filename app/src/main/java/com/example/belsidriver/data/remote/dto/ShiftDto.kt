package com.example.belsidriver.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ShiftDto(
    val id: String,
    @SerialName("driver_id") val driverId: String,
    @SerialName("started_at") val startedAt: String,
    @SerialName("ended_at") val endedAt: String? = null,
    @SerialName("start_photo_url") val startPhotoUrl: String,
    @SerialName("end_photo_url") val endPhotoUrl: String? = null,
    val status: String
)
