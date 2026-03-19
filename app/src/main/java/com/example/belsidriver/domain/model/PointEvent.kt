package com.example.belsidriver.domain.model

data class PointEvent(
    val id: String,
    val deliveryPointId: String,
    val eventType: EventType,
    val timestamp: String,
    val photoUrl: String?,
    val latitude: Double?,
    val longitude: Double?
)

enum class EventType {
    ARRIVAL,
    DEPARTURE,
    DELIVERY_COMPLETE
}
