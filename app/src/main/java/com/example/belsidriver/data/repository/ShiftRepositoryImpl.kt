package com.example.belsidriver.data.repository

import com.example.belsidriver.data.mapper.toDomain
import com.example.belsidriver.data.remote.api.ShiftApi
import com.example.belsidriver.domain.model.Shift
import com.example.belsidriver.domain.repository.ShiftRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class ShiftRepositoryImpl @Inject constructor(
    private val shiftApi: ShiftApi
) : ShiftRepository {

    override suspend fun startShift(photo: File): Shift {
        val part = photo.toMultipartPart("photo")
        return shiftApi.startShift(part).toDomain()
    }

    override suspend fun endShift(shiftId: String, photo: File): Shift {
        val part = photo.toMultipartPart("photo")
        return shiftApi.endShift(shiftId, part).toDomain()
    }

    override suspend fun getCurrentShift(): Shift? {
        return try {
            shiftApi.getCurrentShift().toDomain()
        } catch (e: retrofit2.HttpException) {
            if (e.code() == 404) null else throw e
        }
    }

    override suspend fun getShifts(driverId: String?): List<Shift> {
        return shiftApi.getShifts(driverId = driverId).map { it.toDomain() }
    }

    private fun File.toMultipartPart(name: String): MultipartBody.Part {
        val requestBody = this.asRequestBody("image/jpeg".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(name, this.name, requestBody)
    }
}
