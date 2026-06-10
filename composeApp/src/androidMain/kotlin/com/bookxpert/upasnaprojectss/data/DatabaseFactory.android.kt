package com.bookxpert.upasnaprojectss.data

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

object AndroidDatabaseContext {
    lateinit var appContext: Context
}

actual fun appDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> =
    Room.databaseBuilder(
        context = AndroidDatabaseContext.appContext,
        klass = AppDatabase::class.java,
        name = "bookxpert_employees.db"
    )
