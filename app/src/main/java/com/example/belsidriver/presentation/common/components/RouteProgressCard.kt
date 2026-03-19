package com.example.belsidriver.presentation.common.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.belsidriver.ui.theme.BelsiTheme

@Composable
fun RouteProgressCard(
    title: String,
    totalPoints: Int,
    completedPoints: Int,
    onOpenRoute: () -> Unit,
    onOpenMap: () -> Unit,
    totalDistance: String? = null,
    totalEstimatedTime: String? = null,
    totalActualTime: String? = null,
    modifier: Modifier = Modifier
) {
    val colors = BelsiTheme.colors

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.cardBackground
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Прогресс: $completedPoints из $totalPoints точек",
                style = MaterialTheme.typography.bodyMedium,
                color = colors.grayPending
            )

            if (totalDistance != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Расстояние: $totalDistance",
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.grayPending
                )
            }

            if (totalEstimatedTime != null || totalActualTime != null) {
                Spacer(modifier = Modifier.height(2.dp))
                val timeText = if (totalActualTime != null) {
                    "Время: $totalActualTime (факт)" +
                            if (totalEstimatedTime != null) " / ~$totalEstimatedTime (расч.)" else ""
                } else {
                    "Расч. время: ~$totalEstimatedTime"
                }
                Text(
                    text = timeText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.grayPending
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                BelsiButton(
                    text = "Открыть маршрут",
                    onClick = onOpenRoute,
                    variant = BelsiButtonVariant.Primary,
                    fullWidth = true,
                    modifier = Modifier.weight(1f)
                )

                BelsiButton(
                    onClick = onOpenMap,
                    variant = BelsiButtonVariant.Secondary,
                    startIcon = {
                        Icon(
                            imageVector = Icons.Default.PinDrop,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                ) {
                    Text("Карта")
                }
            }
        }
    }
}
