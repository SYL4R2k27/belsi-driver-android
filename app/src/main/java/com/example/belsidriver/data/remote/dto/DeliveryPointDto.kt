package com.example.belsidriver.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeliveryPointDto(
    val id: String,
    @SerialName("route_id") val routeId: String,
    @SerialName("sequence_number") val sequenceNumber: Int,
    val address: String,
    val latitude: Double? = null,
    val longitude: Double? = null,
    @SerialName("contact_name") val contactName: String? = null,
    @SerialName("contact_phone") val contactPhone: String? = null,
    val notes: String? = null,
    val status: String,
    val events: List<PointEventDto> = emptyList()
)

@Serializable
data class PointEventDto(
    val id: String,
    @SerialName("delivery_point_id") val deliveryPointId: String,
    @SerialName("event_type") val eventType: String,
    val timestamp: String,
    @SerialName("photo_url") val photoUrl: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null
)
