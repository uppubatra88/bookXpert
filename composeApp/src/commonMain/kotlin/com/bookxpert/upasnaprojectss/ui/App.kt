package com.bookxpert.upasnaprojectss.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bookxpert.upasnaprojectss.domain.Employee
import com.bookxpert.upasnaprojectss.platform.currentTimeMillis
import com.bookxpert.upasnaprojectss.presentation.EmployeeViewModel
import com.bookxpert.upasnaprojectss.validation.validateEmployeeForm
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import upasnaemployeekmp.composeapp.generated.resources.Res
import upasnaemployeekmp.composeapp.generated.resources.add
import upasnaemployeekmp.composeapp.generated.resources.employees_tab
import upasnaemployeekmp.composeapp.generated.resources.form_tab
import upasnaemployeekmp.composeapp.generated.resources.top_tab
import upasnaemployeekmp.composeapp.generated.resources.undo


@Composable
fun App(viewModel: EmployeeViewModel = koinInject()) {
    val isDark = isSystemInDarkTheme()
    val colors = if (isDark) darkAppColors() else lightAppColors()
    MaterialTheme(colorScheme = colors, typography = appTypography()) {
        val state by viewModel.uiState.collectAsState()
        val form by viewModel.form.collectAsState()
        val snackbarHost = remember { SnackbarHostState() }
        var screen by remember { mutableStateOf(Screen.Employees) }
        var selectedEmployee by remember { mutableStateOf<Employee?>(null) }
        var showFilters by remember { mutableStateOf(false) }
        val undoLabel = stringResource(Res.string.undo)

        LaunchedEffect(state.snackbarMessage) {
            state.snackbarMessage?.let {
                val result = snackbarHost.showSnackbar(
                    it,
                    actionLabel = if (it.contains(undoLabel)) undoLabel else null
                )
                if (result.name == "ActionPerformed") viewModel.undoDelete()
                viewModel.showMessage(null)
            }
        }

        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHost) },
            containerColor = MaterialTheme.colorScheme.background,
            floatingActionButton = {
                if (screen != Screen.Form && screen != Screen.Detail) {
                    FloatingActionButton(
                        onClick = { viewModel.newEmployee(); screen = Screen.Form },
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        elevation = FloatingActionButtonDefaults.elevation(8.dp)
                    ) {
                        Icon(Icons.Default.PersonAdd, contentDescription = stringResource(Res.string.add))
                    }
                }
            },
            bottomBar = {
                if (screen != Screen.Detail) {
                    AppBottomBar(
                        current = screen,
                        onEmployees = { screen = Screen.Employees },
                        onTopEarners = { screen = Screen.TopEarners },
                        onForm = { viewModel.newEmployee(); screen = Screen.Form }
                    )
                }
            }
        ) { padding ->
            AnimatedContent(
                targetState = screen,
                transitionSpec = {
                    fadeIn(tween(220)) togetherWith fadeOut(tween(150))
                },
                modifier = Modifier.padding(padding).fillMaxSize()
            ) { target ->
                when (target) {
                    Screen.Employees -> EmployeeListScreen(
                        state = state,
                        onSearch = viewModel::search,
                        onFilter = { showFilters = true },
                        onSort = viewModel::setSort,
                        onOpen = { selectedEmployee = it; screen = Screen.Detail },
                        onEdit = { viewModel.edit(it); screen = Screen.Form },
                        onDelete = viewModel::delete
                    )
                    Screen.TopEarners -> TopEarnersScreen(
                        topEarners = state.topEarners,
                        topN = state.topN,
                        onTopN = viewModel::setTopN
                    )
                    Screen.Form -> EmployeeFormScreen(
                        form = form,
                        errors = validateEmployeeForm(form, currentTimeMillis()),
                        onChange = viewModel::updateForm,
                        onTouch = viewModel::touch,
                        onToggleSkill = viewModel::toggleSkill,
                        onImage = viewModel::setProfileImage,
                        onResume = viewModel::setResume,
                        onRemoveResume = viewModel::removeResume,
                        onSave = { viewModel.save { screen = Screen.Employees } }
                    )
                    Screen.Detail -> selectedEmployee?.let { emp ->
                        EmployeeDetailScreen(
                            employee = emp,
                            onBack = { screen = Screen.Employees },
                            onEdit = { viewModel.edit(emp); screen = Screen.Form },
                            onDelete = { viewModel.delete(emp); screen = Screen.Employees }
                        )
                    } ?: run {
                        screen = Screen.Employees
                    }
                }
            }
        }

        if (showFilters) {
            FilterSheet(
                selectedDepartments = state.filters.departments,
                activeFilter = state.filters.activeFilter,
                selectedTypes = state.filters.employmentTypes,
                onDepartment = viewModel::toggleDepartment,
                onActive = viewModel::setActiveFilter,
                onType = viewModel::toggleEmploymentType,
                onClear = viewModel::clearFilters,
                onDismiss = { showFilters = false }
            )
        }
    }
}

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