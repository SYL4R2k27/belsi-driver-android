package com.example.belsidriver.presentation.common.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.belsidriver.ui.theme.BelsiTheme

data class DeliveryPointUi(
    val id: String,
    val sequence: Int,
    val address: String,
    val contactName: String? = null,
    val phone: String? = null,
    val notes: String? = null,
    val status: DeliveryStatus,
    val distanceFromPrevious: String? = null,
    val estimatedTime: String? = null,
    val actualTravelTime: String? = null,
    val timeOnPoint: String? = null
)

@Composable
fun DeliveryPointCard(
    point: DeliveryPointUi,
    onMarkArrived: ((String) -> Unit)? = null,
    onMarkDelivered: ((String) -> Unit)? = null,
    readOnly: Boolean = false,
    modifier: Modifier = Modifier
) {
    val colors = BelsiTheme.colors
    val uriHandler = LocalUriHandler.current

    val cardBackground = when (point.status) {
        DeliveryStatus.Pending -> colors.cardBackground
        DeliveryStatus.Arrived -> colors.warningAmberLight
        DeliveryStatus.Delivered -> colors.successGreenLight
    }

    Column(modifier = modifier) {
        // Distance/time segment between points
        if (point.distanceFromPrevious != null || point.estimatedTime != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 36.dp, bottom = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "\u2193 ",
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.grayPending
                )
                if (point.distanceFromPrevious != null) {
                    Text(
                        text = point.distanceFromPrevious,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium,
                        color = colors.grayPending
                    )
                }
                if (point.distanceFromPrevious != null && point.estimatedTime != null) {
                    Text(
                        text = " \u00B7 ",
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.grayPending
                    )
                }
                if (point.estimatedTime != null) {
                    Text(
                        text = "~${point.estimatedTime}",
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.grayPending
                    )
                }
                if (point.actualTravelTime != null) {
                    Text(
                        text = " (факт: ${point.actualTravelTime})",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium,
                        color = colors.primaryBlue
                    )
                }
            }
        }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(containerColor = cardBackground)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            CircularNumberBadge(
                number = point.sequence,
                isCompleted = point.status == DeliveryStatus.Delivered
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                // Address
                Text(
                    text = point.address,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                // Contact name
                if (!point.contactName.isNullOrBlank()) {
                    Text(
                        text = point.contactName,
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.grayPending
                    )
                }

                // Phone (clickable)
                if (!point.phone.isNullOrBlank()) {
                    Text(
                        text = point.phone,
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.primaryBlue,
                        modifier = Modifier.clickable {
                            uriHandler.openUri("tel:${point.phone}")
                        }
                    )
                }

                // Notes
                if (!point.notes.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = point.notes,
                        style = MaterialTheme.typography.bodyMedium,
                        fontStyle = FontStyle.Italic,
                        color = colors.grayPending
                    )
                }

                // Time on point
                if (point.timeOnPoint != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = colors.grayPending
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "На точке: ${point.timeOnPoint}",
                            style = MaterialTheme.typography.bodySmall,
                            color = colors.grayPending
                        )
                    }
                }

                // Bottom row: StatusBadge + action button
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    StatusBadge(status = point.status)

                    if (!readOnly) {
                        when (point.status) {
                            DeliveryStatus.Pending -> {
                                if (onMarkArrived != null) {
                                    BelsiButton(
                                        text = "Прибыл / На месте",
                                        onClick = { onMarkArrived(point.id) },
                                        variant = BelsiButtonVariant.Primary,
                                        size = BelsiButtonSize.Small
                                    )
                                }
                            }
                            DeliveryStatus.Arrived -> {
                                if (onMarkDelivered != null) {
                                    BelsiButton(
                                        text = "Доставлено",
                                        onClick = { onMarkDelivered(point.id) },
                                        variant = BelsiButtonVariant.Primary,
                                        size = BelsiButtonSize.Small
                                    )
                                }
                            }
                            DeliveryStatus.Delivered -> {
                                // No button
                            }
                        }
                    }
                }
            }
        }
    }
    } // Column wrapper
}
