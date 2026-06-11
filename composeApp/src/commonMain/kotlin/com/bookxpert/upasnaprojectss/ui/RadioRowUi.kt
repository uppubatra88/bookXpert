package com.bookxpert.upasnaprojectss.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
internal fun <T> RadioRow(
    title: String,
    values: List<T>,
    selected: T?,
    labelOf: (T) -> String,
    error: String?,
    onSelected: (T) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(title, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            values.forEach { value ->
                Row(
                    modifier = Modifier.clickable { onSelected(value) },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(selected = selected == value, onClick = { onSelected(value) })
                    Text(labelOf(value), style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
        error?.let { Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall) }
    }
}