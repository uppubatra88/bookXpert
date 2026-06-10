package com.bookxpert.upasnaprojectss

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.bookxpert.upasnaprojectss.data.AndroidDatabaseContext
import com.bookxpert.upasnaprojectss.di.initKoin
import com.bookxpert.upasnaprojectss.ui.App

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidDatabaseContext.appContext = applicationContext
        runCatching { initKoin() }
        setContent { App() }
    }
}
