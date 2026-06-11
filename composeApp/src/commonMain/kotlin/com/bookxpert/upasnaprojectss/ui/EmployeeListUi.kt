package com.bookxpert.upasnaprojectss.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.unit.sp
import com.bookxpert.upasnaprojectss.domain.Employee
import com.bookxpert.upasnaprojectss.platform.ProfileAvatar
import com.bookxpert.upasnaprojectss.platform.formatDate
import com.bookxpert.upasnaprojectss.presentation.SortOption
import org.jetbrains.compose.resources.stringResource
import upasnaemployeekmp.composeapp.generated.resources.Res
import upasnaemployeekmp.composeapp.generated.resources.delete
import upasnaemployeekmp.composeapp.generated.resources.edit
import upasnaemployeekmp.composeapp.generated.resources.employee_profiles
import upasnaemployeekmp.composeapp.generated.resources.filter
import upasnaemployeekmp.composeapp.generated.resources.salary_format
import upasnaemployeekmp.composeapp.generated.resources.search_placeholder
import upasnaemployeekmp.composeapp.generated.resources.sort

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun EmployeeListScreen(
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

