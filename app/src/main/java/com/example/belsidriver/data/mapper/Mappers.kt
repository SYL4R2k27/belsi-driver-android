package com.example.belsidriver.data.mapper

import com.example.belsidriver.data.remote.dto.DeliveryPointDto
import com.example.belsidriver.data.remote.dto.PointEventDto
import com.example.belsidriver.data.remote.dto.RouteDto
import com.example.belsidriver.data.remote.dto.ShiftDto
import com.example.belsidriver.data.remote.dto.UserDto
import com.example.belsidriver.domain.model.DeliveryPoint
import com.example.belsidriver.domain.model.EventType
import com.example.belsidriver.domain.model.PointEvent
import com.example.belsidriver.domain.model.PointStatus
import com.example.belsidriver.domain.model.Route
import com.example.belsidriver.domain.model.RouteStatus
import com.example.belsidriver.domain.model.Shift
import com.example.belsidriver.domain.model.ShiftStatus
import com.example.belsidriver.domain.model.User
import com.example.belsidriver.domain.model.UserRole

fun UserDto.toDomain(): User = User(
    id = id,
    phone = phone,
    fullName = fullName,
    role = when (role.uppercase()) {
        "LOGISTICIAN" -> UserRole.LOGISTICIAN
        else -> UserRole.DRIVER
    },
    isActive = isActive
)

fun ShiftDto.toDomain(): Shift = Shift(
    id = id,
    driverId = driverId,
    startedAt = startedAt,
    endedAt = endedAt,
    startPhotoUrl = startPhotoUrl,
    endPhotoUrl = endPhotoUrl,
    status = when (status.uppercase()) {
        "COMPLETED" -> ShiftStatus.COMPLETED
        else -> ShiftStatus.ACTIVE
    }
)

fun RouteDto.toDomain(): Route = Route(
    id = id,
    driverId = driverId,
    shiftId = shiftId,
    createdBy = createdBy,
    title = title,
    status = when (status.uppercase()) {
        "IN_PROGRESS" -> RouteStatus.IN_PROGRESS
        "COMPLETED" -> RouteStatus.COMPLETED
        "CANCELLED" -> RouteStatus.CANCELLED
        else -> RouteStatus.PENDING
    },
    yandexMapsUrl = yandexMapsUrl,
    plannedDate = plannedDate,
    startedAt = startedAt,
    completedAt = completedAt,
    deliveryPoints = deliveryPoints.map { it.toDomain() }
)

fun DeliveryPointDto.toDomain(): DeliveryPoint = DeliveryPoint(
    id = id,
    routeId = routeId,
    sequenceNumber = sequenceNumber,
    address = address,
    latitude = latitude,
    longitude = longitude,
    contactName = contactName,
    contactPhone = contactPhone,
    notes = notes,
    status = when (status.uppercase()) {
        "ARRIVED" -> PointStatus.ARRIVED
        "DELIVERED" -> PointStatus.DELIVERED
        else -> PointStatus.PENDING
    },
    events = events.map { it.toDomain() }
)

fun PointEventDto.toDomain(): PointEvent = PointEvent(
    id = id,
    deliveryPointId = deliveryPointId,
    eventType = when (eventType.uppercase()) {
        "DEPARTURE" -> EventType.DEPARTURE
        "DELIVERY_COMPLETE" -> EventType.DELIVERY_COMPLETE
        else -> EventType.ARRIVAL
    },
    timestamp = timestamp,
    photoUrl = photoUrl,
    latitude = latitude,
    longitude = longitude
)
