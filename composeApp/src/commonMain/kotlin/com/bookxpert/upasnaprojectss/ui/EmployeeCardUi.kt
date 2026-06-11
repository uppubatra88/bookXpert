package com.bookxpert.upasnaprojectss.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.bookxpert.upasnaprojectss.domain.Employee
import com.bookxpert.upasnaprojectss.platform.ProfileAvatar
import com.bookxpert.upasnaprojectss.platform.formatDate
import org.jetbrains.compose.resources.stringResource
import upasnaemployeekmp.composeapp.generated.resources.Res
import upasnaemployeekmp.composeapp.generated.resources.delete
import upasnaemployeekmp.composeapp.generated.resources.edit
import upasnaemployeekmp.composeapp.generated.resources.salary_format

@OptIn(ExperimentalFoundationApi::class, ExperimentalLayoutApi::class)
@Composable
fun EmployeeCard(
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

@Composable
internal fun MetaChip(label: String, isPrimary: Boolean = false) {
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