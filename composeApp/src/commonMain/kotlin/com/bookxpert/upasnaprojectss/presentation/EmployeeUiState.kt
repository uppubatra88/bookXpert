package com.bookxpert.upasnaprojectss.presentation

import com.bookxpert.upasnaprojectss.domain.Department
import com.bookxpert.upasnaprojectss.domain.Employee
import com.bookxpert.upasnaprojectss.domain.EmploymentType

enum class SortOption(val label: String) {
    NameAsc("Name A-Z"),
    NameDesc("Name Z-A"),
    JoiningNewest("Joining Newest"),
    JoiningOldest("Joining Oldest"),
    SalaryHigh("Salary High-Low"),
    SalaryLow("Salary Low-High")
}

enum class ActiveFilter(val label: String) {
    All("All"),
    Active("Active"),
    Inactive("Inactive")
}

data class EmployeeFilters(
    val departments: Set<Department> = emptySet(),
    val activeFilter: ActiveFilter = ActiveFilter.All,
    val employmentTypes: Set<EmploymentType> = emptySet()
) {
    val activeCount: Int
        get() = departments.size + employmentTypes.size + if (activeFilter == ActiveFilter.All) 0 else 1
}

data class EmployeeUiState(
    val employees: List<Employee> = emptyList(),
    val visibleEmployees: List<Employee> = emptyList(),
    val topEarners: List<Employee> = emptyList(),
    val topN: Int = 5,
    val searchQuery: String = "",
    val filters: EmployeeFilters = EmployeeFilters(),
    val sortOption: SortOption = SortOption.NameAsc,
    val selectedEmployee: Employee? = null,
    val snackbarMessage: String? = null,
    val isLoading: Boolean = true
)
