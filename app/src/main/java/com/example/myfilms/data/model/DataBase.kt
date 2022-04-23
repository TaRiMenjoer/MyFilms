package com.example.myfilms.data.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Movie::class] , version = 1)
abstract class DataBase: RoomDatabase() {
    abstract fun movieDao(): MovieDao

    companion object {

        var INSTANCE: DataBase? = null

        fun getDataBase(context: Context): DataBase{
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    DataBase::class.java,
                    "app_database.db"
                ).build()
            }
            return INSTANCE!!
        }
    }

}


