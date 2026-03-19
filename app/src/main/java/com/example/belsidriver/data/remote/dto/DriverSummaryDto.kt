package com.example.belsidriver.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DriverSummaryDto(
    val id: String,
    @SerialName("full_name") val fullName: String,
    val phone: String,
    @SerialName("current_shift") val currentShift: ShiftDto? = null,
    @SerialName("active_route") val activeRoute: RouteDto? = null,
    @SerialName("last_event_at") val lastEventAt: String? = null
)

@Serializable
data class PageDto<T>(
    val content: List<T>,
    val page: Int,
    val size: Int,
    @SerialName("total_pages") val totalPages: Int,
    @SerialName("total_elements") val totalElements: Int
)
