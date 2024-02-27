package com.mp08.a07roomValidation

import android.app.Application
import androidx.room.Room
import com.mp08.a07roomValidation.room.MyDb
import kotlinx.coroutines.GlobalScope

class App: Application() {
    lateinit var db: MyDb

    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(
                this,
                MyDb::class.java,
                "room-db"
            ).build()
    }
}