package com.example.belsidriver.presentation.common.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.belsidriver.ui.theme.BelsiTheme

enum class BelsiButtonVariant {
    Primary,
    Secondary,
    Outlined,
    Danger
}

enum class BelsiButtonSize {
    Small,
    Medium,
    Large
}

@Composable
fun BelsiButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: BelsiButtonVariant = BelsiButtonVariant.Primary,
    size: BelsiButtonSize = BelsiButtonSize.Medium,
    fullWidth: Boolean = false,
    enabled: Boolean = true,
    startIcon: (@Composable () -> Unit)? = null,
    endIcon: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val colors = BelsiTheme.colors

    val containerColor = when {
        !enabled -> Color(0xFFE0E0E0)
        isPressed -> when (variant) {
            BelsiButtonVariant.Primary -> Color(0xFF1557B0)
            BelsiButtonVariant.Secondary -> Color(0xFFD2E3FC)
            BelsiButtonVariant.Outlined -> colors.primaryBlueLight
            BelsiButtonVariant.Danger -> Color(0xFFD33B2E)
        }
        else -> when (variant) {
            BelsiButtonVariant.Primary -> colors.primaryBlue
            BelsiButtonVariant.Secondary -> colors.primaryBlueLight
            BelsiButtonVariant.Outlined -> Color.Transparent
            BelsiButtonVariant.Danger -> colors.errorRed
        }
    }

    val contentColor = when {
        !enabled -> Color(0xFF9E9E9E)
        else -> when (variant) {
            BelsiButtonVariant.Primary -> Color.White
            BelsiButtonVariant.Secondary -> colors.primaryBlue
            BelsiButtonVariant.Outlined -> colors.primaryBlue
            BelsiButtonVariant.Danger -> Color.White
        }
    }

    val border = when {
        !enabled -> null
        variant == BelsiButtonVariant.Outlined -> BorderStroke(1.dp, colors.primaryBlue)
        else -> null
    }

    val elevation = when {
        !enabled -> ButtonDefaults.buttonElevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
            disabledElevation = 0.dp
        )
        variant == BelsiButtonVariant.Outlined -> ButtonDefaults.buttonElevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp
        )
        else -> ButtonDefaults.buttonElevation(
            defaultElevation = 1.dp,
            pressedElevation = 2.dp,
            disabledElevation = 0.dp
        )
    }

    val buttonHeight = when (size) {
        BelsiButtonSize.Small -> 36.dp
        BelsiButtonSize.Medium -> 48.dp
        BelsiButtonSize.Large -> 56.dp
    }

    val horizontalPadding = when (size) {
        BelsiButtonSize.Small -> 16.dp
        BelsiButtonSize.Medium -> 24.dp
        BelsiButtonSize.Large -> 32.dp
    }

    Button(
        onClick = onClick,
        modifier = modifier
            .then(if (fullWidth) Modifier.fillMaxWidth() else Modifier)
            .defaultMinSize(minHeight = buttonHeight)
            .height(buttonHeight),
        enabled = enabled,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = Color(0xFFE0E0E0),
            disabledContentColor = Color(0xFF9E9E9E)
        ),
        elevation = elevation,
        border = border,
        contentPadding = PaddingValues(horizontal = horizontalPadding, vertical = 0.dp),
        interactionSource = interactionSource
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (startIcon != null) {
                startIcon()
                Spacer(modifier = Modifier.width(8.dp))
            }
            content()
            if (endIcon != null) {
                Spacer(modifier = Modifier.width(8.dp))
                endIcon()
            }
        }
    }
}

@Composable
fun BelsiButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: BelsiButtonVariant = BelsiButtonVariant.Primary,
    size: BelsiButtonSize = BelsiButtonSize.Medium,
    fullWidth: Boolean = false,
    enabled: Boolean = true,
    startIcon: (@Composable () -> Unit)? = null,
    endIcon: (@Composable () -> Unit)? = null
) {
    BelsiButton(
        onClick = onClick,
        modifier = modifier,
        variant = variant,
        size = size,
        fullWidth = fullWidth,
        enabled = enabled,
        startIcon = startIcon,
        endIcon = endIcon
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
