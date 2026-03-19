package com.example.belsidriver.presentation.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.belsidriver.domain.model.UserRole
import com.example.belsidriver.presentation.common.components.BelsiButton
import com.example.belsidriver.presentation.common.components.BelsiButtonSize
import com.example.belsidriver.presentation.common.components.BelsiButtonVariant
import com.example.belsidriver.presentation.common.components.BelsiTextField
import com.example.belsidriver.presentation.common.components.BelsiTextFieldType
import com.example.belsidriver.ui.theme.BelsiTheme

@Composable
fun LoginScreen(
    onLoginSuccess: (UserRole) -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    var phone by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    // Navigate on successful login
    LaunchedEffect(uiState.user) {
        uiState.user?.let { user ->
            onLoginSuccess(user.role)
        }
    }

    if (uiState.isCheckingAuth) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Title
            Text(
                text = "BELSI.Driver",
                color = BelsiTheme.colors.primaryBlue,
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Subtitle
            Text(
                text = "Система управления доставками",
                style = MaterialTheme.typography.bodyMedium,
                color = BelsiTheme.colors.grayPending,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Phone field
            BelsiTextField(
                value = phone,
                onValueChange = { phone = it },
                label = "Телефон",
                placeholder = "+7 (___) ___-__-__",
                type = BelsiTextFieldType.Phone,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = null,
                        tint = BelsiTheme.colors.grayPending
                    )
                },
                fullWidth = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password field
            BelsiTextField(
                value = password,
                onValueChange = { password = it },
                label = "Пароль",
                placeholder = "Введите пароль",
                type = BelsiTextFieldType.Password,
                fullWidth = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Login button
            BelsiButton(
                text = "Войти",
                onClick = { viewModel.login(phone, password) },
                variant = BelsiButtonVariant.Primary,
                size = BelsiButtonSize.Large,
                fullWidth = true,
                enabled = phone.isNotBlank() && password.isNotBlank() && !uiState.isLoading
            )

            // "Войти как логист" link
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Войти как логист",
                style = MaterialTheme.typography.bodyMedium,
                color = BelsiTheme.colors.primaryBlue,
                textAlign = TextAlign.Center,
                modifier = Modifier.clickable { /* placeholder for logistician login */ }
            )

            // Error text
            if (uiState.error != null) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = uiState.error!!,
                    style = MaterialTheme.typography.bodyMedium,
                    color = BelsiTheme.colors.errorRed,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
