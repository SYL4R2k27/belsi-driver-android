package com.example.belsidriver.presentation.driver.shift

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.belsidriver.presentation.driver.camera.CameraScreen

@Composable
fun ShiftCameraScreen(
    type: String, // "start" or "end"
    shiftId: String? = null,
    onSuccess: () -> Unit,
    onCancel: () -> Unit,
    viewModel: ShiftViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.success) {
        if (uiState.success) {
            onSuccess()
        }
    }

    val title = if (type == "start") "Фото начала смены" else "Фото окончания смены"

    CameraScreen(
        title = title,
        isLoading = uiState.isLoading,
        onPhotoTaken = { file ->
            if (type == "start") {
                viewModel.startShift(file)
            } else {
                shiftId?.let { viewModel.endShift(it, file) }
            }
        },
        onCancel = onCancel
    )
}
