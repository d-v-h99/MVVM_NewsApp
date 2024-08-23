package com.hoangdoviet.mvvm_newsapp.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.newsapp.api.RetrofitInstance
import com.example.newsapp.model.NewsResponse
import com.hoangdoviet.mvvm_newsapp.db.NewsDatabase
import com.hoangdoviet.mvvm_newsapp.model.NewsModel
import com.hoangdoviet.mvvm_newsapp.util.Constants.Companion.API_KEY
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class NewsRepository {
    companion object {

        private var newsDatabase: NewsDatabase? = null

        private fun initializeDB(context: Context): NewsDatabase {
            return NewsDatabase.getDatabaseClient(context)
        }

        fun insertNews(context: Context, news: NewsModel) {

            newsDatabase = initializeDB(context)
            CoroutineScope(IO).launch {
                newsDatabase!!.getNewsDao().insertNews(news)
            }
        }

        fun deleteNews(context: Context, news: NewsModel) {

            newsDatabase = initializeDB(context)
            CoroutineScope(IO).launch {
                newsDatabase!!.getNewsDao().deleteNews(news)
            }
        }

        fun getAllNews(context: Context): LiveData<List<NewsModel>> {

            newsDatabase = initializeDB(context)
            return newsDatabase!!.getNewsDao().getNewsFromDataBase()
        }

    }

        fun getNewsApiCall(category: String?): MutableLiveData<List<NewsModel>> {
            val newsList = MutableLiveData<List<NewsModel>>()
            val call = RetrofitInstance.api.getNews("us", category, API_KEY)
            call.enqueue(object : retrofit2.Callback<NewsResponse> {
                override fun onResponse(
                    call: Call<NewsResponse>,
                    response: Response<NewsResponse>
                ) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body != null) {
                            val tempNewsList = mutableListOf<NewsModel>()
                            body.articles.forEach {
                                tempNewsList.add(
                                    NewsModel(
                                        it.title,
                                        it.urlToImage,
                                        it.description,
                                        it.url,
                                        it.source.name,
                                        it.publishedAt,
                                        it.content
                                    )
                                )
                            }
                            newsList.value = tempNewsList
                        }
                    }
                }

                override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                 Log.d("loi","That bai")
                }

            })
            return newsList
        }
    }
