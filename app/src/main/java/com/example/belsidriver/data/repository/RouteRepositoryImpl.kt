package com.example.belsidriver.data.repository

import com.example.belsidriver.data.mapper.toDomain
import com.example.belsidriver.data.remote.api.RouteApi
import com.example.belsidriver.data.remote.dto.CreateDeliveryPointDto
import com.example.belsidriver.data.remote.dto.CreateRouteRequestDto
import com.example.belsidriver.domain.model.Route
import com.example.belsidriver.domain.repository.RoutePointInput
import com.example.belsidriver.domain.repository.RouteRepository
import javax.inject.Inject

class RouteRepositoryImpl @Inject constructor(
    private val routeApi: RouteApi
) : RouteRepository {

    override suspend fun createRoute(
        driverId: String,
        title: String?,
        plannedDate: String,
        yandexMapsUrl: String?,
        points: List<RoutePointInput>
    ): Route {
        val request = CreateRouteRequestDto(
            driverId = driverId,
            title = title,
            plannedDate = plannedDate,
            yandexMapsUrl = yandexMapsUrl,
            deliveryPoints = points.map {
                CreateDeliveryPointDto(
                    sequenceNumber = it.sequenceNumber,
                    address = it.address,
                    latitude = it.latitude,
                    longitude = it.longitude,
                    contactName = it.contactName,
                    contactPhone = it.contactPhone,
                    notes = it.notes
                )
            }
        )
        return routeApi.createRoute(request).toDomain()
    }

    override suspend fun getRoutes(driverId: String?, status: String?): List<Route> {
        return routeApi.getRoutes(driverId = driverId, status = status).map { it.toDomain() }
    }

    override suspend fun getRoute(routeId: String): Route {
        return routeApi.getRoute(routeId).toDomain()
    }

    override suspend fun startRoute(routeId: String): Route {
        return routeApi.startRoute(routeId).toDomain()
    }

    override suspend fun completeRoute(routeId: String): Route {
        return routeApi.completeRoute(routeId).toDomain()
    }

    override suspend fun deleteRoute(routeId: String) {
        routeApi.deleteRoute(routeId)
    }
}
