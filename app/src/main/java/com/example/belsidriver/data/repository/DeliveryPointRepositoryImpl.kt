package com.example.belsidriver.data.repository

import com.example.belsidriver.data.mapper.toDomain
import com.example.belsidriver.data.remote.api.DeliveryPointApi
import com.example.belsidriver.domain.model.DeliveryPoint
import com.example.belsidriver.domain.repository.DeliveryPointRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class DeliveryPointRepositoryImpl @Inject constructor(
    private val deliveryPointApi: DeliveryPointApi
) : DeliveryPointRepository {

    override suspend fun getPoints(routeId: String): List<DeliveryPoint> {
        return deliveryPointApi.getPoints(routeId).map { it.toDomain() }
    }

    override suspend fun getPoint(routeId: String, pointId: String): DeliveryPoint {
        return deliveryPointApi.getPoint(routeId, pointId).toDomain()
    }

    override suspend fun arriveAtPoint(
        routeId: String,
        pointId: String,
        photo: File,
        latitude: Double?,
        longitude: Double?
    ): DeliveryPoint {
        val photoPart = photo.toMultipartPart("photo")
        val latBody = latitude?.toString()?.toRequestBody("text/plain".toMediaTypeOrNull())
        val lngBody = longitude?.toString()?.toRequestBody("text/plain".toMediaTypeOrNull())
        return deliveryPointApi.arriveAtPoint(routeId, pointId, photoPart, latBody, lngBody).toDomain()
    }

    override suspend fun deliverAtPoint(
        routeId: String,
        pointId: String,
        photo: File,
        latitude: Double?,
        longitude: Double?
    ): DeliveryPoint {
        val photoPart = photo.toMultipartPart("photo")
        val latBody = latitude?.toString()?.toRequestBody("text/plain".toMediaTypeOrNull())
        val lngBody = longitude?.toString()?.toRequestBody("text/plain".toMediaTypeOrNull())
        return deliveryPointApi.deliverAtPoint(routeId, pointId, photoPart, latBody, lngBody).toDomain()
    }

    override suspend fun departFromPoint(routeId: String, pointId: String): DeliveryPoint {
        return deliveryPointApi.departFromPoint(routeId, pointId).toDomain()
    }

    private fun File.toMultipartPart(name: String): MultipartBody.Part {
        val requestBody = this.asRequestBody("image/jpeg".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(name, this.name, requestBody)
    }
}
