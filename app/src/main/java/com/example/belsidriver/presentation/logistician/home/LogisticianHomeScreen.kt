package com.example.belsidriver.presentation.logistician.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.belsidriver.presentation.common.components.BelsiAppBar
import com.example.belsidriver.presentation.common.components.DriverCard
import com.example.belsidriver.presentation.common.components.DriverUi
import com.example.belsidriver.ui.theme.BelsiTheme

@Composable
fun LogisticianHomeScreen(
    onDriverClick: (driverId: String) -> Unit,
    onLogout: () -> Unit,
    viewModel: LogisticianHomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val colors = BelsiTheme.colors

    Scaffold(
        topBar = {
            BelsiAppBar(
                title = "BELSI.Driver \u2014 \u041B\u043E\u0433\u0438\u0441\u0442",
                actions = {
                    IconButton(onClick = { viewModel.logout(onLogout) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "\u0412\u044B\u0439\u0442\u0438",
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) { padding ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = colors.primaryBlue)
            }
            return@Scaffold
        }

        uiState.error?.let { errorMessage ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = errorMessage,
                    style = MaterialTheme.typography.bodyLarge,
                    color = colors.errorRed
                )
            }
            return@Scaffold
        }

        val drivers = uiState.drivers
        val onShiftCount = drivers.count { it.currentShift != null }
        val totalCount = drivers.size

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "\u0421\u043F\u0438\u0441\u043E\u043A \u0432\u043E\u0434\u0438\u0442\u0435\u043B\u0435\u0439",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "\u041D\u0430 \u0441\u043C\u0435\u043D\u0435: $onShiftCount \u0438\u0437 $totalCount",
                style = MaterialTheme.typography.bodyMedium,
                color = colors.grayPending
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(drivers, key = { it.id }) { driver ->
                    val isOnShift = driver.currentShift != null
                    DriverCard(
                        driver = DriverUi(
                            id = driver.id,
                            name = driver.fullName,
                            isOnShift = isOnShift,
                            currentRoute = driver.activeRoute?.title,
                            lastEvent = if (driver.lastEventAt != null) "\u041F\u043E\u0441\u043B\u0435\u0434\u043D\u0435\u0435 \u0441\u043E\u0431\u044B\u0442\u0438\u0435" else null,
                            lastEventTime = driver.lastEventAt
                        ),
                        onClick = { onDriverClick(it) }
                    )
                }
            }
        }
    }
}
