package com.example.belsidriver.domain.model

data class AuthToken(
    val accessToken: String,
    val refreshToken: String
)
