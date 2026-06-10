package com.bookxpert.upasnaprojectss.data

import androidx.room.TypeConverter
import com.bookxpert.upasnaprojectss.domain.Department
import com.bookxpert.upasnaprojectss.domain.EmploymentType
import com.bookxpert.upasnaprojectss.domain.Gender
import com.bookxpert.upasnaprojectss.domain.Skill

class Converters {
    @TypeConverter
    fun skillsToString(value: List<Skill>): String = value.joinToString(",") { it.name }

    @TypeConverter
    fun stringToSkills(value: String): List<Skill> =
        value.split(",").filter { it.isNotBlank() }.map { Skill.valueOf(it) }

    @TypeConverter fun genderToString(value: Gender): String = value.name
    @TypeConverter fun stringToGender(value: String): Gender = Gender.valueOf(value)
    @TypeConverter fun departmentToString(value: Department): String = value.name
    @TypeConverter fun stringToDepartment(value: String): Department = Department.valueOf(value)
    @TypeConverter fun employmentToString(value: EmploymentType): String = value.name
    @TypeConverter fun stringToEmployment(value: String): EmploymentType = EmploymentType.valueOf(value)
}
