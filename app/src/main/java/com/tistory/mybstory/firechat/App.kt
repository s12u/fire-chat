package com.tistory.mybstory.firechat

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
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
