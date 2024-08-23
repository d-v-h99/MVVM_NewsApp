package com.hoangdoviet.mvvm_newsapp.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hoangdoviet.mvvm_newsapp.model.NewsModel
@Dao
interface NewsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNews(news: NewsModel)

    @Query("select * from News_Table")
    fun getNewsFromDataBase(): LiveData<List<NewsModel>>

    @Delete
    fun deleteNews(news: NewsModel)
}