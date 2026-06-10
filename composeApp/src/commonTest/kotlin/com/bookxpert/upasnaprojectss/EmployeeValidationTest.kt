package com.bookxpert.upasnaprojectss

import com.bookxpert.upasnaprojectss.domain.Department
import com.bookxpert.upasnaprojectss.domain.EmployeeFormState
import com.bookxpert.upasnaprojectss.domain.EmploymentType
import com.bookxpert.upasnaprojectss.domain.Gender
import com.bookxpert.upasnaprojectss.domain.Skill
import com.bookxpert.upasnaprojectss.validation.validateEmployeeForm
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class EmployeeValidationTest {
    @Test fun validFormPasses() = assertTrue(validateEmployeeForm(validForm(), 2_000).isValid)
    @Test fun nameRequiresThreeChars() = assertNotNull(validateEmployeeForm(validForm().copy(fullName = "Al"), 2_000).fullName)
    @Test fun emailRequiresValidFormat() = assertNotNull(validateEmployeeForm(validForm().copy(email = "bad"), 2_000).email)
    @Test fun phoneRequiresTenDigits() = assertNotNull(validateEmployeeForm(validForm().copy(phone = "123"), 2_000).phone)
    @Test fun futureDateFails() = assertNotNull(validateEmployeeForm(validForm().copy(joiningDateMillis = 3_000), 2_000).joiningDate)
    @Test fun salaryMustBePositive() = assertNotNull(validateEmployeeForm(validForm().copy(salary = "0"), 2_000).salary)
    @Test fun skillsRequired() = assertFalse(validateEmployeeForm(validForm().copy(skills = emptySet()), 2_000).isValid)
}

private fun validForm() = EmployeeFormState(
    fullName = "Alex Kumar",
    email = "alex@bookxpert.com",
    phone = "9876543210",
    address = "Bengaluru Office",
    gender = Gender.Male,
    department = Department.Engineering,
    skills = setOf(Skill.Kotlin),
    employmentType = EmploymentType.FullTime,
    joiningDateMillis = 1_000,
    salary = "50000"
)
