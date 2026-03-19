package com.example.belsidriver.presentation.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.belsidriver.ui.theme.BelsiTheme

@Composable
fun CircularNumberBadge(
    number: Int,
    isCompleted: Boolean = false,
    modifier: Modifier = Modifier
) {
    val colors = BelsiTheme.colors

    Box(
        modifier = modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(
                if (isCompleted) colors.successGreenLight
                else colors.primaryBlueLight
            ),
        contentAlignment = Alignment.Center
    ) {
        if (isCompleted) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = colors.successGreen,
                modifier = Modifier.size(20.dp)
            )
        } else {
            Text(
                text = number.toString(),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = colors.primaryBlue
            )
        }
    }
}
