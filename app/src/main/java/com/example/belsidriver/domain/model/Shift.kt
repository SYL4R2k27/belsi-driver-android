package com.example.belsidriver.domain.model

data class Shift(
    val id: String,
    val driverId: String,
    val startedAt: String,
    val endedAt: String?,
    val startPhotoUrl: String,
    val endPhotoUrl: String?,
    val status: ShiftStatus
)

enum class ShiftStatus {
    ACTIVE,
    COMPLETED
}
