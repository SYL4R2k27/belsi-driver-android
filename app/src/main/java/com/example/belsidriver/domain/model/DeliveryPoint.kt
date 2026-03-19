package com.example.belsidriver.domain.model

data class DeliveryPoint(
    val id: String,
    val routeId: String,
    val sequenceNumber: Int,
    val address: String,
    val latitude: Double?,
    val longitude: Double?,
    val contactName: String?,
    val contactPhone: String?,
    val notes: String?,
    val status: PointStatus,
    val events: List<PointEvent>,
    val distanceFromPreviousMeters: Int? = null,
    val estimatedDurationMinutes: Int? = null
)

enum class PointStatus {
    PENDING,
    ARRIVED,
    DELIVERED
}
