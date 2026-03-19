package com.example.belsidriver.presentation.common.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.belsidriver.ui.theme.BelsiTheme

enum class BelsiTextFieldType {
    Text,
    Password,
    Phone,
    Email
}

@Composable
fun BelsiTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    type: BelsiTextFieldType = BelsiTextFieldType.Text,
    fullWidth: Boolean = true,
    disabled: Boolean = false,
    multiline: Boolean = false,
    rows: Int = 1,
    required: Boolean = false
) {
    val colors = BelsiTheme.colors
    var passwordVisible by remember { mutableStateOf(false) }

    val keyboardType = when (type) {
        BelsiTextFieldType.Text -> KeyboardType.Text
        BelsiTextFieldType.Password -> KeyboardType.Password
        BelsiTextFieldType.Phone -> KeyboardType.Phone
        BelsiTextFieldType.Email -> KeyboardType.Email
    }

    val visualTransformation = when {
        type == BelsiTextFieldType.Password && !passwordVisible -> PasswordVisualTransformation()
        else -> VisualTransformation.None
    }

    val trailingIcon: (@Composable () -> Unit)? = when (type) {
        BelsiTextFieldType.Password -> {
            {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) {
                            Icons.Default.VisibilityOff
                        } else {
                            Icons.Default.Visibility
                        },
                        contentDescription = if (passwordVisible) {
                            "Hide password"
                        } else {
                            "Show password"
                        },
                        tint = colors.grayPending
                    )
                }
            }
        }
        else -> null
    }

    val displayLabel = if (required && label != null) "$label *" else label

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.then(if (fullWidth) Modifier.fillMaxWidth() else Modifier),
        enabled = !disabled,
        label = if (displayLabel != null) {
            {
                Text(
                    text = displayLabel,
                    fontWeight = FontWeight.Medium
                )
            }
        } else null,
        placeholder = if (placeholder != null) {
            {
                Text(
                    text = placeholder,
                    color = colors.grayPending
                )
            }
        } else null,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        visualTransformation = visualTransformation,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = !multiline,
        minLines = if (multiline) rows else 1,
        maxLines = if (multiline) rows else 1,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            // Unfocused state
            unfocusedContainerColor = colors.grayLight,
            unfocusedBorderColor = Color.Transparent,
            unfocusedLabelColor = colors.grayPending,
            unfocusedPlaceholderColor = colors.grayPending,
            unfocusedTextColor = Color.Unspecified,
            // Focused state
            focusedContainerColor = colors.grayLight,
            focusedBorderColor = colors.primaryBlue,
            focusedLabelColor = colors.primaryBlue,
            focusedPlaceholderColor = colors.grayPending,
            focusedTextColor = Color.Unspecified,
            // Disabled state
            disabledContainerColor = colors.disabledBg,
            disabledBorderColor = Color.Transparent,
            disabledLabelColor = colors.disabledText,
            disabledPlaceholderColor = colors.disabledText,
            disabledTextColor = colors.disabledText,
            // Cursor
            cursorColor = colors.primaryBlue
        )
    )
}
