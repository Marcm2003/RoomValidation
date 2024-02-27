package com.mp08.a07roomValidation.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Friend::class], version = 1)

abstract class MyDb : RoomDatabase() {
    abstract fun friendDao(): FriendDao

}

