package com.bookxpert.upasnaprojectss.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Group
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import upasnaemployeekmp.composeapp.generated.resources.Res
import upasnaemployeekmp.composeapp.generated.resources.employees_tab
import upasnaemployeekmp.composeapp.generated.resources.form_tab
import upasnaemployeekmp.composeapp.generated.resources.top_tab

enum class Screen { Employees, TopEarners, Form, Detail }

@Composable
private fun AppBottomBar(
    current: Screen,
    onEmployees: () -> Unit,
    onTopEarners: () -> Unit,
    onForm: () -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = current == Screen.Employees,
            onClick = onEmployees,
            icon = { Icon(Icons.Default.Group, null) },
            label = { Text(stringResource(Res.string.employees_tab)) }
        )
        NavigationBarItem(
            selected = current == Screen.TopEarners,
            onClick = onTopEarners,
            icon = { Icon(Icons.Default.EmojiEvents, null) },
            label = { Text(stringResource(Res.string.top_tab)) }
        )
        NavigationBarItem(
            selected = current == Screen.Form,
            onClick = onForm,
            icon = { Icon(Icons.Default.Edit, null) },
            label = { Text(stringResource(Res.string.form_tab)) }
        )
    }
}