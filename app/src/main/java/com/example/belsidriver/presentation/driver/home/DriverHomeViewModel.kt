package com.example.belsidriver.presentation.driver.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.belsidriver.domain.model.Route
import com.example.belsidriver.domain.model.Shift
import com.example.belsidriver.domain.repository.AuthRepository
import com.example.belsidriver.domain.repository.RouteRepository
import com.example.belsidriver.domain.repository.ShiftRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DriverHomeUiState(
    val isLoading: Boolean = true,
    val currentShift: Shift? = null,
    val activeRoute: Route? = null,
    val error: String? = null
)

@HiltViewModel
class DriverHomeViewModel @Inject constructor(
    private val shiftRepository: ShiftRepository,
    private val routeRepository: RouteRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DriverHomeUiState())
    val uiState: StateFlow<DriverHomeUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val shift = shiftRepository.getCurrentShift()
                val routes = routeRepository.getRoutes(status = "IN_PROGRESS")
                val activeRoute = routes.firstOrNull()

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    currentShift = shift,
                    activeRoute = activeRoute
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Ошибка загрузки данных"
                )
            }
        }
    }

    fun logout(onLoggedOut: () -> Unit) {
        viewModelScope.launch {
            try {
                authRepository.logout()
                onLoggedOut()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = "Ошибка выхода")
            }
        }
    }
}
