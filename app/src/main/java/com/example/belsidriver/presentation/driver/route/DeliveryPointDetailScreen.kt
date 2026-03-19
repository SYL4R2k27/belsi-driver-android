package com.example.belsidriver.presentation.driver.route

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.belsidriver.presentation.common.components.BelsiAppBar
import com.example.belsidriver.presentation.common.components.PhotoThumbnail
import com.example.belsidriver.presentation.common.components.TimelineEvent
import com.example.belsidriver.presentation.common.components.TimelineStepper
import com.example.belsidriver.ui.theme.BelsiTheme

@Composable
fun DeliveryPointDetailScreen(
    onBack: () -> Unit,
    onPhotoClick: (photoUrl: String) -> Unit,
    viewModel: DeliveryPointDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val colors = BelsiTheme.colors

    Scaffold(
        topBar = {
            BelsiAppBar(
                title = uiState.point?.address ?: "Точка доставки",
                onNavigateBack = onBack
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

        val point = uiState.point ?: return@Scaffold

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 16.dp)
        ) {
            // Card 1: Delivery details
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    colors = CardDefaults.cardColors(containerColor = colors.cardBackground)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Детали доставки",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = point.address,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        if (!point.contactName.isNullOrBlank()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Контакт: ${point.contactName}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        if (!point.contactPhone.isNullOrBlank()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = point.contactPhone,
                                style = MaterialTheme.typography.bodyMedium,
                                color = colors.primaryBlue
                            )
                        }

                        if (!point.notes.isNullOrBlank()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = point.notes,
                                style = MaterialTheme.typography.bodyMedium,
                                color = colors.grayPending
                            )
                        }
                    }
                }
            }

            // Card 2: Timeline
            item {
                val timelineData = viewModel.getTimelineEvents()

                if (timelineData.isNotEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        colors = CardDefaults.cardColors(containerColor = colors.cardBackground)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Хронология",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            val timelineEvents = timelineData.mapIndexed { index, (label, time) ->
                                TimelineEvent(
                                    label = label,
                                    time = time,
                                    icon = {
                                        val icon = when (label) {
                                            "Прибытие" -> Icons.Default.LocationOn
                                            "На месте" -> Icons.Default.AccessTime
                                            "Отправление" -> Icons.Default.DirectionsCar
                                            "Доставлено" -> Icons.Default.Flag
                                            else -> Icons.Default.AccessTime
                                        }
                                        Icon(
                                            imageVector = icon,
                                            contentDescription = label,
                                            tint = Color.White,
                                            modifier = Modifier
                                        )
                                    }
                                )
                            }

                            TimelineStepper(events = timelineEvents)
                        }
                    }
                }
            }

            // Card 3: Photos
            item {
                val photos = viewModel.getPhotoUrls()

                if (photos.isNotEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        colors = CardDefaults.cardColors(containerColor = colors.cardBackground)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Фотографии",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                photos.forEach { (url, description) ->
                                    PhotoThumbnail(
                                        imageUrl = url,
                                        contentDescription = description,
                                        onClick = { onPhotoClick(url) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
