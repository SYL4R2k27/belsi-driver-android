package com.example.belsidriver.presentation.logistician.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.belsidriver.data.remote.dto.DriverSummaryDto
import com.example.belsidriver.domain.repository.AuthRepository
import com.example.belsidriver.domain.repository.DriverRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LogisticianHomeUiState(
    val isLoading: Boolean = true,
    val drivers: List<DriverSummaryDto> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class LogisticianHomeViewModel @Inject constructor(
    private val driverRepository: DriverRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LogisticianHomeUiState())
    val uiState: StateFlow<LogisticianHomeUiState> = _uiState.asStateFlow()

    init {
        loadDrivers()
    }

    fun loadDrivers() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val drivers = driverRepository.getDrivers()
                _uiState.value = _uiState.value.copy(isLoading = false, drivers = drivers)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Ошибка загрузки"
                )
            }
        }
    }

    fun logout(onLoggedOut: () -> Unit) {
        viewModelScope.launch {
            try {
                authRepository.logout()
                onLoggedOut()
            } catch (_: Exception) {}
        }
    }
}
