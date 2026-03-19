package com.example.belsidriver.domain.repository

import com.example.belsidriver.data.remote.dto.DriverSummaryDto

interface DriverRepository {
    suspend fun getDrivers(): List<DriverSummaryDto>
    suspend fun getDriver(driverId: String): DriverSummaryDto
}
