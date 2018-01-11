package cn.psycho.moviebeauty

import android.app.Application

/**
 * Created by Psycho on 2018/1/10.
 */
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        application = this
    }


    companion object {
        var application: MyApplication ?= null

        fun getInstance(): MyApplication? {
            return application
        }
    }
}