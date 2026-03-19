package com.example.belsidriver.presentation.common.components

import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.belsidriver.ui.theme.BelsiTheme
import kotlinx.coroutines.delay
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun ShiftInfoCard(
    startTime: String,
    startPhotoUrl: String? = null,
    onPhotoClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val colors = BelsiTheme.colors

    val startInstant = remember(startTime) {
        try {
            Instant.parse(startTime)
        } catch (_: Exception) {
            Instant.now()
        }
    }

    val formattedStartTime = remember(startInstant) {
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
            .withZone(ZoneId.systemDefault())
        formatter.format(startInstant)
    }

    var elapsedMinutes by remember { mutableLongStateOf(0L) }

    LaunchedEffect(startInstant) {
        while (true) {
            val now = Instant.now()
            val diffMillis = now.toEpochMilli() - startInstant.toEpochMilli()
            elapsedMinutes = if (diffMillis > 0) diffMillis / 60_000 else 0L
            delay(60_000L)
        }
    }

    val hours = elapsedMinutes / 60
    val minutes = elapsedMinutes % 60

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.successGreenLight
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Смена активна",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = colors.successGreen
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.Schedule,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = colors.grayPending
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Начало: $formattedStartTime",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.grayPending
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "Длительность: ${hours}ч ${minutes}м",
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.grayPending
                )
            }

            if (startPhotoUrl != null) {
                Spacer(modifier = Modifier.width(12.dp))
                AsyncImage(
                    model = startPhotoUrl,
                    contentDescription = "Фото начала смены",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .then(
                            if (onPhotoClick != null) {
                                Modifier.clickable(onClick = onPhotoClick)
                            } else {
                                Modifier
                            }
                        )
                )
            }
        }
    }
}
