package com.bookxpert.upasnaprojectss.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bookxpert.upasnaprojectss.domain.Department
import com.bookxpert.upasnaprojectss.domain.EmploymentType
import com.bookxpert.upasnaprojectss.presentation.ActiveFilter
import org.jetbrains.compose.resources.stringResource
import upasnaemployeekmp.composeapp.generated.resources.Res
import upasnaemployeekmp.composeapp.generated.resources.clear_all_filters
import upasnaemployeekmp.composeapp.generated.resources.department
import upasnaemployeekmp.composeapp.generated.resources.employment
import upasnaemployeekmp.composeapp.generated.resources.filters
import upasnaemployeekmp.composeapp.generated.resources.status

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
internal fun FilterSheet(
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