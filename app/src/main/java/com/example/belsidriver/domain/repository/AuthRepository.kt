package com.example.belsidriver.domain.repository

import com.example.belsidriver.domain.model.User

interface AuthRepository {
    suspend fun login(phone: String, password: String): User
    suspend fun logout()
    suspend fun getCurrentUser(): User
    suspend fun isLoggedIn(): Boolean
    suspend fun getUserRole(): String?
}
