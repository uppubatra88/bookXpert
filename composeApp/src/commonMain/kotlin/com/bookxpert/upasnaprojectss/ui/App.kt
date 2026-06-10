package com.bookxpert.upasnaprojectss.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.ExperimentalLayoutApi
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
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
import org.koin.compose.koinInject

private enum class Screen { Employees, TopEarners, Form, Detail }

@Composable
fun App(viewModel: EmployeeViewModel = koinInject()) {
    val colors = if (isSystemInDarkTheme()) darkAppColors() else lightAppColors()
    MaterialTheme(colorScheme = colors) {
        val state by viewModel.uiState.collectAsState()
        val form by viewModel.form.collectAsState()
        val snackbarHost = remember { SnackbarHostState() }
        var screen by remember { mutableStateOf(Screen.Employees) }
        var selectedEmployee by remember { mutableStateOf<Employee?>(null) }
        var showFilters by remember { mutableStateOf(false) }

        LaunchedEffect(state.snackbarMessage) {
            state.snackbarMessage?.let {
                val result = snackbarHost.showSnackbar(it, actionLabel = if (it.contains("Undo")) "Undo" else null)
                if (result.name == "ActionPerformed") viewModel.undoDelete()
                viewModel.showMessage(null)
            }
        }

        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHost) },
            floatingActionButton = {
                if (screen != Screen.Form && screen != Screen.Detail) {
                    FloatingActionButton(onClick = { viewModel.newEmployee(); screen = Screen.Form }) {
                        Icon(Icons.Default.Add, contentDescription = "Add")
                    }
                }
            },
            bottomBar = {
                if (screen != Screen.Detail) {
                    NavigationBar {
                        NavigationBarItem(selected = screen == Screen.Employees, onClick = { screen = Screen.Employees }, icon = { Icon(Icons.Default.Search, null) }, label = { Text(AppText.employeesTab) })
                        NavigationBarItem(selected = screen == Screen.TopEarners, onClick = { screen = Screen.TopEarners }, icon = { Text("#") }, label = { Text(AppText.topTab) })
                        NavigationBarItem(selected = screen == Screen.Form, onClick = { viewModel.newEmployee(); screen = Screen.Form }, icon = { Icon(Icons.Default.Edit, null) }, label = { Text(AppText.formTab) })
                    }
                }
            }
        ) { padding ->
            Box(Modifier.padding(padding).fillMaxSize()) {
                when (screen) {
                    Screen.Employees -> EmployeeListScreen(
                        state = state,
                        onSearch = viewModel::search,
                        onFilter = { showFilters = true },
                        onSort = viewModel::setSort,
                        onOpen = { selectedEmployee = it; screen = Screen.Detail },
                        onEdit = { viewModel.edit(it); screen = Screen.Form },
                        onDelete = viewModel::delete
                    )
                    Screen.TopEarners -> TopEarnersScreen(state.topEarners, state.topN, viewModel::setTopN)
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
                    Screen.Detail -> selectedEmployee?.let { employee ->
                        EmployeeDetailScreen(
                            employee = employee,
                            onBack = { screen = Screen.Employees },
                            onEdit = { viewModel.edit(employee); screen = Screen.Form },
                            onDelete = {
                                viewModel.delete(employee)
                                screen = Screen.Employees
                            }
                        )
                    } ?: EmployeeListScreen(
                        state = state,
                        onSearch = viewModel::search,
                        onFilter = { showFilters = true },
                        onSort = viewModel::setSort,
                        onOpen = { selectedEmployee = it; screen = Screen.Detail },
                        onEdit = { viewModel.edit(it); screen = Screen.Form },
                        onDelete = viewModel::delete
                    )
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
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(AppText.employeeProfiles, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = onSearch,
                modifier = Modifier.weight(1f),
                leadingIcon = { Icon(Icons.Default.Search, null) },
                placeholder = { Text(AppText.searchPlaceholder) },
                singleLine = true
            )
            BadgedBox(badge = { if (state.filters.activeCount > 0) Badge { Text("${state.filters.activeCount}") } }) {
                IconButton(onClick = onFilter) { Icon(Icons.Default.FilterList, AppText.filter) }
            }
            Box {
                IconButton(onClick = { sortOpen = true }) { Icon(Icons.Default.KeyboardArrowDown, AppText.sort) }
                DropdownMenu(expanded = sortOpen, onDismissRequest = { sortOpen = false }) {
                    SortOption.entries.forEach {
                        DropdownMenuItem(text = { Text(it.label) }, onClick = { onSort(it); sortOpen = false })
                    }
                }
            }
        }
        if (state.visibleEmployees.isEmpty()) {
            EmptyState()
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp), contentPadding = PaddingValues(bottom = 96.dp)) {
                items(state.visibleEmployees, key = { it.id }) { employee ->
                    AnimatedVisibility(true) {
                        EmployeeCard(employee, state.searchQuery, onOpen, onEdit, onDelete)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalLayoutApi::class)
@Composable
private fun EmployeeCard(employee: Employee, query: String, onOpen: (Employee) -> Unit, onEdit: (Employee) -> Unit, onDelete: (Employee) -> Unit) {
    var menuOpen by remember { mutableStateOf(false) }
    var confirmDelete by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier.fillMaxWidth().combinedClickable(onClick = { onOpen(employee) }, onLongClick = { menuOpen = true }),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ProfileAvatar(employee.profileImagePath, employee.initials(), Modifier.size(56.dp))
                Column(Modifier.weight(1f)) {
                    Text(highlight(employee.fullName, query), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Text(highlight(employee.email, query), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                StatusDot(employee.isActive)
            }
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                ElevatedAssistChip(onClick = {}, label = { Text(employee.department.label) })
                AssistChip(onClick = {}, label = { Text(employee.employmentType.label) })
                AssistChip(onClick = {}, label = { Text("₹${employee.salary.toInt()}") })
                AssistChip(onClick = {}, label = { Text(formatDate(employee.joiningDateMillis)) })
            }
        }
        DropdownMenu(expanded = menuOpen, onDismissRequest = { menuOpen = false }) {
            DropdownMenuItem(text = { Text(AppText.edit) }, leadingIcon = { Icon(Icons.Default.Edit, null) }, onClick = { menuOpen = false; onEdit(employee) })
            DropdownMenuItem(text = { Text(AppText.delete) }, leadingIcon = { Icon(Icons.Default.Delete, null) }, onClick = { menuOpen = false; confirmDelete = true })
        }
    }
    if (confirmDelete) {
        AlertDialog(
            onDismissRequest = { confirmDelete = false },
            title = { Text(AppText.deleteEmployeeTitle) },
            text = { Text("${AppText.deleteEmployeePrefix} ${employee.fullName}. ${AppText.deleteEmployeeSuffix}") },
            confirmButton = { TextButton(onClick = { confirmDelete = false; onDelete(employee) }) { Text(AppText.delete) } },
            dismissButton = { TextButton(onClick = { confirmDelete = false }) { Text(AppText.cancel) } }
        )
    }
}

@Composable
private fun EmployeeDetailScreen(
    employee: Employee,
    onBack: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var confirmDelete by remember { mutableStateOf(false) }
    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, AppText.back) }
                Text(AppText.employeeDetails, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            }
        }
        item {
            Card(shape = RoundedCornerShape(8.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                Column(Modifier.fillMaxWidth().padding(18.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    ProfileAvatar(employee.profileImagePath, employee.initials(), Modifier.size(96.dp))
                    Text(employee.fullName, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text(employee.email, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    StatusDot(employee.isActive)
                }
            }
        }
        item {
            DetailSection(
                title = AppText.workDetails,
                rows = listOf(
                    AppText.department to employee.department.label,
                    AppText.employmentType to employee.employmentType.label,
                    AppText.joiningDate to formatDate(employee.joiningDateMillis),
                    AppText.salary to "₹${employee.salary.toInt()}",
                    AppText.skills to employee.skills.joinToString { it.label }
                )
            )
        }
        item {
            DetailSection(
                title = AppText.contactDetails,
                rows = listOf(
                    AppText.phoneNumber to employee.phone,
                    AppText.address to employee.address,
                    AppText.resumeDocument to (employee.resume?.name ?: AppText.noDocument)
                )
            )
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                Button(onClick = onEdit, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Default.Edit, null)
                    Spacer(Modifier.width(8.dp))
                    Text(AppText.edit)
                }
                TextButton(onClick = { confirmDelete = true }, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Default.Delete, null)
                    Spacer(Modifier.width(8.dp))
                    Text(AppText.delete)
                }
            }
        }
    }
    if (confirmDelete) {
        AlertDialog(
            onDismissRequest = { confirmDelete = false },
            title = { Text(AppText.deleteEmployeeTitle) },
            text = { Text("${AppText.deleteEmployeePrefix} ${employee.fullName}. ${AppText.deleteEmployeeSuffix}") },
            confirmButton = { TextButton(onClick = { confirmDelete = false; onDelete() }) { Text(AppText.delete) } },
            dismissButton = { TextButton(onClick = { confirmDelete = false }) { Text(AppText.cancel) } }
        )
    }
}

@Composable
private fun DetailSection(title: String, rows: List<Pair<String, String>>) {
    Card(shape = RoundedCornerShape(8.dp)) {
        Column(Modifier.fillMaxWidth().padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            rows.forEach { (label, value) ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(value, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f), textAlign = androidx.compose.ui.text.style.TextAlign.End)
                }
            }
        }
    }
}

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
    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Box {
                    ProfileAvatar(form.profileImagePath, form.fullName.initialsFromName(), Modifier.size(80.dp))
                    IconButton(
                        onClick = { showImageSheet = true },
                        modifier = Modifier.align(Alignment.BottomEnd).size(32.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primary)
                    ) { Icon(Icons.Default.CameraAlt, null, tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(18.dp)) }
                }
                Column {
                    Text(if (form.editingId == null) "Add Employee" else "Edit Employee", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Text("BookXpert profile details", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
        item {
            OutlinedTextField(
                value = form.fullName,
                onValueChange = { value -> onChange { it.copy(fullName = value) } },
                modifier = Modifier.fillMaxWidth().onFocusChanged { if (!it.isFocused) onTouch(FormField.FullName) },
                label = { Text("Full name") },
                isError = FormField.FullName in form.touched && errors.fullName != null,
                supportingText = { if (FormField.FullName in form.touched) errors.fullName?.let { Text(it) } },
                singleLine = true
            )
        }
        item {
            OutlinedTextField(
                value = form.email,
                onValueChange = { value -> onChange { it.copy(email = value) } },
                modifier = Modifier.fillMaxWidth().onFocusChanged { if (!it.isFocused) onTouch(FormField.Email) },
                label = { Text("Email") },
                isError = FormField.Email in form.touched && errors.email != null,
                supportingText = { if (FormField.Email in form.touched) errors.email?.let { Text(it) } },
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true
            )
        }
        item {
            OutlinedTextField(
                value = form.phone,
                onValueChange = { value -> onChange { it.copy(phone = value.filter { c -> c.isDigit() || c in "+- ()" }) } },
                modifier = Modifier.fillMaxWidth().onFocusChanged { if (!it.isFocused) onTouch(FormField.Phone) },
                label = { Text("Phone number") },
                isError = FormField.Phone in form.touched && errors.phone != null,
                supportingText = { if (FormField.Phone in form.touched) errors.phone?.let { Text(it) } },
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true
            )
        }
        item {
            OutlinedTextField(
                value = form.address,
                onValueChange = { value -> onChange { it.copy(address = value) } },
                modifier = Modifier.fillMaxWidth().onFocusChanged { if (!it.isFocused) onTouch(FormField.Address) },
                label = { Text("Address") },
                minLines = 4,
                isError = FormField.Address in form.touched && errors.address != null,
                supportingText = { if (FormField.Address in form.touched) errors.address?.let { Text(it) } }
            )
        }
        item { RadioRow("Gender", Gender.entries, form.gender, { it.label }, errors.gender) { onChange { state -> state.copy(gender = it) } } }
        item { EnumDropdown("Department", Department.entries, form.department, { it.label }, errors.department) { value -> onChange { it.copy(department = value) } } }
        item {
            Text("Skills", fontWeight = FontWeight.SemiBold)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Skill.entries.forEach { skill ->
                    FilterChip(selected = skill in form.skills, onClick = { onToggleSkill(skill) }, label = { Text(skill.label) })
                }
            }
            errors.skills?.let { Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall) }
        }
        item { EnumDropdown("Employment Type", EmploymentType.entries, form.employmentType, { it.label }, errors.employmentType) { value -> onChange { it.copy(employmentType = value) } } }
        item {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Active", Modifier.weight(1f))
                Switch(checked = form.isActive, onCheckedChange = { checked -> onChange { it.copy(isActive = checked) } })
            }
        }
        item {
            OutlinedTextField(
                value = form.joiningDateMillis?.let { formatDate(it) }.orEmpty(),
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                label = { Text("Joining Date") },
                trailingIcon = { IconButton(onClick = { showDatePicker = true }) { Icon(Icons.Default.KeyboardArrowDown, null) } },
                isError = errors.joiningDate != null,
                supportingText = { errors.joiningDate?.let { Text(it) } }
            )
        }
        item {
            OutlinedTextField(
                value = form.salary,
                onValueChange = { value -> onChange { it.copy(salary = value.filter { c -> c.isDigit() || c == '.' }) } },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Salary") },
                prefix = { Text("₹") },
                isError = errors.salary != null,
                supportingText = { errors.salary?.let { Text(it) } },
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
        }
        item {
            ResumePicker(form, onPick = picker::chooseDocument, onRemove = onRemoveResume)
        }
        item {
            Button(onClick = onSave, modifier = Modifier.fillMaxWidth().height(52.dp)) {
                Text(if (form.editingId == null) "Submit Employee" else "Update Employee")
            }
        }
    }

    if (showImageSheet) {
        ModalBottomSheet(onDismissRequest = { showImageSheet = false }, sheetState = rememberModalBottomSheetState()) {
            Text("Profile image", Modifier.padding(horizontal = 20.dp), style = MaterialTheme.typography.titleMedium)
            DropdownMenuItem(text = { Text("Take Photo") }, leadingIcon = { Icon(Icons.Default.CameraAlt, null) }, onClick = { showImageSheet = false; picker.takePhoto() })
            DropdownMenuItem(text = { Text("Choose from Gallery") }, leadingIcon = { Icon(Icons.Default.Image, null) }, onClick = { showImageSheet = false; picker.chooseFromGallery() })
            Spacer(Modifier.height(20.dp))
        }
    }

    if (showDatePicker) {
        val dateState = rememberDatePickerState(initialSelectedDateMillis = form.joiningDateMillis)
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    dateState.selectedDateMillis?.let { value -> onChange { it.copy(joiningDateMillis = value) } }
                    showDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = { TextButton(onClick = { showDatePicker = false }) { Text("Cancel") } }
        ) { DatePicker(dateState) }
    }
}

@Composable
private fun ResumePicker(form: EmployeeFormState, onPick: () -> Unit, onRemove: () -> Unit) {
    Card(shape = RoundedCornerShape(8.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)) {
        Row(Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Icon(Icons.Default.AttachFile, null)
            Column(Modifier.weight(1f)) {
                Text(form.resume?.name ?: "Resume document", fontWeight = FontWeight.SemiBold)
                Text(form.resume?.let { "${it.mimeType} • ${it.sizeLabel}" } ?: "PDF, DOC, DOCX up to 5 MB", style = MaterialTheme.typography.bodySmall)
            }
            if (form.resume == null) TextButton(onClick = onPick) { Text("Select") } else IconButton(onClick = onRemove) { Icon(Icons.Default.Close, null) }
        }
    }
}

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
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(Modifier.padding(20.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Text("Filters", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text("Department", fontWeight = FontWeight.SemiBold)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Department.entries.forEach { FilterChip(selected = it in selectedDepartments, onClick = { onDepartment(it) }, label = { Text(it.label) }) }
            }
            Text("Status", fontWeight = FontWeight.SemiBold)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ActiveFilter.entries.forEach { FilterChip(selected = activeFilter == it, onClick = { onActive(it) }, label = { Text(it.label) }) }
            }
            Text("Employment", fontWeight = FontWeight.SemiBold)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                EmploymentType.entries.forEach { FilterChip(selected = it in selectedTypes, onClick = { onType(it) }, label = { Text(it.label) }) }
            }
            Button(onClick = onClear, modifier = Modifier.fillMaxWidth()) { Text("Clear All Filters") }
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun TopEarnersScreen(topEarners: List<Employee>, topN: Int, onTopN: (Int) -> Unit) {
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Top Earners", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            IconButton(onClick = { onTopN(topN - 1) }) { Icon(Icons.Default.Remove, null) }
            Text("Top $topN", style = MaterialTheme.typography.titleMedium)
            IconButton(onClick = { onTopN(topN + 1) }) { Icon(Icons.Default.Add, null) }
        }
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(topEarners) { employee ->
                Card(shape = RoundedCornerShape(8.dp)) {
                    Row(Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("#${topEarners.indexOf(employee) + 1}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        Column(Modifier.weight(1f)) {
                            Text(employee.fullName, fontWeight = FontWeight.SemiBold)
                            Text(employee.department.label, style = MaterialTheme.typography.bodySmall)
                        }
                        Text("₹${employee.salary.toInt()}", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyState() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("No employees found", style = MaterialTheme.typography.titleMedium)
            Text("Try a different search or filter", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun <T> EnumDropdown(label: String, values: List<T>, selected: T?, labelOf: (T) -> String, error: String?, onSelected: (T) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        OutlinedTextField(
            value = selected?.let(labelOf).orEmpty(),
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            isError = error != null,
            supportingText = { error?.let { Text(it) } },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            values.forEach { value ->
                DropdownMenuItem(text = { Text(labelOf(value)) }, onClick = { onSelected(value); expanded = false })
            }
        }
    }
}

@Composable
private fun <T> RadioRow(title: String, values: List<T>, selected: T?, labelOf: (T) -> String, error: String?, onSelected: (T) -> Unit) {
    Text(title, fontWeight = FontWeight.SemiBold)
    values.forEach { value ->
        Row(
            modifier = Modifier.fillMaxWidth().clickable { onSelected(value) }.padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(selected = selected == value, onClick = { onSelected(value) })
            Text(labelOf(value))
        }
    }
    error?.let { Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall) }
}

@Composable
private fun StatusDot(active: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Box(Modifier.size(10.dp).clip(CircleShape).background(if (active) Color(0xFF2E7D32) else Color(0xFFC62828)))
        Text(if (active) "Active" else "Inactive", style = MaterialTheme.typography.bodySmall)
    }
}

private fun highlight(text: String, query: String) = buildAnnotatedString {
    val index = text.lowercase().indexOf(query.trim().lowercase())
    if (query.isBlank() || index < 0) append(text) else {
        append(text.substring(0, index))
        withStyle(SpanStyle(background = Color(0xFFFFE082), fontWeight = FontWeight.Bold)) {
            append(text.substring(index, index + query.length))
        }
        append(text.substring(index + query.length))
    }
}

private fun Employee.initials(): String = fullName.initialsFromName()

private fun String.initialsFromName(): String = trim().split(" ").filter { it.isNotBlank() }.take(2).joinToString("") { it.first().uppercase() }.ifBlank { "EP" }

private fun lightAppColors() = androidx.compose.material3.lightColorScheme(
    primary = Color(0xFF2F7D68),
    secondary = Color(0xFF6A6D35),
    tertiary = Color(0xFF9A5A35),
    background = Color(0xFFF8FAF7),
    surface = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFE7F0E9)
)

private fun darkAppColors() = androidx.compose.material3.darkColorScheme(
    primary = Color(0xFF80D8BE),
    secondary = Color(0xFFD2D88B),
    tertiary = Color(0xFFFFB789),
    background = Color(0xFF101411),
    surface = Color(0xFF171D19),
    secondaryContainer = Color(0xFF26372F)
)

private object AppText {
    const val employeesTab = "Employees"
    const val topTab = "Top"
    const val formTab = "Form"
    const val employeeProfiles = "Employee Profiles"
    const val searchPlaceholder = "Search name, email, department"
    const val filter = "Filter"
    const val sort = "Sort"
    const val edit = "Edit"
    const val delete = "Delete"
    const val cancel = "Cancel"
    const val back = "Back"
    const val employeeDetails = "Employee Details"
    const val workDetails = "Work Details"
    const val contactDetails = "Contact Details"
    const val department = "Department"
    const val employmentType = "Employment Type"
    const val joiningDate = "Joining Date"
    const val salary = "Salary"
    const val skills = "Skills"
    const val phoneNumber = "Phone Number"
    const val address = "Address"
    const val resumeDocument = "Resume Document"
    const val noDocument = "No document selected"
    const val deleteEmployeeTitle = "Delete employee?"
    const val deleteEmployeePrefix = "This removes"
    const val deleteEmployeeSuffix = "You can undo from the snackbar."
}
