package com.bookxpert.upasnaprojectss.data

import androidx.room.Room
import androidx.room.RoomDatabase
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

actual fun appDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val documents = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null
    )?.path.orEmpty()
    return Room.databaseBuilder<AppDatabase>("$documents/bookxpert_employees.db")
}
