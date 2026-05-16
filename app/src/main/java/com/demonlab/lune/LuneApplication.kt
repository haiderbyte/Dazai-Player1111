package com.demonlab.lune

import android.app.Application
import android.app.Activity
import android.os.Bundle
import androidx.core.view.WindowCompat

class LuneApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // مراقبة التطبيق بالكامل ومطالبة أي شاشة تفتح باحترام أزرار النظام السفلية
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                // إجبار الأنشطة على إظهار محتواها متوافقاً مع شريط أزرار التنقل السفلي
                WindowCompat.setDecorFitsSystemWindows(activity.window, true)
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
