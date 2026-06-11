package com.bookxpert.upasnaprojectss.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bookxpert.upasnaprojectss.domain.Employee
import com.bookxpert.upasnaprojectss.platform.ProfileAvatar
import com.bookxpert.upasnaprojectss.platform.formatDate
import org.jetbrains.compose.resources.stringResource
import upasnaemployeekmp.composeapp.generated.resources.Res
import upasnaemployeekmp.composeapp.generated.resources.address
import upasnaemployeekmp.composeapp.generated.resources.back
import upasnaemployeekmp.composeapp.generated.resources.contact_details
import upasnaemployeekmp.composeapp.generated.resources.delete
import upasnaemployeekmp.composeapp.generated.resources.department
import upasnaemployeekmp.composeapp.generated.resources.edit
import upasnaemployeekmp.composeapp.generated.resources.employee_details
import upasnaemployeekmp.composeapp.generated.resources.employment_type
import upasnaemployeekmp.composeapp.generated.resources.joining_date
import upasnaemployeekmp.composeapp.generated.resources.no_document
import upasnaemployeekmp.composeapp.generated.resources.phone_number
import upasnaemployeekmp.composeapp.generated.resources.resume_document
import upasnaemployeekmp.composeapp.generated.resources.salary
import upasnaemployeekmp.composeapp.generated.resources.salary_format
import upasnaemployeekmp.composeapp.generated.resources.skills
import upasnaemployeekmp.composeapp.generated.resources.work_details

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun EmployeeDetailScreen(
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