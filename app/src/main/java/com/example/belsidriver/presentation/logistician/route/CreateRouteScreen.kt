package com.example.belsidriver.presentation.logistician.route

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.belsidriver.presentation.common.components.BelsiAppBar
import com.example.belsidriver.presentation.common.components.BelsiButton
import com.example.belsidriver.presentation.common.components.BelsiButtonSize
import com.example.belsidriver.presentation.common.components.BelsiButtonVariant
import com.example.belsidriver.presentation.common.components.BelsiTextField
import com.example.belsidriver.presentation.common.components.BelsiTextFieldType
import com.example.belsidriver.ui.theme.BelsiTheme
import com.example.belsidriver.util.IntentUtils

@Composable
fun CreateRouteScreen(
    onBack: () -> Unit,
    onSuccess: () -> Unit,
    viewModel: CreateRouteViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val colors = BelsiTheme.colors
    val context = LocalContext.current

    LaunchedEffect(uiState.success) {
        if (uiState.success) onSuccess()
    }

    Scaffold(
        topBar = {
            BelsiAppBar(
                title = "Создать маршрут",
                onNavigateBack = onBack
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Route info card
            item {
                Spacer(modifier = Modifier.height(4.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    colors = CardDefaults.cardColors(containerColor = colors.cardBackground)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        BelsiTextField(
                            value = uiState.title,
                            onValueChange = viewModel::updateTitle,
                            label = "Название рейса",
                            placeholder = "Например: Рейс Москва-Казань"
                        )

                        BelsiTextField(
                            value = uiState.plannedDate,
                            onValueChange = viewModel::updateDate,
                            label = "Дата (ГГГГ-ММ-ДД)",
                            placeholder = "2026-02-27",
                            required = true
                        )
                    }
                }
            }

            // Section title: Delivery points
            item {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Точки доставки",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Маршрут для Яндекс.Навигатора будет построен автоматически",
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.grayPending
                )
                Spacer(modifier = Modifier.height(4.dp))
            }

            // Dynamic list of point cards
            itemsIndexed(uiState.points) { index, point ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    colors = CardDefaults.cardColors(containerColor = colors.cardBackground)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Точка ${index + 1}",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f)
                            )
                            if (uiState.points.size > 1) {
                                IconButton(onClick = { viewModel.removePoint(index) }) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Удалить точку",
                                        tint = colors.errorRed
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        BelsiTextField(
                            value = point.address,
                            onValueChange = {
                                viewModel.updatePoint(index, point.copy(address = it))
                            },
                            label = "Адрес",
                            placeholder = "Москва, ул. Примерная 1",
                            required = true
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        BelsiTextField(
                            value = point.contactName,
                            onValueChange = {
                                viewModel.updatePoint(index, point.copy(contactName = it))
                            },
                            label = "Контактное лицо"
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        BelsiTextField(
                            value = point.contactPhone,
                            onValueChange = {
                                viewModel.updatePoint(index, point.copy(contactPhone = it))
                            },
                            label = "Телефон",
                            type = BelsiTextFieldType.Phone
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        BelsiTextField(
                            value = point.notes,
                            onValueChange = {
                                viewModel.updatePoint(index, point.copy(notes = it))
                            },
                            label = "Примечание",
                            multiline = true,
                            rows = 2
                        )
                    }
                }
            }

            // Add point button
            item {
                BelsiButton(
                    text = "Добавить точку",
                    onClick = viewModel::addPoint,
                    variant = BelsiButtonVariant.Outlined,
                    fullWidth = true,
                    startIcon = {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                )
            }

            // Preview route on map button
            item {
                val hasEnoughAddresses = uiState.points.count { it.address.isNotBlank() } >= 2
                BelsiButton(
                    text = "Предпросмотр маршрута на карте",
                    onClick = {
                        val url = viewModel.buildPreviewUrl()
                        if (url != null) {
                            IntentUtils.openYandexMaps(context, url)
                        }
                    },
                    variant = BelsiButtonVariant.Secondary,
                    fullWidth = true,
                    enabled = hasEnoughAddresses,
                    startIcon = {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                )
            }

            // Error text
            if (uiState.error != null) {
                item {
                    Text(
                        text = uiState.error!!,
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.errorRed
                    )
                }
            }

            // Save button
            item {
                BelsiButton(
                    onClick = viewModel::createRoute,
                    variant = BelsiButtonVariant.Primary,
                    size = BelsiButtonSize.Large,
                    fullWidth = true,
                    enabled = !uiState.isLoading
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "Сохранить маршрут",
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
