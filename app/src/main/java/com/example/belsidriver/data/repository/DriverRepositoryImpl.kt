package com.example.belsidriver.data.repository

import com.example.belsidriver.data.remote.api.DriverApi
import com.example.belsidriver.data.remote.dto.DriverSummaryDto
import com.example.belsidriver.domain.repository.DriverRepository
import javax.inject.Inject

class DriverRepositoryImpl @Inject constructor(
    private val driverApi: DriverApi
) : DriverRepository {

    override suspend fun getDrivers(): List<DriverSummaryDto> {
        return driverApi.getDrivers()
    }

    override suspend fun getDriver(driverId: String): DriverSummaryDto {
        return driverApi.getDriver(driverId)
    }
}
