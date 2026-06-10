package com.bookxpert.upasnaprojectss.domain

data class EmployeeFormState(
    val editingId: Long? = null,
    val fullName: String = "",
    val email: String = "",
    val phone: String = "",
    val address: String = "",
    val gender: Gender? = null,
    val department: Department? = null,
    val skills: Set<Skill> = emptySet(),
    val employmentType: EmploymentType? = null,
    val isActive: Boolean = true,
    val joiningDateMillis: Long? = null,
    val salary: String = "",
    val profileImagePath: String? = null,
    val resume: ResumeDocument? = null,
    val touched: Set<FormField> = emptySet(),
    val duplicateEmailError: String? = null,
    val duplicatePhoneError: String? = null
)

enum class FormField {
    FullName,
    Email,
    Phone,
    Address,
    Gender,
    Department,
    Skills,
    EmploymentType,
    JoiningDate,
    Salary
}

data class FormErrors(
    val fullName: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val address: String? = null,
    val gender: String? = null,
    val department: String? = null,
    val skills: String? = null,
    val employmentType: String? = null,
    val joiningDate: String? = null,
    val salary: String? = null
) {
    val isValid: Boolean
        get() = listOf(
            fullName, email, phone, address, gender, department, skills,
            employmentType, joiningDate, salary
        ).all { it == null }
}

fun EmployeeFormState.toEmployee(now: Long, existingCreatedAt: Long? = null): Employee {
    val cleanPhone = com.bookxpert.upasnaprojectss.algorithms.normalizePhone(phone)
    return Employee(
        id = editingId ?: 0,
        fullName = fullName.trim(),
        email = email.trim().lowercase(),
        phone = phone.trim(),
        normalizedPhone = cleanPhone,
        address = address.trim(),
        gender = requireNotNull(gender),
        department = requireNotNull(department),
        skills = skills.toList(),
        employmentType = requireNotNull(employmentType),
        isActive = isActive,
        joiningDateMillis = requireNotNull(joiningDateMillis),
        salary = salary.toDouble(),
        profileImagePath = profileImagePath,
        resume = resume,
        createdAt = existingCreatedAt ?: now,
        updatedAt = now
    )
}
