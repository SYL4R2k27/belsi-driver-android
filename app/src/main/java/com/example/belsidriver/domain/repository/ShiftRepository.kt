package com.example.belsidriver.domain.repository

import com.example.belsidriver.domain.model.Shift
import java.io.File

interface ShiftRepository {
    suspend fun startShift(photo: File): Shift
    suspend fun endShift(shiftId: String, photo: File): Shift
    suspend fun getCurrentShift(): Shift?
    suspend fun getShifts(driverId: String? = null): List<Shift>
}
