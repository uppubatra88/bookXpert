package com.bookxpert.upasnaprojectss.validation

import com.bookxpert.upasnaprojectss.algorithms.normalizePhone
import com.bookxpert.upasnaprojectss.domain.EmployeeFormState
import com.bookxpert.upasnaprojectss.domain.FormErrors

private val emailRegex = Regex("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$", RegexOption.IGNORE_CASE)

fun validateEmployeeForm(form: EmployeeFormState, todayMillis: Long): FormErrors {
    val phone = normalizePhone(form.phone)
    val salary = form.salary.toDoubleOrNull()
    return FormErrors(
        fullName = when {
            form.fullName.trim().length < 3 -> "Full name must be at least 3 characters"
            else -> null
        },
        email = form.duplicateEmailError ?: when {
            form.email.isBlank() -> "Email is required"
            !emailRegex.matches(form.email.trim()) -> "Enter a valid email"
            else -> null
        },
        phone = form.duplicatePhoneError ?: when {
            phone.length != 10 -> "Phone number must be 10 digits"
            else -> null
        },
        address = when {
            form.address.trim().length < 6 -> "Address must be at least 6 characters"
            else -> null
        },
        gender = if (form.gender == null) "Select gender" else null,
        department = if (form.department == null) "Select department" else null,
        skills = if (form.skills.isEmpty()) "Choose at least one skill" else null,
        employmentType = if (form.employmentType == null) "Select employment type" else null,
        joiningDate = when {
            form.joiningDateMillis == null -> "Select joining date"
            form.joiningDateMillis > todayMillis -> "Joining date cannot be in the future"
            else -> null
        },
        salary = when {
            form.salary.isBlank() -> "Salary is required"
            salary == null || salary <= 0.0 -> "Salary must be a positive number"
            else -> null
        }
    )
}
