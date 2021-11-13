package com.example.contactlist

import android.app.Application
import android.content.Context
import java.security.AccessControlContext

class App:Application() {
    override fun onCreate() {
        context = applicationContext
        super.onCreate()
    }

        companion object{
            lateinit var context: Context
        }
}

interface IContextProvider {
    val context :Context
}

object ContextProvider : IContextProvider{
    override val context: Context
        get() = App.context

}