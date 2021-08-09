package com.tistory.mybstory.firechat

import android.app.Application
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        FirebaseApp.initializeApp(this)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    override fun onTerminate() {
        super.onTerminate()
    }

    companion object {
        @JvmField
        var instance: App? = null
        @JvmStatic fun getApplication(): App {
            return instance as App
        }
    }
}
