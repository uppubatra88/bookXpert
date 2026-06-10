package com.bookxpert.upasnaprojectss.data

import androidx.room.RoomDatabase

expect fun appDatabaseBuilder(): RoomDatabase.Builder<AppDatabase>
