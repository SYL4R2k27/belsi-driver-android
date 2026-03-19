package com.example.belsidriver.presentation.driver.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.belsidriver.presentation.driver.camera.CameraScreen

@Composable
fun PointCameraScreen(
    onSuccess: () -> Unit,
    onCancel: () -> Unit,
    viewModel: PointCameraViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.success) {
        if (uiState.success) {
            onSuccess()
        }
    }

    val title = when (viewModel.action) {
        "arrive" -> "Фото прибытия"
        "deliver" -> "Фото доставки"
        else -> "Фото"
    }

    CameraScreen(
        title = title,
        isLoading = uiState.isLoading,
        onPhotoTaken = { file -> viewModel.submitPhoto(file) },
        onCancel = onCancel
    )
}
