package com.example.belsidriver.domain.repository

import com.example.belsidriver.domain.model.Route

interface RouteRepository {
    suspend fun createRoute(
        driverId: String,
        title: String?,
        plannedDate: String,
        yandexMapsUrl: String?,
        points: List<RoutePointInput>
    ): Route

    suspend fun getRoutes(
        driverId: String? = null,
        status: String? = null
    ): List<Route>

    suspend fun getRoute(routeId: String): Route
    suspend fun startRoute(routeId: String): Route
    suspend fun completeRoute(routeId: String): Route
    suspend fun deleteRoute(routeId: String)
}

data class RoutePointInput(
    val sequenceNumber: Int,
    val address: String,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val contactName: String? = null,
    val contactPhone: String? = null,
    val notes: String? = null
)
