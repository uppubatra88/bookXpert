package com.bookxpert.upasnaprojectss.algorithms

import com.bookxpert.upasnaprojectss.domain.Employee

fun normalizePhone(raw: String): String =
    raw.filter { it.isDigit() }
        .removePrefix("91")
        .removePrefix("0")

class DuplicateEmployeeIndex(employees: List<Employee> = emptyList()) {
    private val emails = employees.mapTo(mutableSetOf()) { it.email.lowercase() }
    private val phones = employees.mapTo(mutableSetOf()) { it.normalizedPhone }

    /**
     * Checks for duplicate email or normalised phone before persist.
     * Time complexity: O(1) average for HashSet contains().
     * Space complexity: O(n), where n is the number of indexed employees.
     */
    fun isDuplicate(email: String, normalizedPhone: String, ignoringId: Long? = null, employees: List<Employee> = emptyList()): DuplicateResult {
        val cleanEmail = email.lowercase()
        if (ignoringId != null && employees.isNotEmpty()) {
            val duplicate = employees.firstOrNull {
                it.id != ignoringId && (it.email == cleanEmail || it.normalizedPhone == normalizedPhone)
            }
            return DuplicateResult(
                emailExists = duplicate?.email == cleanEmail,
                phoneExists = duplicate?.normalizedPhone == normalizedPhone
            )
        }
        return DuplicateResult(cleanEmail in emails, normalizedPhone in phones)
    }

    fun add(employee: Employee) {
        emails += employee.email.lowercase()
        phones += employee.normalizedPhone
    }

    fun remove(employee: Employee) {
        emails -= employee.email.lowercase()
        phones -= employee.normalizedPhone
    }
}

data class DuplicateResult(val emailExists: Boolean, val phoneExists: Boolean) {
    val hasAny: Boolean get() = emailExists || phoneExists
}

/**
 * Returns the top [n] employees by salary using a min-heap of size [n].
 * Time complexity: O(m log n), where m is total employees and n is top count.
 * Space complexity: O(n), because the heap holds at most [n] employees.
 *
 * A fixed min-heap beats sorting when n is small because full sort costs
 * O(m log m), while the heap only orders the best n candidates.
 */
fun topNBySalary(employees: List<Employee>, n: Int = 5): List<Employee> {
    if (n <= 0) return emptyList()
    val heap = MinSalaryHeap(n)
    employees.forEach { heap.offer(it) }
    return heap.toList().sortedByDescending { it.salary }
}

/**
 * Stores deleted employees for Undo.
 * Time complexity: O(1) push and O(1) pop.
 * Space complexity: O(k), where k is max undo depth.
 */
class UndoDeleteStack(private val maxDepth: Int = 10) {
    private val stack = ArrayDeque<Employee>()

    fun push(employee: Employee) {
        if (stack.size == maxDepth) stack.removeFirst()
        stack.addLast(employee)
    }

    fun pop(): Employee? = stack.removeLastOrNull()
}

private class MinSalaryHeap(private val capacity: Int) {
    private val items = mutableListOf<Employee>()

    fun offer(employee: Employee) {
        if (items.size < capacity) {
            items += employee
            siftUp(items.lastIndex)
            return
        }
        if (items.firstOrNull()?.salary != null && employee.salary > items[0].salary) {
            items[0] = employee
            siftDown(0)
        }
    }

    fun toList(): List<Employee> = items.toList()

    private fun siftUp(start: Int) {
        var index = start
        while (index > 0) {
            val parent = (index - 1) / 2
            if (items[parent].salary <= items[index].salary) break
            items.swap(parent, index)
            index = parent
        }
    }

    private fun siftDown(start: Int) {
        var index = start
        while (true) {
            val left = index * 2 + 1
            val right = left + 1
            var smallest = index
            if (left < items.size && items[left].salary < items[smallest].salary) smallest = left
            if (right < items.size && items[right].salary < items[smallest].salary) smallest = right
            if (smallest == index) return
            items.swap(index, smallest)
            index = smallest
        }
    }
}

private fun MutableList<Employee>.swap(a: Int, b: Int) {
    val old = this[a]
    this[a] = this[b]
    this[b] = old
}
