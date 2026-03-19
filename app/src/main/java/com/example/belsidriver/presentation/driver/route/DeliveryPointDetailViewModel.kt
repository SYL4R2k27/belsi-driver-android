package com.example.belsidriver.presentation.driver.route

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.belsidriver.domain.model.DeliveryPoint
import com.example.belsidriver.domain.model.EventType
import com.example.belsidriver.domain.model.PointEvent
import com.example.belsidriver.domain.repository.DeliveryPointRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DeliveryPointDetailUiState(
    val isLoading: Boolean = true,
    val point: DeliveryPoint? = null,
    val error: String? = null
)

@HiltViewModel
class DeliveryPointDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val deliveryPointRepository: DeliveryPointRepository
) : ViewModel() {

    val routeId: String = savedStateHandle["routeId"] ?: ""
    val pointId: String = savedStateHandle["pointId"] ?: ""

    private val _uiState = MutableStateFlow(DeliveryPointDetailUiState())
    val uiState: StateFlow<DeliveryPointDetailUiState> = _uiState.asStateFlow()

    init {
        loadPointDetail()
    }

    private fun loadPointDetail() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val point = deliveryPointRepository.getPoint(routeId, pointId)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    point = point
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Ошибка загрузки данных точки"
                )
            }
        }
    }

    fun getTimelineEvents(): List<Pair<String, String>> {
        val point = _uiState.value.point ?: return emptyList()
        val events = point.events.sortedBy { it.timestamp }
        val result = mutableListOf<Pair<String, String>>()

        val arrivalEvent = events.firstOrNull { it.eventType == EventType.ARRIVAL }
        val departureEvent = events.firstOrNull { it.eventType == EventType.DEPARTURE }
        val deliveryEvent = events.firstOrNull { it.eventType == EventType.DELIVERY_COMPLETE }

        if (arrivalEvent != null) {
            result.add("Прибытие" to formatTimestamp(arrivalEvent.timestamp))
        }

        if (arrivalEvent != null && (departureEvent != null || deliveryEvent != null)) {
            val endTime = departureEvent?.timestamp ?: deliveryEvent?.timestamp ?: ""
            val duration = calculateDuration(arrivalEvent.timestamp, endTime)
            result.add("На месте" to duration)
        }

        if (departureEvent != null) {
            result.add("Отправление" to formatTimestamp(departureEvent.timestamp))
        }

        if (deliveryEvent != null) {
            result.add("Доставлено" to formatTimestamp(deliveryEvent.timestamp))
        }

        return result
    }

    fun getPhotoUrls(): List<Pair<String, String>> {
        val point = _uiState.value.point ?: return emptyList()
        val photos = mutableListOf<Pair<String, String>>()

        point.events.forEach { event ->
            if (!event.photoUrl.isNullOrBlank()) {
                val label = when (event.eventType) {
                    EventType.ARRIVAL -> "Фото прибытия"
                    EventType.DELIVERY_COMPLETE -> "Фото доставки"
                    EventType.DEPARTURE -> "Фото отправления"
                }
                photos.add(event.photoUrl to label)
            }
        }

        return photos
    }

    private fun formatTimestamp(timestamp: String): String {
        // Parse ISO timestamp and return HH:mm format
        return try {
            val inputFormat = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", java.util.Locale.getDefault())
            val date = inputFormat.parse(timestamp)
            val outputFormat = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault())
            date?.let { outputFormat.format(it) } ?: timestamp
        } catch (_: Exception) {
            timestamp
        }
    }

    private fun calculateDuration(start: String, end: String): String {
        return try {
            val format = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", java.util.Locale.getDefault())
            val startDate = format.parse(start)
            val endDate = format.parse(end)
            if (startDate != null && endDate != null) {
                val diffMs = endDate.time - startDate.time
                val minutes = (diffMs / 1000 / 60).toInt()
                if (minutes < 60) {
                    "$minutes мин"
                } else {
                    val hours = minutes / 60
                    val remainingMinutes = minutes % 60
                    "${hours}ч ${remainingMinutes}мин"
                }
            } else {
                "—"
            }
        } catch (_: Exception) {
            "—"
        }
    }
}
