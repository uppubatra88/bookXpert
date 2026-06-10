package com.bookxpert.upasnaprojectss

import com.bookxpert.upasnaprojectss.algorithms.DuplicateEmployeeIndex
import com.bookxpert.upasnaprojectss.algorithms.UndoDeleteStack
import com.bookxpert.upasnaprojectss.algorithms.normalizePhone
import com.bookxpert.upasnaprojectss.algorithms.topNBySalary
import com.bookxpert.upasnaprojectss.domain.Department
import com.bookxpert.upasnaprojectss.domain.Employee
import com.bookxpert.upasnaprojectss.domain.EmploymentType
import com.bookxpert.upasnaprojectss.domain.Gender
import com.bookxpert.upasnaprojectss.domain.Skill
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class EmployeeAlgorithmsTest {
    @Test fun normalizesSpacesDashesAndPlus() = assertEquals("9876543210", normalizePhone("+91 987-654-3210"))
    @Test fun removesLeadingZero() = assertEquals("9876543210", normalizePhone("09876543210"))
    @Test fun keepsPlainTenDigits() = assertEquals("9876543210", normalizePhone("9876543210"))
    @Test fun duplicateEmailIsDetected() = assertTrue(DuplicateEmployeeIndex(listOf(emp(email = "a@b.com"))).isDuplicate("a@b.com", "111").emailExists)
    @Test fun duplicatePhoneIsDetected() = assertTrue(DuplicateEmployeeIndex(listOf(emp(phone = "999"))).isDuplicate("x@y.com", "999").phoneExists)
    @Test fun uniqueEmployeePasses() = assertFalse(DuplicateEmployeeIndex(listOf(emp())).isDuplicate("new@b.com", "123").hasAny)
    @Test fun topNReturnsRequestedCount() = assertEquals(2, topNBySalary(listOf(emp(salary = 1.0), emp(salary = 3.0), emp(salary = 2.0)), 2).size)
    @Test fun topNOrdersDescending() = assertEquals(listOf(5.0, 4.0), topNBySalary(listOf(emp(salary = 4.0), emp(salary = 5.0), emp(salary = 1.0)), 2).map { it.salary })
    @Test fun topNZeroReturnsEmpty() = assertTrue(topNBySalary(listOf(emp()), 0).isEmpty())
    @Test fun undoReturnsLastDeleted() {
        val stack = UndoDeleteStack()
        stack.push(emp(fullName = "One"))
        stack.push(emp(fullName = "Two"))
        assertEquals("Two", stack.pop()?.fullName)
    }
    @Test fun undoEmptyReturnsNull() = assertEquals(null, UndoDeleteStack().pop())
}

private fun emp(
    id: Long = 1,
    fullName: String = "Alex Kumar",
    email: String = "alex@bookxpert.com",
    phone: String = "9876543210",
    salary: Double = 10.0
) = Employee(
    id = id,
    fullName = fullName,
    email = email,
    phone = phone,
    normalizedPhone = phone,
    address = "Bengaluru",
    gender = Gender.PreferNotToSay,
    department = Department.Engineering,
    skills = listOf(Skill.Kotlin),
    employmentType = EmploymentType.FullTime,
    isActive = true,
    joiningDateMillis = 1,
    salary = salary,
    createdAt = 1,
    updatedAt = 1
)
