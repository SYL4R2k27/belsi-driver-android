package com.example.belsidriver.presentation.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.belsidriver.ui.theme.BelsiTheme

data class DriverUi(
    val id: String,
    val name: String,
    val isOnShift: Boolean,
    val currentRoute: String? = null,
    val lastEvent: String? = null,
    val lastEventTime: String? = null
)

@Composable
fun DriverCard(
    driver: DriverUi,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = BelsiTheme.colors

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick(driver.id) },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(containerColor = colors.cardBackground)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar with status dot
            Box(modifier = Modifier.size(48.dp)) {
                // Avatar circle
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(colors.primaryBlueLight),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = colors.primaryBlue,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Status dot at bottom-right
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .align(Alignment.BottomEnd)
                        .offset(x = (-4).dp, y = (-4).dp)
                        .border(width = 2.dp, color = Color.White, shape = CircleShape)
                        .clip(CircleShape)
                        .background(
                            if (driver.isOnShift) colors.successGreen
                            else colors.onlineOff
                        )
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                // Name
                Text(
                    text = driver.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                // Shift status text
                Text(
                    text = if (driver.isOnShift) "На смене" else "Не на смене",
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.grayPending
                )

                // Current route (only when on shift)
                if (driver.isOnShift && !driver.currentRoute.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = driver.currentRoute,
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.primaryBlue
                    )
                }

                // Last event
                if (!driver.lastEvent.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    val eventText = if (!driver.lastEventTime.isNullOrBlank()) {
                        "${driver.lastEvent} в ${driver.lastEventTime}"
                    } else {
                        driver.lastEvent
                    }
                    Text(
                        text = eventText,
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.grayPending
                    )
                }
            }
        }
    }
}
