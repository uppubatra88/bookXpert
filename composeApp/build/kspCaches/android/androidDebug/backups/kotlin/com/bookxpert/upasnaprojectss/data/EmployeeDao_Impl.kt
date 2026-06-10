package com.bookxpert.upasnaprojectss.`data`

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.bookxpert.upasnaprojectss.domain.Department
import com.bookxpert.upasnaprojectss.domain.EmploymentType
import com.bookxpert.upasnaprojectss.domain.Gender
import com.bookxpert.upasnaprojectss.domain.Skill
import javax.`annotation`.processing.Generated
import kotlin.Boolean
import kotlin.Double
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class EmployeeDao_Impl(
  __db: RoomDatabase,
) : EmployeeDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfEmployeeEntity: EntityInsertAdapter<EmployeeEntity>

  private val __converters: Converters = Converters()

  private val __deleteAdapterOfEmployeeEntity: EntityDeleteOrUpdateAdapter<EmployeeEntity>

  private val __updateAdapterOfEmployeeEntity: EntityDeleteOrUpdateAdapter<EmployeeEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfEmployeeEntity = object : EntityInsertAdapter<EmployeeEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `employees` (`id`,`fullName`,`email`,`phone`,`normalizedPhone`,`address`,`gender`,`department`,`skills`,`employmentType`,`isActive`,`joiningDateMillis`,`salary`,`profileImagePath`,`resumePath`,`resumeName`,`resumeSizeBytes`,`resumeMimeType`,`createdAt`,`updatedAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: EmployeeEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.fullName)
        statement.bindText(3, entity.email)
        statement.bindText(4, entity.phone)
        statement.bindText(5, entity.normalizedPhone)
        statement.bindText(6, entity.address)
        val _tmp: String = __converters.genderToString(entity.gender)
        statement.bindText(7, _tmp)
        val _tmp_1: String = __converters.departmentToString(entity.department)
        statement.bindText(8, _tmp_1)
        val _tmp_2: String = __converters.skillsToString(entity.skills)
        statement.bindText(9, _tmp_2)
        val _tmp_3: String = __converters.employmentToString(entity.employmentType)
        statement.bindText(10, _tmp_3)
        val _tmp_4: Int = if (entity.isActive) 1 else 0
        statement.bindLong(11, _tmp_4.toLong())
        statement.bindLong(12, entity.joiningDateMillis)
        statement.bindDouble(13, entity.salary)
        val _tmpProfileImagePath: String? = entity.profileImagePath
        if (_tmpProfileImagePath == null) {
          statement.bindNull(14)
        } else {
          statement.bindText(14, _tmpProfileImagePath)
        }
        val _tmpResumePath: String? = entity.resumePath
        if (_tmpResumePath == null) {
          statement.bindNull(15)
        } else {
          statement.bindText(15, _tmpResumePath)
        }
        val _tmpResumeName: String? = entity.resumeName
        if (_tmpResumeName == null) {
          statement.bindNull(16)
        } else {
          statement.bindText(16, _tmpResumeName)
        }
        val _tmpResumeSizeBytes: Long? = entity.resumeSizeBytes
        if (_tmpResumeSizeBytes == null) {
          statement.bindNull(17)
        } else {
          statement.bindLong(17, _tmpResumeSizeBytes)
        }
        val _tmpResumeMimeType: String? = entity.resumeMimeType
        if (_tmpResumeMimeType == null) {
          statement.bindNull(18)
        } else {
          statement.bindText(18, _tmpResumeMimeType)
        }
        statement.bindLong(19, entity.createdAt)
        statement.bindLong(20, entity.updatedAt)
      }
    }
    this.__deleteAdapterOfEmployeeEntity = object : EntityDeleteOrUpdateAdapter<EmployeeEntity>() {
      protected override fun createQuery(): String = "DELETE FROM `employees` WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: EmployeeEntity) {
        statement.bindLong(1, entity.id)
      }
    }
    this.__updateAdapterOfEmployeeEntity = object : EntityDeleteOrUpdateAdapter<EmployeeEntity>() {
      protected override fun createQuery(): String =
          "UPDATE OR ABORT `employees` SET `id` = ?,`fullName` = ?,`email` = ?,`phone` = ?,`normalizedPhone` = ?,`address` = ?,`gender` = ?,`department` = ?,`skills` = ?,`employmentType` = ?,`isActive` = ?,`joiningDateMillis` = ?,`salary` = ?,`profileImagePath` = ?,`resumePath` = ?,`resumeName` = ?,`resumeSizeBytes` = ?,`resumeMimeType` = ?,`createdAt` = ?,`updatedAt` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: EmployeeEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.fullName)
        statement.bindText(3, entity.email)
        statement.bindText(4, entity.phone)
        statement.bindText(5, entity.normalizedPhone)
        statement.bindText(6, entity.address)
        val _tmp: String = __converters.genderToString(entity.gender)
        statement.bindText(7, _tmp)
        val _tmp_1: String = __converters.departmentToString(entity.department)
        statement.bindText(8, _tmp_1)
        val _tmp_2: String = __converters.skillsToString(entity.skills)
        statement.bindText(9, _tmp_2)
        val _tmp_3: String = __converters.employmentToString(entity.employmentType)
        statement.bindText(10, _tmp_3)
        val _tmp_4: Int = if (entity.isActive) 1 else 0
        statement.bindLong(11, _tmp_4.toLong())
        statement.bindLong(12, entity.joiningDateMillis)
        statement.bindDouble(13, entity.salary)
        val _tmpProfileImagePath: String? = entity.profileImagePath
        if (_tmpProfileImagePath == null) {
          statement.bindNull(14)
        } else {
          statement.bindText(14, _tmpProfileImagePath)
        }
        val _tmpResumePath: String? = entity.resumePath
        if (_tmpResumePath == null) {
          statement.bindNull(15)
        } else {
          statement.bindText(15, _tmpResumePath)
        }
        val _tmpResumeName: String? = entity.resumeName
        if (_tmpResumeName == null) {
          statement.bindNull(16)
        } else {
          statement.bindText(16, _tmpResumeName)
        }
        val _tmpResumeSizeBytes: Long? = entity.resumeSizeBytes
        if (_tmpResumeSizeBytes == null) {
          statement.bindNull(17)
        } else {
          statement.bindLong(17, _tmpResumeSizeBytes)
        }
        val _tmpResumeMimeType: String? = entity.resumeMimeType
        if (_tmpResumeMimeType == null) {
          statement.bindNull(18)
        } else {
          statement.bindText(18, _tmpResumeMimeType)
        }
        statement.bindLong(19, entity.createdAt)
        statement.bindLong(20, entity.updatedAt)
        statement.bindLong(21, entity.id)
      }
    }
  }

  public override suspend fun insert(employee: EmployeeEntity): Long = performSuspending(__db,
      false, true) { _connection ->
    val _result: Long = __insertAdapterOfEmployeeEntity.insertAndReturnId(_connection, employee)
    _result
  }

  public override suspend fun delete(employee: EmployeeEntity): Unit = performSuspending(__db,
      false, true) { _connection ->
    __deleteAdapterOfEmployeeEntity.handle(_connection, employee)
  }

  public override suspend fun update(employee: EmployeeEntity): Unit = performSuspending(__db,
      false, true) { _connection ->
    __updateAdapterOfEmployeeEntity.handle(_connection, employee)
  }

  public override fun observeEmployees(): Flow<List<EmployeeEntity>> {
    val _sql: String = "SELECT * FROM employees ORDER BY updatedAt DESC"
    return createFlow(__db, false, arrayOf("employees")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfFullName: Int = getColumnIndexOrThrow(_stmt, "fullName")
        val _columnIndexOfEmail: Int = getColumnIndexOrThrow(_stmt, "email")
        val _columnIndexOfPhone: Int = getColumnIndexOrThrow(_stmt, "phone")
        val _columnIndexOfNormalizedPhone: Int = getColumnIndexOrThrow(_stmt, "normalizedPhone")
        val _columnIndexOfAddress: Int = getColumnIndexOrThrow(_stmt, "address")
        val _columnIndexOfGender: Int = getColumnIndexOrThrow(_stmt, "gender")
        val _columnIndexOfDepartment: Int = getColumnIndexOrThrow(_stmt, "department")
        val _columnIndexOfSkills: Int = getColumnIndexOrThrow(_stmt, "skills")
        val _columnIndexOfEmploymentType: Int = getColumnIndexOrThrow(_stmt, "employmentType")
        val _columnIndexOfIsActive: Int = getColumnIndexOrThrow(_stmt, "isActive")
        val _columnIndexOfJoiningDateMillis: Int = getColumnIndexOrThrow(_stmt, "joiningDateMillis")
        val _columnIndexOfSalary: Int = getColumnIndexOrThrow(_stmt, "salary")
        val _columnIndexOfProfileImagePath: Int = getColumnIndexOrThrow(_stmt, "profileImagePath")
        val _columnIndexOfResumePath: Int = getColumnIndexOrThrow(_stmt, "resumePath")
        val _columnIndexOfResumeName: Int = getColumnIndexOrThrow(_stmt, "resumeName")
        val _columnIndexOfResumeSizeBytes: Int = getColumnIndexOrThrow(_stmt, "resumeSizeBytes")
        val _columnIndexOfResumeMimeType: Int = getColumnIndexOrThrow(_stmt, "resumeMimeType")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _result: MutableList<EmployeeEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: EmployeeEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpFullName: String
          _tmpFullName = _stmt.getText(_columnIndexOfFullName)
          val _tmpEmail: String
          _tmpEmail = _stmt.getText(_columnIndexOfEmail)
          val _tmpPhone: String
          _tmpPhone = _stmt.getText(_columnIndexOfPhone)
          val _tmpNormalizedPhone: String
          _tmpNormalizedPhone = _stmt.getText(_columnIndexOfNormalizedPhone)
          val _tmpAddress: String
          _tmpAddress = _stmt.getText(_columnIndexOfAddress)
          val _tmpGender: Gender
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfGender)
          _tmpGender = __converters.stringToGender(_tmp)
          val _tmpDepartment: Department
          val _tmp_1: String
          _tmp_1 = _stmt.getText(_columnIndexOfDepartment)
          _tmpDepartment = __converters.stringToDepartment(_tmp_1)
          val _tmpSkills: List<Skill>
          val _tmp_2: String
          _tmp_2 = _stmt.getText(_columnIndexOfSkills)
          _tmpSkills = __converters.stringToSkills(_tmp_2)
          val _tmpEmploymentType: EmploymentType
          val _tmp_3: String
          _tmp_3 = _stmt.getText(_columnIndexOfEmploymentType)
          _tmpEmploymentType = __converters.stringToEmployment(_tmp_3)
          val _tmpIsActive: Boolean
          val _tmp_4: Int
          _tmp_4 = _stmt.getLong(_columnIndexOfIsActive).toInt()
          _tmpIsActive = _tmp_4 != 0
          val _tmpJoiningDateMillis: Long
          _tmpJoiningDateMillis = _stmt.getLong(_columnIndexOfJoiningDateMillis)
          val _tmpSalary: Double
          _tmpSalary = _stmt.getDouble(_columnIndexOfSalary)
          val _tmpProfileImagePath: String?
          if (_stmt.isNull(_columnIndexOfProfileImagePath)) {
            _tmpProfileImagePath = null
          } else {
            _tmpProfileImagePath = _stmt.getText(_columnIndexOfProfileImagePath)
          }
          val _tmpResumePath: String?
          if (_stmt.isNull(_columnIndexOfResumePath)) {
            _tmpResumePath = null
          } else {
            _tmpResumePath = _stmt.getText(_columnIndexOfResumePath)
          }
          val _tmpResumeName: String?
          if (_stmt.isNull(_columnIndexOfResumeName)) {
            _tmpResumeName = null
          } else {
            _tmpResumeName = _stmt.getText(_columnIndexOfResumeName)
          }
          val _tmpResumeSizeBytes: Long?
          if (_stmt.isNull(_columnIndexOfResumeSizeBytes)) {
            _tmpResumeSizeBytes = null
          } else {
            _tmpResumeSizeBytes = _stmt.getLong(_columnIndexOfResumeSizeBytes)
          }
          val _tmpResumeMimeType: String?
          if (_stmt.isNull(_columnIndexOfResumeMimeType)) {
            _tmpResumeMimeType = null
          } else {
            _tmpResumeMimeType = _stmt.getText(_columnIndexOfResumeMimeType)
          }
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          _item =
              EmployeeEntity(_tmpId,_tmpFullName,_tmpEmail,_tmpPhone,_tmpNormalizedPhone,_tmpAddress,_tmpGender,_tmpDepartment,_tmpSkills,_tmpEmploymentType,_tmpIsActive,_tmpJoiningDateMillis,_tmpSalary,_tmpProfileImagePath,_tmpResumePath,_tmpResumeName,_tmpResumeSizeBytes,_tmpResumeMimeType,_tmpCreatedAt,_tmpUpdatedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getById(id: Long): EmployeeEntity? {
    val _sql: String = "SELECT * FROM employees WHERE id = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfFullName: Int = getColumnIndexOrThrow(_stmt, "fullName")
        val _columnIndexOfEmail: Int = getColumnIndexOrThrow(_stmt, "email")
        val _columnIndexOfPhone: Int = getColumnIndexOrThrow(_stmt, "phone")
        val _columnIndexOfNormalizedPhone: Int = getColumnIndexOrThrow(_stmt, "normalizedPhone")
        val _columnIndexOfAddress: Int = getColumnIndexOrThrow(_stmt, "address")
        val _columnIndexOfGender: Int = getColumnIndexOrThrow(_stmt, "gender")
        val _columnIndexOfDepartment: Int = getColumnIndexOrThrow(_stmt, "department")
        val _columnIndexOfSkills: Int = getColumnIndexOrThrow(_stmt, "skills")
        val _columnIndexOfEmploymentType: Int = getColumnIndexOrThrow(_stmt, "employmentType")
        val _columnIndexOfIsActive: Int = getColumnIndexOrThrow(_stmt, "isActive")
        val _columnIndexOfJoiningDateMillis: Int = getColumnIndexOrThrow(_stmt, "joiningDateMillis")
        val _columnIndexOfSalary: Int = getColumnIndexOrThrow(_stmt, "salary")
        val _columnIndexOfProfileImagePath: Int = getColumnIndexOrThrow(_stmt, "profileImagePath")
        val _columnIndexOfResumePath: Int = getColumnIndexOrThrow(_stmt, "resumePath")
        val _columnIndexOfResumeName: Int = getColumnIndexOrThrow(_stmt, "resumeName")
        val _columnIndexOfResumeSizeBytes: Int = getColumnIndexOrThrow(_stmt, "resumeSizeBytes")
        val _columnIndexOfResumeMimeType: Int = getColumnIndexOrThrow(_stmt, "resumeMimeType")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _result: EmployeeEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpFullName: String
          _tmpFullName = _stmt.getText(_columnIndexOfFullName)
          val _tmpEmail: String
          _tmpEmail = _stmt.getText(_columnIndexOfEmail)
          val _tmpPhone: String
          _tmpPhone = _stmt.getText(_columnIndexOfPhone)
          val _tmpNormalizedPhone: String
          _tmpNormalizedPhone = _stmt.getText(_columnIndexOfNormalizedPhone)
          val _tmpAddress: String
          _tmpAddress = _stmt.getText(_columnIndexOfAddress)
          val _tmpGender: Gender
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfGender)
          _tmpGender = __converters.stringToGender(_tmp)
          val _tmpDepartment: Department
          val _tmp_1: String
          _tmp_1 = _stmt.getText(_columnIndexOfDepartment)
          _tmpDepartment = __converters.stringToDepartment(_tmp_1)
          val _tmpSkills: List<Skill>
          val _tmp_2: String
          _tmp_2 = _stmt.getText(_columnIndexOfSkills)
          _tmpSkills = __converters.stringToSkills(_tmp_2)
          val _tmpEmploymentType: EmploymentType
          val _tmp_3: String
          _tmp_3 = _stmt.getText(_columnIndexOfEmploymentType)
          _tmpEmploymentType = __converters.stringToEmployment(_tmp_3)
          val _tmpIsActive: Boolean
          val _tmp_4: Int
          _tmp_4 = _stmt.getLong(_columnIndexOfIsActive).toInt()
          _tmpIsActive = _tmp_4 != 0
          val _tmpJoiningDateMillis: Long
          _tmpJoiningDateMillis = _stmt.getLong(_columnIndexOfJoiningDateMillis)
          val _tmpSalary: Double
          _tmpSalary = _stmt.getDouble(_columnIndexOfSalary)
          val _tmpProfileImagePath: String?
          if (_stmt.isNull(_columnIndexOfProfileImagePath)) {
            _tmpProfileImagePath = null
          } else {
            _tmpProfileImagePath = _stmt.getText(_columnIndexOfProfileImagePath)
          }
          val _tmpResumePath: String?
          if (_stmt.isNull(_columnIndexOfResumePath)) {
            _tmpResumePath = null
          } else {
            _tmpResumePath = _stmt.getText(_columnIndexOfResumePath)
          }
          val _tmpResumeName: String?
          if (_stmt.isNull(_columnIndexOfResumeName)) {
            _tmpResumeName = null
          } else {
            _tmpResumeName = _stmt.getText(_columnIndexOfResumeName)
          }
          val _tmpResumeSizeBytes: Long?
          if (_stmt.isNull(_columnIndexOfResumeSizeBytes)) {
            _tmpResumeSizeBytes = null
          } else {
            _tmpResumeSizeBytes = _stmt.getLong(_columnIndexOfResumeSizeBytes)
          }
          val _tmpResumeMimeType: String?
          if (_stmt.isNull(_columnIndexOfResumeMimeType)) {
            _tmpResumeMimeType = null
          } else {
            _tmpResumeMimeType = _stmt.getText(_columnIndexOfResumeMimeType)
          }
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          _result =
              EmployeeEntity(_tmpId,_tmpFullName,_tmpEmail,_tmpPhone,_tmpNormalizedPhone,_tmpAddress,_tmpGender,_tmpDepartment,_tmpSkills,_tmpEmploymentType,_tmpIsActive,_tmpJoiningDateMillis,_tmpSalary,_tmpProfileImagePath,_tmpResumePath,_tmpResumeName,_tmpResumeSizeBytes,_tmpResumeMimeType,_tmpCreatedAt,_tmpUpdatedAt)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteById(id: Long) {
    val _sql: String = "DELETE FROM employees WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
