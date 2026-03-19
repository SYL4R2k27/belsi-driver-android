package com.example.belsidriver.domain.model

data class User(
    val id: String,
    val phone: String,
    val fullName: String,
    val role: UserRole,
    val isActive: Boolean
)

enum class UserRole {
    DRIVER,
    LOGISTICIAN
}
