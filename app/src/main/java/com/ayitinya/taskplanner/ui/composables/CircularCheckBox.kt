package com.ayitinya.taskplanner.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun CircularCheckBox(
    modifier: Modifier = Modifier, checked: Boolean, onCheckedChange: (checked: Boolean) -> Unit
) {
    val color = MaterialTheme.colorScheme

    val imageVector = if (checked) Icons.Filled.TaskAlt else Icons.Outlined.Circle
    val tint = if (checked) color.onPrimary else color.onSurfaceVariant
    val background = if (checked) color.primary else Color.Transparent

    IconButton(onClick = {
        onCheckedChange(!checked)
    }, modifier = modifier) {
        Icon(
            imageVector = imageVector,
            tint = tint,
            contentDescription = "checkbox",
            modifier = Modifier
                .background(background, shape = CircleShape)
                .fillMaxSize()
        )
    }
}