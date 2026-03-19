package com.example.belsidriver.presentation.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.example.belsidriver.ui.theme.BelsiTheme

@Composable
fun PhotoThumbnail(
    imageUrl: String,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = BelsiTheme.colors
    val shape = RoundedCornerShape(12.dp)

    SubcomposeAsyncImage(
        model = imageUrl,
        contentDescription = contentDescription,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .size(80.dp)
            .clip(shape)
            .clickable(onClick = onClick),
        loading = {
            PhotoThumbnailPlaceholder(modifier = Modifier.size(80.dp))
        },
        error = {
            PhotoThumbnailPlaceholder(modifier = Modifier.size(80.dp))
        }
    )
}

@Composable
private fun PhotoThumbnailPlaceholder(
    modifier: Modifier = Modifier
) {
    val colors = BelsiTheme.colors

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(colors.grayLight),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Outlined.CameraAlt,
            contentDescription = null,
            tint = colors.grayPending,
            modifier = Modifier.size(28.dp)
        )
    }
}
