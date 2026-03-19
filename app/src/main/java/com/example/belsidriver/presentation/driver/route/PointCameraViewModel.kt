package com.example.belsidriver.presentation.driver.route

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.belsidriver.domain.repository.DeliveryPointRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

data class PointCameraUiState(
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class PointCameraViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val deliveryPointRepository: DeliveryPointRepository
) : ViewModel() {

    val routeId: String = savedStateHandle["routeId"] ?: ""
    val pointId: String = savedStateHandle["pointId"] ?: ""
    val action: String = savedStateHandle["action"] ?: "arrive"

    private val _uiState = MutableStateFlow(PointCameraUiState())
    val uiState: StateFlow<PointCameraUiState> = _uiState.asStateFlow()

    fun submitPhoto(photo: File) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                when (action) {
                    "arrive" -> deliveryPointRepository.arriveAtPoint(routeId, pointId, photo)
                    "deliver" -> deliveryPointRepository.deliverAtPoint(routeId, pointId, photo)
                }
                _uiState.value = _uiState.value.copy(isLoading = false, success = true)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Ошибка"
                )
            }
        }
    }
}
