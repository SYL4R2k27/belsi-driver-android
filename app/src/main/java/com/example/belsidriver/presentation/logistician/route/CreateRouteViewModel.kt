package com.example.belsidriver.presentation.logistician.route

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.belsidriver.domain.repository.RoutePointInput
import com.example.belsidriver.domain.repository.RouteRepository
import com.example.belsidriver.util.YandexRouteBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PointFormData(
    val address: String = "",
    val contactName: String = "",
    val contactPhone: String = "",
    val notes: String = ""
)

data class CreateRouteUiState(
    val title: String = "",
    val plannedDate: String = "",
    val points: List<PointFormData> = listOf(PointFormData()),
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class CreateRouteViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val routeRepository: RouteRepository
) : ViewModel() {

    val driverId: String = savedStateHandle["driverId"] ?: ""

    private val _uiState = MutableStateFlow(CreateRouteUiState())
    val uiState: StateFlow<CreateRouteUiState> = _uiState.asStateFlow()

    fun updateTitle(title: String) {
        _uiState.value = _uiState.value.copy(title = title)
    }

    fun updateDate(date: String) {
        _uiState.value = _uiState.value.copy(plannedDate = date)
    }

    fun updatePoint(index: Int, point: PointFormData) {
        val points = _uiState.value.points.toMutableList()
        if (index < points.size) {
            points[index] = point
            _uiState.value = _uiState.value.copy(points = points)
        }
    }

    fun addPoint() {
        val points = _uiState.value.points + PointFormData()
        _uiState.value = _uiState.value.copy(points = points)
    }

    fun removePoint(index: Int) {
        if (_uiState.value.points.size > 1) {
            val points = _uiState.value.points.toMutableList()
            points.removeAt(index)
            _uiState.value = _uiState.value.copy(points = points)
        }
    }

    /**
     * Строит URL маршрута для предпросмотра на Яндекс.Картах.
     * @return URL или null если адресов < 2
     */
    fun buildPreviewUrl(): String? {
        val addresses = _uiState.value.points
            .map { it.address }
            .filter { it.isNotBlank() }
        return YandexRouteBuilder.buildUrl(addresses)
    }

    fun createRoute() {
        val state = _uiState.value
        if (state.plannedDate.isBlank()) {
            _uiState.value = state.copy(error = "Укажите дату")
            return
        }

        val validPoints = state.points.filter { it.address.isNotBlank() }
        if (validPoints.isEmpty()) {
            _uiState.value = state.copy(error = "Добавьте хотя бы одну точку с адресом")
            return
        }

        // Auto-generate Yandex Maps URL from addresses
        val yandexUrl = YandexRouteBuilder.buildUrl(validPoints.map { it.address })

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                routeRepository.createRoute(
                    driverId = driverId,
                    title = state.title.ifBlank { null },
                    plannedDate = state.plannedDate,
                    yandexMapsUrl = yandexUrl,
                    points = validPoints.mapIndexed { index, point ->
                        RoutePointInput(
                            sequenceNumber = index + 1,
                            address = point.address,
                            contactName = point.contactName.ifBlank { null },
                            contactPhone = point.contactPhone.ifBlank { null },
                            notes = point.notes.ifBlank { null }
                        )
                    }
                )
                _uiState.value = _uiState.value.copy(isLoading = false, success = true)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Ошибка создания маршрута"
                )
            }
        }
    }
}
