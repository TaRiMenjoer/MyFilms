package com.example.myfilms.data.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Movie::class], version = 1)
abstract class DataBase : RoomDatabase() {
    abstract fun movieDao(): MovieDao

    companion object {

        var INSTANCE: DataBase? = null

//        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL("ALTER TABLE movie_table ADD COLUMN isLiked INTEGER DEFAULT 0")
//            }
//        }

        fun getDataBase(context: Context): DataBase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    DataBase::class.java,
                    "app_database3.db"
                ).build()
            }
            return INSTANCE!!
        }
    }
}


