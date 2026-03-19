package com.example.belsidriver.domain.repository

import com.example.belsidriver.domain.model.DeliveryPoint
import java.io.File

interface DeliveryPointRepository {
    suspend fun getPoints(routeId: String): List<DeliveryPoint>
    suspend fun getPoint(routeId: String, pointId: String): DeliveryPoint
    suspend fun arriveAtPoint(
        routeId: String,
        pointId: String,
        photo: File,
        latitude: Double? = null,
        longitude: Double? = null
    ): DeliveryPoint

    suspend fun deliverAtPoint(
        routeId: String,
        pointId: String,
        photo: File,
        latitude: Double? = null,
        longitude: Double? = null
    ): DeliveryPoint

    suspend fun departFromPoint(routeId: String, pointId: String): DeliveryPoint
}
