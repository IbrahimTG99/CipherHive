package com.devsinc.cipherhive

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.view.WindowManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CipherHive : Application() {
    override fun onCreate() {
        super.onCreate()
        registerListeners()
    }

    private fun registerListeners() {
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                activity.window.setFlags(
                    WindowManager.LayoutParams.FLAG_SECURE,
                    WindowManager.LayoutParams.FLAG_SECURE
                )
            }

            override fun onActivityStarted(activity: Activity) = Unit
            override fun onActivityResumed(activity: Activity) = Unit
            override fun onActivityPaused(activity: Activity) = Unit
            override fun onActivityStopped(activity: Activity) = Unit
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit
            override fun onActivityDestroyed(activity: Activity) = Unit
        })
    }
}
