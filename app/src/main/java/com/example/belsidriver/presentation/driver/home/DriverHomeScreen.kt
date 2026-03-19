package com.example.belsidriver.presentation.driver.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.StopCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.belsidriver.domain.model.PointStatus
import com.example.belsidriver.presentation.common.components.BelsiAppBar
import com.example.belsidriver.presentation.common.components.BelsiButton
import com.example.belsidriver.presentation.common.components.BelsiButtonSize
import com.example.belsidriver.presentation.common.components.BelsiButtonVariant
import com.example.belsidriver.presentation.common.components.RouteProgressCard
import com.example.belsidriver.presentation.common.components.ShiftInfoCard
import com.example.belsidriver.ui.theme.BelsiTheme
import com.example.belsidriver.util.IntentUtils

@Composable
fun DriverHomeScreen(
    onStartShift: () -> Unit,
    onEndShift: (shiftId: String) -> Unit,
    onOpenRoute: (routeId: String) -> Unit,
    onLogout: () -> Unit,
    viewModel: DriverHomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            BelsiAppBar(
                title = "BELSI.Driver",
                actions = {
                    IconButton(onClick = { viewModel.logout(onLogout) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = "Выйти",
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) { padding ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        if (uiState.currentShift == null) {
            // No active shift: center the start button
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BelsiButton(
                    text = "Начать смену",
                    onClick = onStartShift,
                    variant = BelsiButtonVariant.Primary,
                    size = BelsiButtonSize.Large,
                    startIcon = {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                )
            }
        } else {
            // Active shift
            val shift = uiState.currentShift!!
            val route = uiState.activeRoute

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Shift info card
                ShiftInfoCard(
                    startTime = shift.startedAt,
                    startPhotoUrl = shift.startPhotoUrl
                )

                if (route != null) {
                    // Route progress card
                    val totalPoints = route.deliveryPoints.size
                    val completedPoints =
                        route.deliveryPoints.count { it.status == PointStatus.DELIVERED }

                    RouteProgressCard(
                        title = route.title ?: "Маршрут",
                        totalPoints = totalPoints,
                        completedPoints = completedPoints,
                        onOpenRoute = { onOpenRoute(route.id) },
                        onOpenMap = {
                            if (!route.yandexMapsUrl.isNullOrBlank()) {
                                IntentUtils.openYandexMaps(context, route.yandexMapsUrl)
                            }
                        }
                    )
                } else {
                    // No route assigned
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Text(
                            text = "Маршрут не назначен",
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyLarge,
                            color = BelsiTheme.colors.grayPending
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // End shift button
                BelsiButton(
                    text = "Завершить смену",
                    onClick = { onEndShift(shift.id) },
                    variant = BelsiButtonVariant.Danger,
                    size = BelsiButtonSize.Large,
                    fullWidth = true,
                    startIcon = {
                        Icon(
                            imageVector = Icons.Default.StopCircle,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                )
            }
        }
    }
}
