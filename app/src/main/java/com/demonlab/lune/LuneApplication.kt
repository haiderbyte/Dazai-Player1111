package com.demonlab.lune

import android.app.Application
import android.app.Activity
import android.os.Bundle
import androidx.core.view.WindowCompat

class LuneApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                // تفعيل Edge-to-Edge الحقيقي لـ Compose
                WindowCompat.setDecorFitsSystemWindows(activity.window, false)
            }
            override fun onActivityStarted(activity: Activity) {}
            override fun onActivityResumed(activity: Activity) {}
            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {}
        })
    }
}
