package com.bookxpert.upasnaprojectss.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bookxpert.upasnaprojectss.domain.Department
import com.bookxpert.upasnaprojectss.domain.Employee
import com.bookxpert.upasnaprojectss.domain.EmployeeFormState
import com.bookxpert.upasnaprojectss.domain.EmploymentType
import com.bookxpert.upasnaprojectss.domain.FormErrors
import com.bookxpert.upasnaprojectss.domain.FormField
import com.bookxpert.upasnaprojectss.domain.Gender
import com.bookxpert.upasnaprojectss.domain.Skill
import com.bookxpert.upasnaprojectss.platform.ProfileAvatar
import com.bookxpert.upasnaprojectss.platform.currentTimeMillis
import com.bookxpert.upasnaprojectss.platform.formatDate
import com.bookxpert.upasnaprojectss.platform.rememberPlatformFilePicker
import com.bookxpert.upasnaprojectss.presentation.ActiveFilter
import com.bookxpert.upasnaprojectss.presentation.EmployeeViewModel
import com.bookxpert.upasnaprojectss.presentation.SortOption
import com.bookxpert.upasnaprojectss.validation.validateEmployeeForm
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import upasnaemployeekmp.composeapp.generated.resources.Res
import upasnaemployeekmp.composeapp.generated.resources.*

// ─── Navigation ───────────────────────────────────────────────────────────────

private enum class Screen { Employees, TopEarners, Form, Detail }

// ─── Root ─────────────────────────────────────────────────────────────────────

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

// ─── Bottom Navigation ────────────────────────────────────────────────────────

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

// ─── Employee List ────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EmployeeListScreen(
    state: com.bookxpert.upasnaprojectss.presentation.EmployeeUiState,
    onSearch: (String) -> Unit,
    onFilter: () -> Unit,
    onSort: (SortOption) -> Unit,
    onOpen: (Employee) -> Unit,
    onEdit: (Employee) -> Unit,
    onDelete: (Employee) -> Unit
) {
    var sortOpen by remember { mutableStateOf(false) }

    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        // ── Header ──
        Spacer(Modifier.height(20.dp))
        Text(
            text = stringResource(Res.string.employee_profiles),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "${state.visibleEmployees.size} ${if (state.visibleEmployees.size == 1) "member" else "members"}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(16.dp))

        // ── Search + Actions ──
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = onSearch,
                modifier = Modifier.weight(1f),
                leadingIcon = { Icon(Icons.Default.Search, null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
                placeholder = { Text(stringResource(Res.string.search_placeholder), fontSize = 11.sp) },
                singleLine = true,
                shape = RoundedCornerShape(10.dp)
            )
            BadgedBox(
                badge = {
                    if (state.filters.activeCount > 0) {
                        Badge(containerColor = MaterialTheme.colorScheme.primary) {
                            Text("${state.filters.activeCount}", color = MaterialTheme.colorScheme.onPrimary)
                        }
                    }
                }
            ) {
                IconButton(
                    onClick = onFilter,
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = if (state.filters.activeCount > 0)
                            MaterialTheme.colorScheme.primaryContainer
                        else MaterialTheme.colorScheme.surfaceVariant
                    )
                ) { Icon(Icons.Default.FilterList, stringResource(Res.string.filter)) }
            }
            Box {
                IconButton(
                    onClick = { sortOpen = true },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) { Icon(Icons.Default.KeyboardArrowDown, stringResource(Res.string.sort)) }
                DropdownMenu(expanded = sortOpen, onDismissRequest = { sortOpen = false }) {
                    SortOption.entries.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option.label) },
                            onClick = { onSort(option); sortOpen = false }
                        )
                    }
                }
            }
        }
        Spacer(Modifier.height(12.dp))

        // ── List ──
        if (state.visibleEmployees.isEmpty()) {
            EmptyState()
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                itemsIndexed(state.visibleEmployees, key = { _, e -> e.id }) { _, employee ->
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn() + slideInVertically()
                    ) {
                        EmployeeCard(
                            employee = employee,
                            query = state.searchQuery,
                            onOpen = onOpen,
                            onEdit = onEdit,
                            onDelete = onDelete
                        )
                    }
                }
            }
        }
    }
}

// ─── Employee Card ────────────────────────────────────────────────────────────

@OptIn(ExperimentalFoundationApi::class, ExperimentalLayoutApi::class)
@Composable
private fun EmployeeCard(
    employee: Employee,
    query: String,
    onOpen: (Employee) -> Unit,
    onEdit: (Employee) -> Unit,
    onDelete: (Employee) -> Unit
) {
    var menuOpen by remember { mutableStateOf(false) }
    var confirmDelete by remember { mutableStateOf(false) }

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = { onOpen(employee) },
                onLongClick = { menuOpen = true }
            ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            // ── Top row: avatar + name + status ──
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                ProfileAvatar(
                    employee.profileImagePath,
                    employee.initials(),
                    Modifier.size(52.dp)
                )
                Column(Modifier.weight(1f)) {
                    Text(
                        text = highlight(employee.fullName, query),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = highlight(employee.email, query),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                StatusPill(employee.isActive)
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

            // ── Meta chips ──
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                MetaChip(label = employee.department.label, isPrimary = true)
                MetaChip(label = employee.employmentType.label)
                MetaChip(label = stringResource(Res.string.salary_format, employee.salary.toInt()))
                MetaChip(label = formatDate(employee.joiningDateMillis))
            }
        }

        // ── Context menu ──
        DropdownMenu(expanded = menuOpen, onDismissRequest = { menuOpen = false }) {
            DropdownMenuItem(
                text = { Text(stringResource(Res.string.edit)) },
                leadingIcon = { Icon(Icons.Default.Edit, null, tint = MaterialTheme.colorScheme.primary) },
                onClick = { menuOpen = false; onEdit(employee) }
            )
            DropdownMenuItem(
                text = { Text(stringResource(Res.string.delete), color = MaterialTheme.colorScheme.error) },
                leadingIcon = { Icon(Icons.Default.Delete, null, tint = MaterialTheme.colorScheme.error) },
                onClick = { menuOpen = false; confirmDelete = true }
            )
        }
    }

    if (confirmDelete) {
        DeleteConfirmDialog(
            name = employee.fullName,
            onConfirm = { confirmDelete = false; onDelete(employee) },
            onDismiss = { confirmDelete = false }
        )
    }
}

// ─── Employee Detail ──────────────────────────────────────────────────────────

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun EmployeeDetailScreen(
    employee: Employee,
    onBack: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var confirmDelete by remember { mutableStateOf(false) }

    LazyColumn(
        Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        // ── Top bar ──
        item {
            Row(
                Modifier.padding(start = 4.dp, top = 12.dp, end = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, stringResource(Res.string.back))
                }
                Text(
                    stringResource(Res.string.employee_details),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // ── Hero card ──
        item {
            Box(
                Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                MaterialTheme.colorScheme.primaryContainer,
                                MaterialTheme.colorScheme.surface
                            )
                        )
                    )
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ProfileAvatar(employee.profileImagePath, employee.initials(), Modifier.size(88.dp))
                    Text(employee.fullName, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold)
                    Text(employee.email, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    StatusPill(employee.isActive)
                }
            }
        }

        // ── Work details ──
        item {
            DetailSection(
                title = stringResource(Res.string.work_details),
                rows = listOf(
                    stringResource(Res.string.department) to employee.department.label,
                    stringResource(Res.string.employment_type) to employee.employmentType.label,
                    stringResource(Res.string.joining_date) to formatDate(employee.joiningDateMillis),
                    stringResource(Res.string.salary) to stringResource(Res.string.salary_format, employee.salary.toInt()),
                    stringResource(Res.string.skills) to employee.skills.joinToString { it.label }
                )
            )
        }

        // ── Contact details ──
        item {
            DetailSection(
                title = stringResource(Res.string.contact_details),
                rows = listOf(
                    stringResource(Res.string.phone_number) to employee.phone,
                    stringResource(Res.string.address) to employee.address,
                    stringResource(Res.string.resume_document) to (employee.resume?.name ?: stringResource(Res.string.no_document))
                )
            )
        }

        // ── Action buttons ──
        item {
            Row(
                Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onEdit,
                    modifier = Modifier.weight(1f).height(50.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Edit, null, Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(Res.string.edit))
                }
                OutlinedButton(
                    onClick = { confirmDelete = true },
                    modifier = Modifier.weight(1f).height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.error)
                ) {
                    Icon(Icons.Default.Delete, null, Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(Res.string.delete))
                }
            }
        }
    }

    if (confirmDelete) {
        DeleteConfirmDialog(
            name = employee.fullName,
            onConfirm = { confirmDelete = false; onDelete() },
            onDismiss = { confirmDelete = false }
        )
    }
}

@Composable
private fun DetailSection(title: String, rows: List<Pair<String, String>>) {
    Card(
        Modifier.padding(horizontal = 16.dp, vertical = 6.dp).fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(0.dp)) {
            Text(
                title,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            rows.forEachIndexed { index, (label, value) ->
                Row(
                    Modifier.fillMaxWidth().padding(vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        label,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(Modifier.width(16.dp))
                    Text(
                        value,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.End,
                        modifier = Modifier.weight(1.5f)
                    )
                }
                if (index < rows.lastIndex) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                }
            }
        }
    }
}

// ─── Employee Form ────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun EmployeeFormScreen(
    form: EmployeeFormState,
    errors: FormErrors,
    onChange: ((EmployeeFormState) -> EmployeeFormState) -> Unit,
    onTouch: (FormField) -> Unit,
    onToggleSkill: (Skill) -> Unit,
    onImage: (String) -> Unit,
    onResume: (com.bookxpert.upasnaprojectss.domain.ResumeDocument) -> Unit,
    onRemoveResume: () -> Unit,
    onSave: () -> Unit
) {
    var showImageSheet by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    val picker = rememberPlatformFilePicker(
        onImagePicked = { onImage(it.localPath) },
        onDocumentPicked = onResume,
        onError = {}
    )

    LazyColumn(
        Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // ── Header ──
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box {
                    ProfileAvatar(form.profileImagePath, form.fullName.initialsFromName(), Modifier.size(76.dp))
                    Surface(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(28.dp)
                            .clickable { showImageSheet = true },
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary,
                        shadowElevation = 4.dp
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Default.CameraAlt,
                                null,
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
                Column {
                    Text(
                        if (form.editingId == null) stringResource(Res.string.add_employee)
                        else stringResource(Res.string.edit_employee),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        stringResource(Res.string.bookxpert_profile_details),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // ── Section: Personal Info ──
        item { FormSectionLabel("Personal Info") }

        item {
            FormField(
                value = form.fullName,
                label = stringResource(Res.string.full_name),
                field = FormField.FullName,
                error = errors.fullName,
                touched = form.touched,
                onTouch = onTouch,
                onChange = { onChange { s -> s.copy(fullName = it) } }
            )
        }
        item {
            FormField(
                value = form.email,
                label = stringResource(Res.string.email),
                field = FormField.Email,
                error = errors.email,
                touched = form.touched,
                onTouch = onTouch,
                keyboardType = KeyboardType.Email,
                onChange = { onChange { s -> s.copy(email = it) } }
            )
        }
        item {
            FormField(
                value = form.phone,
                label = stringResource(Res.string.phone_number_label),
                field = FormField.Phone,
                error = errors.phone,
                touched = form.touched,
                onTouch = onTouch,
                keyboardType = KeyboardType.Phone,
                onChange = { v -> onChange { s -> s.copy(phone = v.filter { c -> c.isDigit() || c in "+- ()" }) } }
            )
        }
        item {
            OutlinedTextField(
                value = form.address,
                onValueChange = { v -> onChange { s -> s.copy(address = v) } },
                modifier = Modifier.fillMaxWidth().onFocusChanged { if (!it.isFocused) onTouch(FormField.Address) },
                label = { Text(stringResource(Res.string.address)) },
                minLines = 3,
                shape = RoundedCornerShape(12.dp),
                isError = FormField.Address in form.touched && errors.address != null,
                supportingText = { if (FormField.Address in form.touched) errors.address?.let { Text(it) } }
            )
        }

        // ── Gender ──
        item { RadioRow(stringResource(Res.string.gender), Gender.entries, form.gender, { it.label }, errors.gender) { onChange { s -> s.copy(gender = it) } } }

        // ── Section: Work Info ──
        item { FormSectionLabel("Work Details") }

        item {
            EnumDropdown(
                stringResource(Res.string.department),
                Department.entries,
                form.department,
                { it.label },
                errors.department
            ) { v -> onChange { s -> s.copy(department = v) } }
        }

        // ── Skills ──
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(stringResource(Res.string.skills), fontWeight = FontWeight.SemiBold)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Skill.entries.forEach { skill ->
                        FilterChip(
                            selected = skill in form.skills,
                            onClick = { onToggleSkill(skill) },
                            label = { Text(skill.label) }
                        )
                    }
                }
                errors.skills?.let {
                    Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }
            }
        }

        item {
            EnumDropdown(
                stringResource(Res.string.employment_type),
                EmploymentType.entries,
                form.employmentType,
                { it.label },
                errors.employmentType
            ) { v -> onChange { s -> s.copy(employmentType = v) } }
        }

        // ── Active toggle ──
        item {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Row(
                    Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(stringResource(Res.string.active), fontWeight = FontWeight.SemiBold)
                        Text(
                            if (form.isActive) "Currently employed" else "Not currently employed",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(
                        checked = form.isActive,
                        onCheckedChange = { checked -> onChange { s -> s.copy(isActive = checked) } }
                    )
                }
            }
        }

        // ── Dates & Salary ──
        item {
            OutlinedTextField(
                value = form.joiningDateMillis?.let { formatDate(it) }.orEmpty(),
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                label = { Text(stringResource(Res.string.joining_date)) },
                shape = RoundedCornerShape(12.dp),
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Default.KeyboardArrowDown, null)
                    }
                },
                isError = errors.joiningDate != null,
                supportingText = { errors.joiningDate?.let { Text(it) } }
            )
        }

        item {
            OutlinedTextField(
                value = form.salary,
                onValueChange = { v -> onChange { s -> s.copy(salary = v.filter { c -> c.isDigit() || c == '.' }) } },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(Res.string.salary)) },
                prefix = { Text("₹ ") },
                shape = RoundedCornerShape(12.dp),
                isError = errors.salary != null,
                supportingText = { errors.salary?.let { Text(it) } },
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
        }

        // ── Resume ──
        item { ResumePicker(form, onPick = picker::chooseDocument, onRemove = onRemoveResume) }

        // ── Submit ──
        item {
            Button(
                onClick = onSave,
                modifier = Modifier.fillMaxWidth().height(54.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(
                    if (form.editingId == null) stringResource(Res.string.submit_employee)
                    else stringResource(Res.string.update_employee),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        item { Spacer(Modifier.height(16.dp)) }
    }

    // ── Image picker sheet ──
    if (showImageSheet) {
        ModalBottomSheet(
            onDismissRequest = { showImageSheet = false },
            sheetState = rememberModalBottomSheetState()
        ) {
            Text(
                stringResource(Res.string.profile_image),
                Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            HorizontalDivider(Modifier.padding(vertical = 8.dp))
            DropdownMenuItem(
                text = { Text(stringResource(Res.string.take_photo)) },
                leadingIcon = { Icon(Icons.Default.CameraAlt, null, tint = MaterialTheme.colorScheme.primary) },
                onClick = { showImageSheet = false; picker.takePhoto() }
            )
            DropdownMenuItem(
                text = { Text(stringResource(Res.string.choose_gallery)) },
                leadingIcon = { Icon(Icons.Default.Image, null, tint = MaterialTheme.colorScheme.primary) },
                onClick = { showImageSheet = false; picker.chooseFromGallery() }
            )
            Spacer(Modifier.height(28.dp))
        }
    }

    // ── Date picker ──
    if (showDatePicker) {
        val dateState = rememberDatePickerState(initialSelectedDateMillis = form.joiningDateMillis)
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    dateState.selectedDateMillis?.let { v -> onChange { s -> s.copy(joiningDateMillis = v) } }
                    showDatePicker = false
                }) { Text(stringResource(Res.string.ok)) }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text(stringResource(Res.string.cancel)) }
            }
        ) { DatePicker(dateState) }
    }
}

// ─── Resume Picker ────────────────────────────────────────────────────────────

@Composable
private fun ResumePicker(form: EmployeeFormState, onPick: () -> Unit, onRemove: () -> Unit) {
    val hasResume = form.resume != null
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (hasResume) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            Modifier.fillMaxWidth().padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                modifier = Modifier.size(42.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.AttachFile, null, tint = MaterialTheme.colorScheme.primary)
                }
            }
            Column(Modifier.weight(1f)) {
                Text(
                    form.resume?.name ?: stringResource(Res.string.resume_document_label),
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    form.resume?.let { stringResource(Res.string.document_meta_format, it.mimeType, it.sizeLabel) }
                        ?: stringResource(Res.string.resume_hint),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (!hasResume) {
                Button(
                    onClick = onPick,
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                ) { Text(stringResource(Res.string.select)) }
            } else {
                IconButton(onClick = onRemove) {
                    Icon(Icons.Default.Close, null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

// ─── Filter Sheet ─────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun FilterSheet(
    selectedDepartments: Set<Department>,
    activeFilter: ActiveFilter,
    selectedTypes: Set<EmploymentType>,
    onDepartment: (Department) -> Unit,
    onActive: (ActiveFilter) -> Unit,
    onType: (EmploymentType) -> Unit,
    onClear: () -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = rememberModalBottomSheetState()) {
        Column(
            Modifier
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(stringResource(Res.string.filters), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold)
                TextButton(onClick = onClear) { Text(stringResource(Res.string.clear_all_filters)) }
            }

            FilterGroup(title = stringResource(Res.string.department)) {
                Department.entries.forEach {
                    FilterChip(selected = it in selectedDepartments, onClick = { onDepartment(it) }, label = { Text(it.label) })
                }
            }

            FilterGroup(title = stringResource(Res.string.status)) {
                ActiveFilter.entries.forEach {
                    FilterChip(selected = activeFilter == it, onClick = { onActive(it) }, label = { Text(it.label) })
                }
            }

            FilterGroup(title = stringResource(Res.string.employment)) {
                EmploymentType.entries.forEach {
                    FilterChip(selected = it in selectedTypes, onClick = { onType(it) }, label = { Text(it.label) })
                }
            }

            Spacer(Modifier.height(28.dp))
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FilterGroup(title: String, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            content()
        }
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
    }
}

// ─── Top Earners ──────────────────────────────────────────────────────────────

@Composable
private fun TopEarnersScreen(topEarners: List<Employee>, topN: Int, onTopN: (Int) -> Unit) {
    Column(
        Modifier.fillMaxSize().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        Spacer(Modifier.height(20.dp))
        Text(stringResource(Res.string.top_earners), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold)
        Text("Ranked by compensation", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(16.dp))

        // Counter control
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                Modifier.fillMaxWidth().padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Showing top", style = MaterialTheme.typography.bodyMedium)
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    IconButton(
                        onClick = { if (topN > 1) onTopN(topN - 1) },
                        colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) { Icon(Icons.Default.Remove, null, Modifier.size(18.dp)) }
                    Text(
                        "$topN",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.width(36.dp),
                        textAlign = TextAlign.Center
                    )
                    IconButton(
                        onClick = { onTopN(topN + 1) },
                        colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) { Icon(Icons.Default.Add, null, Modifier.size(18.dp)) }
                }
            }
        }
        Spacer(Modifier.height(12.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            itemsIndexed(topEarners) { index, employee ->
                TopEarnerCard(rank = index + 1, employee = employee)
            }
        }
    }
}

@Composable
private fun TopEarnerCard(rank: Int, employee: Employee) {
    val isTopThree = rank <= 3
    val medalColor = when (rank) {
        1 -> Color(0xFFFFD700) // Gold
        2 -> Color(0xFFC0C0C0) // Silver
        3 -> Color(0xFFCD7F32) // Bronze
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    ElevatedCard(
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.elevatedCardElevation(if (isTopThree) 3.dp else 1.dp)
    ) {
        Row(
            Modifier.fillMaxWidth().padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Rank badge
            Box(
                Modifier.size(40.dp).clip(CircleShape).background(
                    if (isTopThree) medalColor.copy(alpha = 0.15f)
                    else MaterialTheme.colorScheme.surfaceVariant
                ),
                contentAlignment = Alignment.Center
            ) {
                if (isTopThree) {
                    Icon(Icons.Default.Star, null, tint = medalColor, modifier = Modifier.size(20.dp))
                } else {
                    Text(
                        "#$rank",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            ProfileAvatar(employee.profileImagePath, employee.initials(), Modifier.size(44.dp))

            Column(Modifier.weight(1f)) {
                Text(employee.fullName, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                Text(employee.department.label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    stringResource(Res.string.salary_format, employee.salary.toInt()),
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )
                if (isTopThree) {
                    Text(
                        when (rank) { 1 -> "🥇 Gold"; 2 -> "🥈 Silver"; else -> "🥉 Bronze" },
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

// ─── Empty State ──────────────────────────────────────────────────────────────

@Composable
private fun EmptyState() {
    Box(Modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                Modifier.size(72.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Search, null, Modifier.size(36.dp), tint = MaterialTheme.colorScheme.primary)
            }
            Text(stringResource(Res.string.no_employees_found), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(
                stringResource(Res.string.empty_search_hint),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

// ─── Shared / Small Components ────────────────────────────────────────────────

@Composable
private fun StatusPill(active: Boolean) {
    val bg by animateColorAsState(if (active) Color(0xFF2E7D32).copy(alpha = 0.12f) else Color(0xFFC62828).copy(alpha = 0.12f))
    val fg = if (active) Color(0xFF2E7D32) else Color(0xFFC62828)
    Surface(shape = RoundedCornerShape(50), color = bg) {
        Row(
            Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Box(Modifier.size(7.dp).clip(CircleShape).background(fg))
            Text(
                if (active) stringResource(Res.string.active_status) else stringResource(Res.string.inactive_status),
                style = MaterialTheme.typography.labelSmall,
                color = fg,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun MetaChip(label: String, isPrimary: Boolean = false) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = if (isPrimary) MaterialTheme.colorScheme.primaryContainer
        else MaterialTheme.colorScheme.surfaceVariant
    ) {
        Text(
            label,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = if (isPrimary) MaterialTheme.colorScheme.onPrimaryContainer
            else MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = if (isPrimary) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

@Composable
private fun FormSectionLabel(label: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        HorizontalDivider(Modifier.weight(1f), color = MaterialTheme.colorScheme.outlineVariant)
        Text(
            label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.ExtraBold
        )
        HorizontalDivider(Modifier.weight(1f), color = MaterialTheme.colorScheme.outlineVariant)
    }
}

@Composable
private fun FormField(
    value: String,
    label: String,
    field: FormField,
    error: String?,
    touched: Set<FormField>,
    onTouch: (FormField) -> Unit,
    onChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged { if (!it.isFocused) onTouch(field) },
        label = { Text(label) },
        shape = RoundedCornerShape(12.dp),
        isError = field in touched && error != null,
        supportingText = { if (field in touched) error?.let { Text(it) } },
        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = keyboardType),
        singleLine = singleLine
    )
}

@Composable
private fun DeleteConfirmDialog(name: String, onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { Icon(Icons.Default.Delete, null, tint = MaterialTheme.colorScheme.error) },
        title = { Text(stringResource(Res.string.delete_employee_title), fontWeight = FontWeight.Bold) },
        text = { Text(stringResource(Res.string.delete_employee_message, name)) },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) { Text(stringResource(Res.string.delete)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(Res.string.cancel)) }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun <T> EnumDropdown(
    label: String,
    values: List<T>,
    selected: T?,
    labelOf: (T) -> String,
    error: String?,
    onSelected: (T) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        OutlinedTextField(
            value = selected?.let(labelOf).orEmpty(),
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            shape = RoundedCornerShape(12.dp),
            isError = error != null,
            supportingText = { error?.let { Text(it) } },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            values.forEach { value ->
                DropdownMenuItem(
                    text = { Text(labelOf(value)) },
                    onClick = { onSelected(value); expanded = false }
                )
            }
        }
    }
}

@Composable
private fun <T> RadioRow(
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

// ─── Utilities ────────────────────────────────────────────────────────────────

private fun highlight(text: String, query: String) = buildAnnotatedString {
    val q = query.trim()
    val index = text.lowercase().indexOf(q.lowercase())
    if (q.isBlank() || index < 0) {
        append(text)
    } else {
        append(text.substring(0, index))
        withStyle(SpanStyle(background = Color(0xFFFFE082), fontWeight = FontWeight.Bold)) {
            append(text.substring(index, index + q.length))
        }
        append(text.substring(index + q.length))
    }
}

private fun Employee.initials(): String = fullName.initialsFromName()

private fun String.initialsFromName(): String =
    trim().split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .joinToString("") { it.first().uppercase() }
        .ifBlank { "EP" }

// ─── Color Schemes ────────────────────────────────────────────────────────────

private fun lightAppColors() = androidx.compose.material3.lightColorScheme(
    primary = Color(0xFF1B6B53),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD2F0E4),
    onPrimaryContainer = Color(0xFF00391F),
    secondary = Color(0xFF4D6357),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFD0E8D8),
    tertiary = Color(0xFF3D6373),
    background = Color(0xFFF4F8F6),
    surface = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFFEEF3F0),
    onSurfaceVariant = Color(0xFF5E7568),
    outline = Color(0xFFB0C4BA),
    outlineVariant = Color(0xFFD8E8E0)
)

private fun darkAppColors() = androidx.compose.material3.darkColorScheme(
    primary = Color(0xFF7DD8BA),
    onPrimary = Color(0xFF003826),
    primaryContainer = Color(0xFF00513A),
    onPrimaryContainer = Color(0xFF9AF4D6),
    secondary = Color(0xFFB5CCBE),
    background = Color(0xFF0C1410),
    surface = Color(0xFF151D18),
    surfaceVariant = Color(0xFF1E2922),
    onSurfaceVariant = Color(0xFFAAC4B3),
    outline = Color(0xFF3D5145),
    outlineVariant = Color(0xFF2A3D33)
)

// ─── Typography ───────────────────────────────────────────────────────────────

private fun appTypography() = androidx.compose.material3.Typography()