package com.example.belsidriver.presentation.driver.route

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.belsidriver.presentation.common.components.DeliveryPointCard
import com.example.belsidriver.presentation.common.components.DeliveryPointUi
import com.example.belsidriver.presentation.common.components.DeliveryStatus
import com.example.belsidriver.presentation.common.components.RouteProgressCard
import com.example.belsidriver.util.DistanceCalculator
import com.example.belsidriver.util.IntentUtils
import com.example.belsidriver.util.TimeCalculator

@Composable
fun ActiveRouteScreen(
    onBack: () -> Unit,
    onPointArrival: (routeId: String, pointId: String) -> Unit,
    onPointDelivery: (routeId: String, pointId: String) -> Unit,
    onPointDetail: (routeId: String, pointId: String) -> Unit,
    viewModel: ActiveRouteViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            BelsiAppBar(
                title = uiState.route?.title ?: "Маршрут",
                onNavigateBack = onBack,
                actions = {
                    val url = uiState.route?.yandexMapsUrl
                    if (!url.isNullOrBlank()) {
                        IconButton(onClick = { IntentUtils.openYandexMaps(context, url) }) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = "Яндекс.Карты",
                                tint = Color.White
                            )
                        }
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

        uiState.error?.let { errorMessage ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(text = errorMessage)
            }
            return@Scaffold
        }

        val sortedPoints = uiState.points

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 4.dp)
        ) {
            // Route summary header
            if (uiState.totalDistance != null || uiState.totalEstimatedTime != null) {
                item(key = "route-summary") {
                    RouteProgressCard(
                        title = uiState.route?.title ?: "Маршрут",
                        totalPoints = sortedPoints.size,
                        completedPoints = sortedPoints.count { it.status == PointStatus.DELIVERED },
                        onOpenRoute = {},
                        onOpenMap = {
                            uiState.route?.yandexMapsUrl?.let {
                                IntentUtils.openYandexMaps(context, it)
                            }
                        },
                        totalDistance = uiState.totalDistance,
                        totalEstimatedTime = uiState.totalEstimatedTime,
                        totalActualTime = uiState.totalActualTime
                    )
                }
            }

            items(sortedPoints, key = { it.id }) { point ->
                val routeId = uiState.route?.id ?: ""
                val pointIndex = sortedPoints.indexOf(point)

                // Calculate actual travel time from previous point
                val actualTravel = if (pointIndex > 0) {
                    TimeCalculator.actualTravelTime(sortedPoints[pointIndex - 1], point)
                        ?.let { DistanceCalculator.formatDuration(it) }
                } else null

                // Calculate time on point
                val onPointTime = TimeCalculator.timeOnPoint(point)
                    ?.let { DistanceCalculator.formatDuration(it) }

                val deliveryPointUi = DeliveryPointUi(
                    id = point.id,
                    sequence = point.sequenceNumber,
                    address = point.address,
                    contactName = point.contactName,
                    phone = point.contactPhone,
                    notes = point.notes,
                    status = when (point.status) {
                        PointStatus.PENDING -> DeliveryStatus.Pending
                        PointStatus.ARRIVED -> DeliveryStatus.Arrived
                        PointStatus.DELIVERED -> DeliveryStatus.Delivered
                    },
                    distanceFromPrevious = point.distanceFromPreviousMeters
                        ?.let { DistanceCalculator.formatDistance(it) },
                    estimatedTime = point.estimatedDurationMinutes
                        ?.let { DistanceCalculator.formatDuration(it) },
                    actualTravelTime = actualTravel,
                    timeOnPoint = onPointTime
                )

                DeliveryPointCard(
                    point = deliveryPointUi,
                    onMarkArrived = { pointId ->
                        onPointArrival(routeId, pointId)
                    },
                    onMarkDelivered = { pointId ->
                        onPointDelivery(routeId, pointId)
                    }
                )
            }
        }
    }
}
