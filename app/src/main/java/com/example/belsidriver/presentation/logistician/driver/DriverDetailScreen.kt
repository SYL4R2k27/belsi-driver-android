package com.example.belsidriver.presentation.logistician.driver

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.belsidriver.domain.model.PointStatus
import com.example.belsidriver.domain.model.Route
import com.example.belsidriver.domain.model.RouteStatus
import com.example.belsidriver.domain.model.Shift
import com.example.belsidriver.domain.model.ShiftStatus
import com.example.belsidriver.presentation.common.components.BelsiAppBar
import com.example.belsidriver.presentation.common.components.ShiftInfoCard
import com.example.belsidriver.ui.theme.BelsiTheme
import java.time.Duration
import java.time.Instant

@Composable
fun DriverDetailScreen(
    onBack: () -> Unit,
    onRouteClick: (routeId: String) -> Unit,
    onCreateRoute: (driverId: String) -> Unit,
    viewModel: DriverDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val colors = BelsiTheme.colors

    Scaffold(
        topBar = {
            BelsiAppBar(
                title = uiState.driver?.fullName ?: "\u0412\u043E\u0434\u0438\u0442\u0435\u043B\u044C",
                onNavigateBack = onBack
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onCreateRoute(viewModel.driverId) },
                containerColor = colors.primaryBlue,
                contentColor = Color.White,
                modifier = Modifier.padding(bottom = 16.dp, end = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "\u0421\u043E\u0437\u0434\u0430\u0442\u044C \u043C\u0430\u0440\u0448\u0440\u0443\u0442"
                )
            }
        }
    ) { padding ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = colors.primaryBlue)
            }
            return@Scaffold
        }

        uiState.error?.let { errorMessage ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = errorMessage,
                    style = MaterialTheme.typography.bodyLarge,
                    color = colors.errorRed
                )
            }
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Section: Current Shift
            item {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "\u0422\u0435\u043A\u0443\u0449\u0430\u044F \u0441\u043C\u0435\u043D\u0430",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            item {
                val currentShift = uiState.driver?.currentShift
                if (currentShift != null) {
                    ShiftInfoCard(
                        startTime = currentShift.startedAt,
                        startPhotoUrl = currentShift.startPhotoUrl
                    )
                } else {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        colors = CardDefaults.cardColors(containerColor = colors.cardBackground)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "\u041D\u0435 \u043D\u0430 \u0441\u043C\u0435\u043D\u0435",
                                style = MaterialTheme.typography.bodyLarge,
                                color = colors.grayPending
                            )
                        }
                    }
                }
            }

            // Section: Routes
            item {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "\u041C\u0430\u0440\u0448\u0440\u0443\u0442\u044B",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            items(uiState.routes, key = { it.id }) { route ->
                RouteCard(
                    route = route,
                    onClick = { onRouteClick(route.id) }
                )
            }

            // Section: Shift History
            item {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "\u0418\u0441\u0442\u043E\u0440\u0438\u044F \u0441\u043C\u0435\u043D",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            items(uiState.shifts, key = { it.id }) { shift ->
                ShiftHistoryCard(shift = shift)
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
private fun RouteCard(
    route: Route,
    onClick: () -> Unit
) {
    val colors = BelsiTheme.colors

    val statusText = when (route.status) {
        RouteStatus.PENDING -> "\u041E\u0436\u0438\u0434\u0430\u0435\u0442"
        RouteStatus.IN_PROGRESS -> "\u0412 \u043F\u0440\u043E\u0446\u0435\u0441\u0441\u0435"
        RouteStatus.COMPLETED -> "\u0417\u0430\u0432\u0435\u0440\u0448\u0451\u043D"
        RouteStatus.CANCELLED -> "\u041E\u0442\u043C\u0435\u043D\u0451\u043D"
    }

    val chipBgColor = when (route.status) {
        RouteStatus.IN_PROGRESS -> colors.warningAmber.copy(alpha = 0.2f)
        RouteStatus.COMPLETED -> colors.successGreen.copy(alpha = 0.2f)
        else -> colors.grayPending.copy(alpha = 0.2f)
    }

    val chipTextColor = when (route.status) {
        RouteStatus.IN_PROGRESS -> colors.warningAmberText
        RouteStatus.COMPLETED -> colors.successGreenDark
        else -> colors.grayPending
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(containerColor = colors.cardBackground)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = route.title ?: "\u041C\u0430\u0440\u0448\u0440\u0443\u0442",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(chipBgColor)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = statusText,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Medium,
                        color = chipTextColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            val pointsCount = route.deliveryPoints.size
            Text(
                text = "${route.plannedDate} \u2022 $pointsCount \u0442\u043E\u0447\u0435\u043A",
                style = MaterialTheme.typography.bodyMedium,
                color = colors.grayPending
            )
        }
    }
}

@Composable
private fun ShiftHistoryCard(shift: Shift) {
    val colors = BelsiTheme.colors

    val formattedDate = try {
        val instant = Instant.parse(shift.startedAt)
        val formatter = java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
            .withZone(java.time.ZoneId.systemDefault())
        formatter.format(instant)
    } catch (_: Exception) {
        shift.startedAt
    }

    val duration = try {
        val start = Instant.parse(shift.startedAt)
        val end = if (shift.endedAt != null) Instant.parse(shift.endedAt) else Instant.now()
        val dur = Duration.between(start, end)
        val hours = dur.toHours()
        val minutes = dur.toMinutes() % 60
        "${hours}\u0447 ${minutes}\u043C"
    } catch (_: Exception) {
        null
    }

    val statusText = when (shift.status) {
        ShiftStatus.ACTIVE -> "\u0410\u043A\u0442\u0438\u0432\u043D\u0430"
        ShiftStatus.COMPLETED -> "\u0417\u0430\u0432\u0435\u0440\u0448\u0435\u043D\u0430"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(containerColor = colors.cardBackground)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = formattedDate,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                if (duration != null) {
                    Text(
                        text = "\u0414\u043B\u0438\u0442\u0435\u043B\u044C\u043D\u043E\u0441\u0442\u044C: $duration",
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.grayPending
                    )
                }
            }
            Text(
                text = statusText,
                style = MaterialTheme.typography.labelMedium,
                color = if (shift.status == ShiftStatus.ACTIVE) colors.successGreen else colors.grayPending
            )
        }
    }
}
