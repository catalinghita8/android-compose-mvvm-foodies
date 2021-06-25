package com.codingtroops.composesample.feature.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun BaseAppBar(text: String, icon: ImageVector, action: () -> Unit = { }) {
    TopAppBar(
        navigationIcon = {
            Icon(
                imageVector = icon,
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .clickable(onClick = action),
                contentDescription = "Action icon"
            )
        },
        title = { Text(text) }
    )
}