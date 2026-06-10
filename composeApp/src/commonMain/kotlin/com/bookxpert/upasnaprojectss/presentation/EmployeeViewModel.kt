package com.bookxpert.upasnaprojectss.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bookxpert.upasnaprojectss.algorithms.DuplicateEmployeeIndex
import com.bookxpert.upasnaprojectss.algorithms.UndoDeleteStack
import com.bookxpert.upasnaprojectss.algorithms.normalizePhone
import com.bookxpert.upasnaprojectss.algorithms.topNBySalary
import com.bookxpert.upasnaprojectss.data.EmployeeRepository
import com.bookxpert.upasnaprojectss.domain.Department
import com.bookxpert.upasnaprojectss.domain.Employee
import com.bookxpert.upasnaprojectss.domain.EmployeeFormState
import com.bookxpert.upasnaprojectss.domain.EmploymentType
import com.bookxpert.upasnaprojectss.domain.FormField
import com.bookxpert.upasnaprojectss.domain.ResumeDocument
import com.bookxpert.upasnaprojectss.domain.Skill
import com.bookxpert.upasnaprojectss.domain.toEmployee
import com.bookxpert.upasnaprojectss.platform.currentTimeMillis
import com.bookxpert.upasnaprojectss.validation.validateEmployeeForm
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class EmployeeViewModel(private val repository: EmployeeRepository) : ViewModel() {
    private val allEmployees = MutableStateFlow<List<Employee>>(emptyList())
    private val searchQuery = MutableStateFlow("")
    private val filters = MutableStateFlow(EmployeeFilters())
    private val sortOption = MutableStateFlow(SortOption.NameAsc)
    private val topN = MutableStateFlow(5)
    private val snackbar = MutableStateFlow<String?>(null)
    private val undoStack = UndoDeleteStack()
    private var duplicateIndex = DuplicateEmployeeIndex()

    val form = MutableStateFlow(EmployeeFormState(joiningDateMillis = currentTimeMillis()))

    private val debouncedSearchQuery = searchQuery.debounce(250)

    private val listInputs = combine(
        searchQuery,
        debouncedSearchQuery,
        filters,
        sortOption,
        topN
    ) { rawQuery, debouncedQuery, selectedFilters, selectedSort, selectedTopN ->
        ListInputs(rawQuery, debouncedQuery, selectedFilters, selectedSort, selectedTopN)
    }

    val uiState = combine(
        allEmployees,
        listInputs,
        snackbar
    ) { employees, inputs, message ->
        val displayTopN = if (employees.isEmpty()) inputs.topN else inputs.topN.coerceAtMost(employees.size)
        val visible = employees
            .filter { employee -> employee.matchesQuery(inputs.filterQuery) }
            .filter { employee -> employee.matchesFilters(inputs.filters) }
            .sortedWith(inputs.sortOption.comparator())
        EmployeeUiState(
            employees = employees,
            visibleEmployees = visible,
            topEarners = topNBySalary(employees, displayTopN),
            topN = displayTopN,
            searchQuery = inputs.rawQuery,
            filters = inputs.filters,
            sortOption = inputs.sortOption,
            snackbarMessage = message,
            isLoading = false
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), EmployeeUiState())

    init {
        viewModelScope.launch {
            repository.employees.collect { employees ->
                allEmployees.value = employees
                duplicateIndex = DuplicateEmployeeIndex(employees)
            }
        }
    }

    fun search(value: String) {
        searchQuery.value = value
    }

    fun setSort(option: SortOption) {
        sortOption.value = option
    }

    fun toggleDepartment(department: Department) {
        filters.value = filters.value.copy(departments = filters.value.departments.toggle(department))
    }

    fun toggleEmploymentType(type: EmploymentType) {
        filters.value = filters.value.copy(employmentTypes = filters.value.employmentTypes.toggle(type))
    }

    fun setActiveFilter(value: ActiveFilter) {
        filters.value = filters.value.copy(activeFilter = value)
    }

    fun clearFilters() {
        filters.value = EmployeeFilters()
    }

    fun setTopN(value: Int) {
        val employeeCount = allEmployees.value.size
        val requested = value.coerceIn(1, 10)
        if (employeeCount == 0) {
            topN.value = 1
            showMessage("No employee found")
            return
        }
        if (requested > employeeCount) {
            topN.value = employeeCount
            showMessage("No employee found")
            return
        }
        topN.value = requested
    }

    fun edit(employee: Employee) {
        form.value = EmployeeFormState(
            editingId = employee.id,
            fullName = employee.fullName,
            email = employee.email,
            phone = employee.phone,
            address = employee.address,
            gender = employee.gender,
            department = employee.department,
            skills = employee.skills.toSet(),
            employmentType = employee.employmentType,
            isActive = employee.isActive,
            joiningDateMillis = employee.joiningDateMillis,
            salary = employee.salary.toString(),
            profileImagePath = employee.profileImagePath,
            resume = employee.resume
        )
    }

    fun newEmployee() {
        form.value = EmployeeFormState(joiningDateMillis = currentTimeMillis())
    }

    fun updateForm(reducer: (EmployeeFormState) -> EmployeeFormState) {
        form.value = reducer(form.value).copy(duplicateEmailError = null, duplicatePhoneError = null)
    }

    fun touch(field: FormField) {
        form.value = form.value.copy(touched = form.value.touched + field)
    }

    fun toggleSkill(skill: Skill) {
        updateForm {
            it.copy(skills = if (skill in it.skills) it.skills - skill else it.skills + skill)
        }
    }

    fun setProfileImage(path: String) {
        updateForm { it.copy(profileImagePath = path) }
    }

    fun setResume(document: ResumeDocument) {
        if (document.sizeBytes > 5 * 1024 * 1024) {
            showMessage("Resume must be 5 MB or smaller")
        } else {
            updateForm { it.copy(resume = document) }
        }
    }

    fun removeResume() {
        updateForm { it.copy(resume = null) }
    }

    fun save(onSaved: () -> Unit) {
        val current = form.value
        val validation = validateEmployeeForm(current, currentTimeMillis())
        if (!validation.isValid) {
            form.value = current.copy(touched = FormField.entries.toSet())
            return
        }
        val normalizedPhone = normalizePhone(current.phone)
        val duplicate = duplicateIndex.isDuplicate(
            current.email.trim().lowercase(),
            normalizedPhone,
            current.editingId,
            allEmployees.value
        )
        if (duplicate.hasAny) {
            form.value = current.copy(
                duplicateEmailError = if (duplicate.emailExists) "Email already exists" else null,
                duplicatePhoneError = if (duplicate.phoneExists) "Phone already exists" else null,
                touched = current.touched + FormField.Email + FormField.Phone
            )
            return
        }

        viewModelScope.launch {
            val existing = current.editingId?.let { repository.getById(it) }
            repository.upsert(current.toEmployee(currentTimeMillis(), existing?.createdAt))
            showMessage(if (current.editingId == null) "Employee added" else "Employee updated")
            newEmployee()
            onSaved()
        }
    }

    fun delete(employee: Employee) {
        viewModelScope.launch {
            repository.delete(employee)
            duplicateIndex.remove(employee)
            undoStack.push(employee)
            showMessage("Employee deleted. Tap Undo to restore.")
        }
    }

    fun undoDelete() {
        val employee = undoStack.pop() ?: return
        viewModelScope.launch {
            repository.upsert(employee.copy(id = 0, createdAt = currentTimeMillis(), updatedAt = currentTimeMillis()))
            showMessage("Employee restored")
        }
    }

    fun showMessage(message: String?) {
        snackbar.value = message
    }
}

private data class ListInputs(
    val rawQuery: String,
    val filterQuery: String,
    val filters: EmployeeFilters,
    val sortOption: SortOption,
    val topN: Int
)

private fun Employee.matchesQuery(query: String): Boolean {
    val q = query.trim().lowercase()
    return q.isBlank() ||
        fullName.lowercase().contains(q) ||
        email.lowercase().contains(q) ||
        department.label.lowercase().contains(q)
}

private fun Employee.matchesFilters(filters: EmployeeFilters): Boolean {
    val departmentOk = filters.departments.isEmpty() || department in filters.departments
    val employmentOk = filters.employmentTypes.isEmpty() || employmentType in filters.employmentTypes
    val activeOk = when (filters.activeFilter) {
        ActiveFilter.All -> true
        ActiveFilter.Active -> isActive
        ActiveFilter.Inactive -> !isActive
    }
    return departmentOk && employmentOk && activeOk
}

private fun SortOption.comparator(): Comparator<Employee> = when (this) {
    SortOption.NameAsc -> compareBy { it.fullName.lowercase() }
    SortOption.NameDesc -> compareByDescending { it.fullName.lowercase() }
    SortOption.JoiningNewest -> compareByDescending { it.joiningDateMillis }
    SortOption.JoiningOldest -> compareBy { it.joiningDateMillis }
    SortOption.SalaryHigh -> compareByDescending { it.salary }
    SortOption.SalaryLow -> compareBy { it.salary }
}

private fun <T> Set<T>.toggle(value: T): Set<T> = if (value in this) this - value else this + value
