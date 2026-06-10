package com.bookxpert.upasnaprojectss.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.bookxpert.upasnaprojectss.data.AppDatabase
import com.bookxpert.upasnaprojectss.data.EmployeeRepository
import com.bookxpert.upasnaprojectss.data.appDatabaseBuilder
import com.bookxpert.upasnaprojectss.presentation.EmployeeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

val sharedModule = module {
    single<AppDatabase> {
        appDatabaseBuilder()
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }
    single { get<AppDatabase>().employeeDao() }
    single { EmployeeRepository(get()) }
    factory { EmployeeViewModel(get()) }
}

fun initKoin(platformModule: Module? = null) {
    startKoin {
        modules(listOfNotNull(sharedModule, platformModule))
    }
}
