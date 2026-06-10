package com.bookxpert.upasnaprojectss.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bookxpert.upasnaprojectss.domain.Department
import com.bookxpert.upasnaprojectss.domain.Employee
import com.bookxpert.upasnaprojectss.domain.EmploymentType
import com.bookxpert.upasnaprojectss.domain.Gender
import com.bookxpert.upasnaprojectss.domain.ResumeDocument
import com.bookxpert.upasnaprojectss.domain.Skill

@Entity(tableName = "employees")
data class EmployeeEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
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
    val profileImagePath: String?,
    val resumePath: String?,
    val resumeName: String?,
    val resumeSizeBytes: Long?,
    val resumeMimeType: String?,
    val createdAt: Long,
    val updatedAt: Long
)

fun EmployeeEntity.toDomain(): Employee = Employee(
    id = id,
    fullName = fullName,
    email = email,
    phone = phone,
    normalizedPhone = normalizedPhone,
    address = address,
    gender = gender,
    department = department,
    skills = skills,
    employmentType = employmentType,
    isActive = isActive,
    joiningDateMillis = joiningDateMillis,
    salary = salary,
    profileImagePath = profileImagePath,
    resume = resumePath?.let {
        ResumeDocument(
            path = it,
            name = resumeName.orEmpty(),
            sizeBytes = resumeSizeBytes ?: 0,
            mimeType = resumeMimeType.orEmpty()
        )
    },
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun Employee.toEntity(): EmployeeEntity = EmployeeEntity(
    id = id,
    fullName = fullName,
    email = email,
    phone = phone,
    normalizedPhone = normalizedPhone,
    address = address,
    gender = gender,
    department = department,
    skills = skills,
    employmentType = employmentType,
    isActive = isActive,
    joiningDateMillis = joiningDateMillis,
    salary = salary,
    profileImagePath = profileImagePath,
    resumePath = resume?.path,
    resumeName = resume?.name,
    resumeSizeBytes = resume?.sizeBytes,
    resumeMimeType = resume?.mimeType,
    createdAt = createdAt,
    updatedAt = updatedAt
)
