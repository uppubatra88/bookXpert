package com.bookxpert.upasnaprojectss.`data`

import androidx.room.InvalidationTracker
import androidx.room.RoomOpenDelegate
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.room.util.TableInfo
import androidx.room.util.TableInfo.Companion.read
import androidx.room.util.dropFtsSyncTriggers
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import javax.`annotation`.processing.Generated
import kotlin.Lazy
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.MutableSet
import kotlin.collections.Set
import kotlin.collections.mutableListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.mutableSetOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class AppDatabase_Impl : AppDatabase() {
  private val _employeeDao: Lazy<EmployeeDao> = lazy {
    EmployeeDao_Impl(this)
  }

  protected override fun createOpenDelegate(): RoomOpenDelegate {
    val _openDelegate: RoomOpenDelegate = object : RoomOpenDelegate(1,
        "0e5afdcf51c1c96ea4cbdc18a70c2023", "45ac8244ede40eaa9d4be96f90dae4d9") {
      public override fun createAllTables(connection: SQLiteConnection) {
        connection.execSQL("CREATE TABLE IF NOT EXISTS `employees` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `fullName` TEXT NOT NULL, `email` TEXT NOT NULL, `phone` TEXT NOT NULL, `normalizedPhone` TEXT NOT NULL, `address` TEXT NOT NULL, `gender` TEXT NOT NULL, `department` TEXT NOT NULL, `skills` TEXT NOT NULL, `employmentType` TEXT NOT NULL, `isActive` INTEGER NOT NULL, `joiningDateMillis` INTEGER NOT NULL, `salary` REAL NOT NULL, `profileImagePath` TEXT, `resumePath` TEXT, `resumeName` TEXT, `resumeSizeBytes` INTEGER, `resumeMimeType` TEXT, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)")
        connection.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '0e5afdcf51c1c96ea4cbdc18a70c2023')")
      }

      public override fun dropAllTables(connection: SQLiteConnection) {
        connection.execSQL("DROP TABLE IF EXISTS `employees`")
      }

      public override fun onCreate(connection: SQLiteConnection) {
      }

      public override fun onOpen(connection: SQLiteConnection) {
        internalInitInvalidationTracker(connection)
      }

      public override fun onPreMigrate(connection: SQLiteConnection) {
        dropFtsSyncTriggers(connection)
      }

      public override fun onPostMigrate(connection: SQLiteConnection) {
      }

      public override fun onValidateSchema(connection: SQLiteConnection):
          RoomOpenDelegate.ValidationResult {
        val _columnsEmployees: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsEmployees.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsEmployees.put("fullName", TableInfo.Column("fullName", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsEmployees.put("email", TableInfo.Column("email", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsEmployees.put("phone", TableInfo.Column("phone", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsEmployees.put("normalizedPhone", TableInfo.Column("normalizedPhone", "TEXT", true,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsEmployees.put("address", TableInfo.Column("address", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsEmployees.put("gender", TableInfo.Column("gender", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsEmployees.put("department", TableInfo.Column("department", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsEmployees.put("skills", TableInfo.Column("skills", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsEmployees.put("employmentType", TableInfo.Column("employmentType", "TEXT", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsEmployees.put("isActive", TableInfo.Column("isActive", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsEmployees.put("joiningDateMillis", TableInfo.Column("joiningDateMillis", "INTEGER",
            true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsEmployees.put("salary", TableInfo.Column("salary", "REAL", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsEmployees.put("profileImagePath", TableInfo.Column("profileImagePath", "TEXT",
            false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsEmployees.put("resumePath", TableInfo.Column("resumePath", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsEmployees.put("resumeName", TableInfo.Column("resumeName", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsEmployees.put("resumeSizeBytes", TableInfo.Column("resumeSizeBytes", "INTEGER",
            false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsEmployees.put("resumeMimeType", TableInfo.Column("resumeMimeType", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsEmployees.put("createdAt", TableInfo.Column("createdAt", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsEmployees.put("updatedAt", TableInfo.Column("updatedAt", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysEmployees: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesEmployees: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoEmployees: TableInfo = TableInfo("employees", _columnsEmployees,
            _foreignKeysEmployees, _indicesEmployees)
        val _existingEmployees: TableInfo = read(connection, "employees")
        if (!_infoEmployees.equals(_existingEmployees)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |employees(com.bookxpert.upasnaprojectss.data.EmployeeEntity).
              | Expected:
              |""".trimMargin() + _infoEmployees + """
              |
              | Found:
              |""".trimMargin() + _existingEmployees)
        }
        return RoomOpenDelegate.ValidationResult(true, null)
      }
    }
    return _openDelegate
  }

  protected override fun createInvalidationTracker(): InvalidationTracker {
    val _shadowTablesMap: MutableMap<String, String> = mutableMapOf()
    val _viewTables: MutableMap<String, Set<String>> = mutableMapOf()
    return InvalidationTracker(this, _shadowTablesMap, _viewTables, "employees")
  }

  public override fun clearAllTables() {
    super.performClear(false, "employees")
  }

  protected override fun getRequiredTypeConverterClasses(): Map<KClass<*>, List<KClass<*>>> {
    val _typeConvertersMap: MutableMap<KClass<*>, List<KClass<*>>> = mutableMapOf()
    _typeConvertersMap.put(EmployeeDao::class, EmployeeDao_Impl.getRequiredConverters())
    return _typeConvertersMap
  }

  public override fun getRequiredAutoMigrationSpecClasses(): Set<KClass<out AutoMigrationSpec>> {
    val _autoMigrationSpecsSet: MutableSet<KClass<out AutoMigrationSpec>> = mutableSetOf()
    return _autoMigrationSpecsSet
  }

  public override
      fun createAutoMigrations(autoMigrationSpecs: Map<KClass<out AutoMigrationSpec>, AutoMigrationSpec>):
      List<Migration> {
    val _autoMigrations: MutableList<Migration> = mutableListOf()
    return _autoMigrations
  }

  public override fun employeeDao(): EmployeeDao = _employeeDao.value
}
