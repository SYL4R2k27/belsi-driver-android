package com.example.belsidriver.domain.model

data class Route(
    val id: String,
    val driverId: String,
    val shiftId: String?,
    val createdBy: String,
    val title: String?,
    val status: RouteStatus,
    val yandexMapsUrl: String?,
    val plannedDate: String,
    val startedAt: String?,
    val completedAt: String?,
    val deliveryPoints: List<DeliveryPoint>,
    val totalDistanceMeters: Int? = null,
    val totalEstimatedMinutes: Int? = null
)

enum class RouteStatus {
    PENDING,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED
}
