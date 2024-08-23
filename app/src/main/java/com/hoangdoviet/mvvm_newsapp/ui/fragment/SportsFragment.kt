package com.hoangdoviet.mvvm_newsapp.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hoangdoviet.mvvm_newsapp.MainActivity
import com.hoangdoviet.mvvm_newsapp.R
import com.hoangdoviet.mvvm_newsapp.ReadNewsActivity
import com.hoangdoviet.mvvm_newsapp.adapter.CustomAdapter
import com.hoangdoviet.mvvm_newsapp.model.NewsModel
import com.hoangdoviet.mvvm_newsapp.util.Constants.Companion.NEWS_CONTENT
import com.hoangdoviet.mvvm_newsapp.util.Constants.Companion.NEWS_DESCRIPTION
import com.hoangdoviet.mvvm_newsapp.util.Constants.Companion.NEWS_IMAGE_URL
import com.hoangdoviet.mvvm_newsapp.util.Constants.Companion.NEWS_PUBLICATION_TIME
import com.hoangdoviet.mvvm_newsapp.util.Constants.Companion.NEWS_SOURCE
import com.hoangdoviet.mvvm_newsapp.util.Constants.Companion.NEWS_TITLE
import com.hoangdoviet.mvvm_newsapp.util.Constants.Companion.NEWS_URL

class SportsFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sports, container, false)
        val newsData: MutableList<NewsModel> = MainActivity.sportsNews
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val adapter = CustomAdapter(newsData)
        recyclerView.adapter = adapter

        adapter.setOnItemClickListener(object : CustomAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {

                val intent = Intent(context, ReadNewsActivity::class.java).apply {
                    putExtra(NEWS_URL, newsData[position].url)
                    putExtra(NEWS_TITLE, newsData[position].headline)
                    putExtra(NEWS_IMAGE_URL, newsData[position].image)
                    putExtra(NEWS_DESCRIPTION, newsData[position].description)
                    putExtra(NEWS_SOURCE, newsData[position].source)
                    putExtra(NEWS_PUBLICATION_TIME, newsData[position].time)
                    putExtra(NEWS_CONTENT, newsData[position].content)
                }
                startActivity(intent)
            }
        })

        //ignore
        adapter.setOnItemLongClickListener(object : CustomAdapter.OnItemLongClickListener {
            override fun onItemLongClick(position: Int) {
            }
        })

        return view
    }


}