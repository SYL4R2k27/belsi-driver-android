package com.example.belsidriver.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RouteDto(
    val id: String,
    @SerialName("driver_id") val driverId: String,
    @SerialName("shift_id") val shiftId: String? = null,
    @SerialName("created_by") val createdBy: String,
    val title: String? = null,
    val status: String,
    @SerialName("yandex_maps_url") val yandexMapsUrl: String? = null,
    @SerialName("planned_date") val plannedDate: String,
    @SerialName("started_at") val startedAt: String? = null,
    @SerialName("completed_at") val completedAt: String? = null,
    @SerialName("delivery_points") val deliveryPoints: List<DeliveryPointDto> = emptyList()
)

@Serializable
data class CreateRouteRequestDto(
    @SerialName("driver_id") val driverId: String,
    val title: String? = null,
    @SerialName("planned_date") val plannedDate: String,
    @SerialName("yandex_maps_url") val yandexMapsUrl: String? = null,
    @SerialName("delivery_points") val deliveryPoints: List<CreateDeliveryPointDto>
)

@Serializable
data class CreateDeliveryPointDto(
    @SerialName("sequence_number") val sequenceNumber: Int,
    val address: String,
    val latitude: Double? = null,
    val longitude: Double? = null,
    @SerialName("contact_name") val contactName: String? = null,
    @SerialName("contact_phone") val contactPhone: String? = null,
    val notes: String? = null
)
