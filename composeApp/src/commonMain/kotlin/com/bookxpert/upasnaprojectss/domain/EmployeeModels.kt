package com.bookxpert.upasnaprojectss.domain

enum class Gender(val label: String) {
    Male("Male"),
    Female("Female"),
    PreferNotToSay("Prefer not to say")
}

enum class Department(val label: String) {
    Engineering("Engineering"),
    HR("HR"),
    Sales("Sales"),
    Finance("Finance"),
    Design("Design"),
    Ops("Ops")
}

enum class Skill(val label: String) {
    Kotlin("Kotlin"),
    Compose("Compose"),
    Kmp("KMP"),
    Room("Room"),
    Sql("SQL"),
    Design("Design"),
    Sales("Sales"),
    Payroll("Payroll")
}

enum class EmploymentType(val label: String) {
    FullTime("Full-Time"),
    PartTime("Part-Time"),
    Contract("Contract")
}

data class ResumeDocument(
    val path: String,
    val name: String,
    val sizeBytes: Long,
    val mimeType: String
) {
    val sizeLabel: String
        get() = when {
            sizeBytes >= 1_048_576 -> "${(sizeBytes / 1_048_576.0).round1()} MB"
            sizeBytes >= 1024 -> "${(sizeBytes / 1024.0).round1()} KB"
            else -> "$sizeBytes B"
        }
}

data class Employee(
    val id: Long = 0,
    val fullName: String,
    val email: String,
    val phone: String,
    val normalizedPhone: String,
    val address: String,
    val gender: Gender,
    val department: Department,
    val skills: List<Skill>,
    val employmentType: EmploymentType,
    val isActive: Boolean,
    val joiningDateMillis: Long,
    val salary: Double,
    val profileImagePath: String? = null,
    val resume: ResumeDocument? = null,
    val createdAt: Long,
    val updatedAt: Long
)

private fun Double.round1(): String = ((this * 10).toInt() / 10.0).toString()
