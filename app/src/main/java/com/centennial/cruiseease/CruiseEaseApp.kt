package com.centennial.cruiseease


import android.app.Application
import com.google.firebase.FirebaseApp

class CruiseEaseApp: Application() {
    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)
    }
}