package com.example.belsidriver.data.repository

import com.example.belsidriver.data.local.preferences.TokenStorage
import com.example.belsidriver.data.mapper.toDomain
import com.example.belsidriver.data.remote.api.AuthApi
import com.example.belsidriver.data.remote.dto.LoginRequestDto
import com.example.belsidriver.domain.model.User
import com.example.belsidriver.domain.repository.AuthRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val tokenStorage: TokenStorage
) : AuthRepository {

    override suspend fun login(phone: String, password: String): User {
        val response = authApi.login(LoginRequestDto(phone, password))
        tokenStorage.saveTokens(response.accessToken, response.refreshToken)
        tokenStorage.saveUserInfo(response.user.id, response.user.role)
        return response.user.toDomain()
    }

    override suspend fun logout() {
        try {
            authApi.logout()
        } finally {
            tokenStorage.clear()
        }
    }

    override suspend fun getCurrentUser(): User {
        return authApi.getCurrentUser().toDomain()
    }

    override suspend fun isLoggedIn(): Boolean {
        return tokenStorage.accessToken.firstOrNull() != null
    }

    override suspend fun getUserRole(): String? {
        return tokenStorage.userRole.firstOrNull()
    }
}
