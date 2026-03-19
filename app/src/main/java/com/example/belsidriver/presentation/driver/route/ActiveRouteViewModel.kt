package com.example.belsidriver.presentation.driver.route

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.belsidriver.domain.model.DeliveryPoint
import com.example.belsidriver.domain.model.Route
import com.example.belsidriver.domain.repository.DeliveryPointRepository
import com.example.belsidriver.domain.repository.RouteRepository
import com.example.belsidriver.util.DistanceCalculator
import com.example.belsidriver.util.TimeCalculator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ActiveRouteUiState(
    val isLoading: Boolean = true,
    val route: Route? = null,
    val points: List<DeliveryPoint> = emptyList(),
    val error: String? = null,
    val totalDistance: String? = null,
    val totalEstimatedTime: String? = null,
    val totalActualTime: String? = null
)

@HiltViewModel
class ActiveRouteViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val routeRepository: RouteRepository,
    private val deliveryPointRepository: DeliveryPointRepository
) : ViewModel() {

    private val routeId: String = savedStateHandle["routeId"] ?: ""

    private val _uiState = MutableStateFlow(ActiveRouteUiState())
    val uiState: StateFlow<ActiveRouteUiState> = _uiState.asStateFlow()

    init {
        loadRoute()
    }

    fun loadRoute() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val route = routeRepository.getRoute(routeId)
                val points = deliveryPointRepository.getPoints(routeId)
                    .sortedBy { it.sequenceNumber }

                // Calculate totals
                val totalDistMeters = route.totalDistanceMeters
                    ?: DistanceCalculator.totalRouteDistance(points)
                val totalEstMin = route.totalEstimatedMinutes
                    ?: DistanceCalculator.totalEstimatedDuration(points)
                val totalActMin = TimeCalculator.totalActualTime(points)

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    route = route,
                    points = points,
                    totalDistance = if (totalDistMeters > 0) DistanceCalculator.formatDistance(totalDistMeters) else null,
                    totalEstimatedTime = if (totalEstMin > 0) DistanceCalculator.formatDuration(totalEstMin) else null,
                    totalActualTime = totalActMin?.let { DistanceCalculator.formatDuration(it) }
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Ошибка загрузки маршрута"
                )
            }
        }
    }
}
