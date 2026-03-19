package com.example.belsidriver.presentation.logistician.driver

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.belsidriver.data.remote.dto.DriverSummaryDto
import com.example.belsidriver.domain.model.Route
import com.example.belsidriver.domain.model.Shift
import com.example.belsidriver.domain.repository.DriverRepository
import com.example.belsidriver.domain.repository.RouteRepository
import com.example.belsidriver.domain.repository.ShiftRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DriverDetailUiState(
    val isLoading: Boolean = true,
    val driver: DriverSummaryDto? = null,
    val routes: List<Route> = emptyList(),
    val shifts: List<Shift> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class DriverDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val driverRepository: DriverRepository,
    private val routeRepository: RouteRepository,
    private val shiftRepository: ShiftRepository
) : ViewModel() {

    val driverId: String = savedStateHandle["driverId"] ?: ""

    private val _uiState = MutableStateFlow(DriverDetailUiState())
    val uiState: StateFlow<DriverDetailUiState> = _uiState.asStateFlow()

    init {
        loadDriverDetail()
    }

    fun loadDriverDetail() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val driver = driverRepository.getDriver(driverId)
                val routes = routeRepository.getRoutes(driverId = driverId)
                val shifts = shiftRepository.getShifts(driverId = driverId)

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    driver = driver,
                    routes = routes,
                    shifts = shifts
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Ошибка загрузки"
                )
            }
        }
    }
}
