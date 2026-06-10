package com.bookxpert.upasnaprojectss.data

import com.bookxpert.upasnaprojectss.domain.Employee
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class EmployeeRepository(private val dao: EmployeeDao) {
    val employees: Flow<List<Employee>> = dao.observeEmployees().map { rows -> rows.map { it.toDomain() } }

    suspend fun getById(id: Long): Employee? = withContext(Dispatchers.IO) {
        dao.getById(id)?.toDomain()
    }

    suspend fun upsert(employee: Employee): Long = withContext(Dispatchers.IO) {
        if (employee.id == 0L) dao.insert(employee.toEntity()) else {
            dao.update(employee.toEntity())
            employee.id
        }
    }

    suspend fun delete(employee: Employee) = withContext(Dispatchers.IO) {
        dao.deleteById(employee.id)
    }
}
