package com.bookxpert.upasnaprojectss.ui

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.bookxpert.upasnaprojectss.domain.Department
import com.bookxpert.upasnaprojectss.domain.EmployeeFormState
import com.bookxpert.upasnaprojectss.domain.EmploymentType
import com.bookxpert.upasnaprojectss.domain.FormErrors
import com.bookxpert.upasnaprojectss.domain.FormField
import com.bookxpert.upasnaprojectss.domain.Gender
import com.bookxpert.upasnaprojectss.domain.Skill
import com.bookxpert.upasnaprojectss.platform.ProfileAvatar
import com.bookxpert.upasnaprojectss.platform.formatDate
import com.bookxpert.upasnaprojectss.platform.rememberPlatformFilePicker
import org.jetbrains.compose.resources.stringResource
import upasnaemployeekmp.composeapp.generated.resources.Res
import upasnaemployeekmp.composeapp.generated.resources.active
import upasnaemployeekmp.composeapp.generated.resources.add_employee
import upasnaemployeekmp.composeapp.generated.resources.address
import upasnaemployeekmp.composeapp.generated.resources.bookxpert_profile_details
import upasnaemployeekmp.composeapp.generated.resources.cancel
import upasnaemployeekmp.composeapp.generated.resources.choose_gallery
import upasnaemployeekmp.composeapp.generated.resources.department
import upasnaemployeekmp.composeapp.generated.resources.edit_employee
import upasnaemployeekmp.composeapp.generated.resources.email
import upasnaemployeekmp.composeapp.generated.resources.employment_type
import upasnaemployeekmp.composeapp.generated.resources.full_name
import upasnaemployeekmp.composeapp.generated.resources.gender
import upasnaemployeekmp.composeapp.generated.resources.joining_date
import upasnaemployeekmp.composeapp.generated.resources.ok
import upasnaemployeekmp.composeapp.generated.resources.phone_number_label
import upasnaemployeekmp.composeapp.generated.resources.profile_image
import upasnaemployeekmp.composeapp.generated.resources.salary
import upasnaemployeekmp.composeapp.generated.resources.skills
import upasnaemployeekmp.composeapp.generated.resources.submit_employee
import upasnaemployeekmp.composeapp.generated.resources.take_photo
import upasnaemployeekmp.composeapp.generated.resources.update_employee

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
internal fun EmployeeFormScreen(
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

        item { RadioRow(stringResource(Res.string.gender), Gender.entries, form.gender, { it.label }, errors.gender) { onChange { s -> s.copy(gender = it) } } }

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

        item { ResumePicker(form, onPick = picker::chooseDocument, onRemove = onRemoveResume) }

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