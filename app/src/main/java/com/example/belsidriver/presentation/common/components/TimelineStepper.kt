package com.example.belsidriver.presentation.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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

data class TimelineEvent(
    val label: String,
    val time: String,
    val icon: @Composable (() -> Unit)? = null
)

@Composable
fun TimelineStepper(
    events: List<TimelineEvent>,
    modifier: Modifier = Modifier
) {
    val colors = BelsiTheme.colors

    Column(modifier = modifier) {
        events.forEachIndexed { index, event ->
            val isLast = index == events.lastIndex

            Row(
                modifier = Modifier.height(IntrinsicSize.Min)
            ) {
                // Left column: dot + connecting line
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Dot with optional icon
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(colors.primaryBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        if (event.icon != null) {
                            event.icon.invoke()
                        }
                    }

                    // Connecting line to next dot
                    if (!isLast) {
                        Box(
                            modifier = Modifier
                                .width(2.dp)
                                .fillMaxHeight()
                                .background(colors.grayLight)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Right column: label + time
                Column(
                    modifier = Modifier.padding(bottom = if (!isLast) 16.dp else 0.dp)
                ) {
                    Text(
                        text = event.label,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )

                    Text(
                        text = event.time,
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.grayPending
                    )
                }
            }
        }
    }
}
