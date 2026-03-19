package com.example.belsidriver.presentation.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.belsidriver.ui.theme.BelsiTheme

enum class DeliveryStatus {
    Pending,
    Arrived,
    Delivered
}

@Composable
fun StatusBadge(
    status: DeliveryStatus,
    modifier: Modifier = Modifier
) {
    val colors = BelsiTheme.colors

    val backgroundColor: Color
    val textColor: Color
    val label: String
    val showCheckIcon: Boolean

    when (status) {
        DeliveryStatus.Pending -> {
            backgroundColor = colors.grayLight
            textColor = colors.grayPending
            label = "\u041E\u0436\u0438\u0434\u0430\u0435\u0442"
            showCheckIcon = false
        }
        DeliveryStatus.Arrived -> {
            backgroundColor = colors.warningAmberLight
            textColor = colors.warningAmberText
            label = "\u041D\u0430 \u043C\u0435\u0441\u0442\u0435"
            showCheckIcon = false
        }
        DeliveryStatus.Delivered -> {
            backgroundColor = colors.successGreenLight
            textColor = colors.successGreen
            label = "\u0414\u043E\u0441\u0442\u0430\u0432\u043B\u0435\u043D\u043E"
            showCheckIcon = true
        }
    }

    Row(
        modifier = modifier
            .height(32.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        if (showCheckIcon) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = textColor
            )
        }
        Text(
            text = label,
            color = textColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
