package com.example.belsidriver.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.belsidriver.domain.model.User
import com.example.belsidriver.domain.model.UserRole
import com.example.belsidriver.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val user: User? = null,
    val isCheckingAuth: Boolean = true
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    init {
        checkExistingAuth()
    }

    private fun checkExistingAuth() {
        viewModelScope.launch {
            try {
                if (authRepository.isLoggedIn()) {
                    val user = authRepository.getCurrentUser()
                    _uiState.value = _uiState.value.copy(user = user, isCheckingAuth = false)
                } else {
                    _uiState.value = _uiState.value.copy(isCheckingAuth = false)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isCheckingAuth = false)
            }
        }
    }

    fun login(phone: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val user = authRepository.login(phone, password)
                _uiState.value = _uiState.value.copy(isLoading = false, user = user)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Ошибка авторизации"
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
