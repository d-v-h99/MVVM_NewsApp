package com.hoangdoviet.mvvm_newsapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hoangdoviet.mvvm_newsapp.model.NewsModel
import com.hoangdoviet.mvvm_newsapp.util.Constants.Companion.DATABASE_NAME

@Database(
    entities = [NewsModel::class],
    version = 1
)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun getNewsDao() : NewsDao
    companion object {

        @Volatile
        private var INSTANCE: NewsDatabase? = null

        fun getDatabaseClient(context: Context): NewsDatabase {

            if (INSTANCE != null) return INSTANCE!!

            synchronized(this) {

                INSTANCE = Room
                    .databaseBuilder(context, NewsDatabase::class.java, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build()

                return INSTANCE!!
            }
        }

    }
}