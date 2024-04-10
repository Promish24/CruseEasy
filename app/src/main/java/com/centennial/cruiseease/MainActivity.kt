package com.centennial.cruiseease

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.MutableLiveData
import com.centennial.cruiseease.navigation.Stack
import com.centennial.cruiseease.ui.theme.CruiseEaseTheme

class MainActivity : ComponentActivity() {
    private val isDataReady = MutableLiveData(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition {
            !isDataReady.value!!
        }

        setContent {
            CruiseEaseTheme {
                Stack()
            }
        }
        loadData()
    }

    private fun loadData() {
        Handler(Looper.getMainLooper()).postDelayed({
            isDataReady.postValue(true)
        }, 1000)
    }
}
