package com.yujing.openConnect

import android.app.Application
import com.yujing.utils.YUtils

class App : Application() {
    //标准单列
//    companion object {
//        val instance: App by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {App()}
//    }
    //单列
    companion object {
        private var instance: App? = null

        @Synchronized
        fun get(): App {
            return instance!!
        }
    }

    override fun onCreate() {
        super.onCreate()
        YUtils.init(this)
        instance = this
        androidx.multidex.MultiDex.install(this)
    }
}