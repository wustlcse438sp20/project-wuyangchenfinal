package com.wustlcse438sp20.myrecipe

import android.app.Application
import androidx.multidex.MultiDex
import com.jakewharton.threetenabp.AndroidThreeTen


class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)

    }

}
