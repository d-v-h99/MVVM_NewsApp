package com.hoangdoviet.mvvm_newsapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "News_Table")
data class NewsModel (
    @PrimaryKey(autoGenerate = false)
    val headline: String,
    val image: String?,
    val description: String?,
    val url: String?,
    val source: String?,
    val time: String?,
    val content: String?
)