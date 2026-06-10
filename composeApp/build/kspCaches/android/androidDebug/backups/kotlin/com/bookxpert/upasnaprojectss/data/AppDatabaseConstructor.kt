package com.bookxpert.upasnaprojectss.`data`

import androidx.room.RoomDatabaseConstructor

public actual object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
  actual override fun initialize(): AppDatabase =
      com.bookxpert.upasnaprojectss.`data`.AppDatabase_Impl()
}
