package com.hoangdoviet.mvvm_newsapp.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.hoangdoviet.mvvm_newsapp.MainActivity
import com.hoangdoviet.mvvm_newsapp.R
import com.hoangdoviet.mvvm_newsapp.ReadNewsActivity
import com.hoangdoviet.mvvm_newsapp.adapter.CustomAdapter
import com.hoangdoviet.mvvm_newsapp.model.NewsModel
import com.hoangdoviet.mvvm_newsapp.util.Constants.Companion.INITIAL_POSITION
import com.hoangdoviet.mvvm_newsapp.util.Constants.Companion.NEWS_CONTENT
import com.hoangdoviet.mvvm_newsapp.util.Constants.Companion.NEWS_DESCRIPTION
import com.hoangdoviet.mvvm_newsapp.util.Constants.Companion.NEWS_IMAGE_URL
import com.hoangdoviet.mvvm_newsapp.util.Constants.Companion.NEWS_PUBLICATION_TIME
import com.hoangdoviet.mvvm_newsapp.util.Constants.Companion.NEWS_SOURCE
import com.hoangdoviet.mvvm_newsapp.util.Constants.Companion.NEWS_TITLE
import com.hoangdoviet.mvvm_newsapp.util.Constants.Companion.NEWS_URL
import com.hoangdoviet.mvvm_newsapp.util.Constants.Companion.TOP_HEADLINES_COUNT


class GeneralFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var imageSlider: ImageSlider
    private lateinit var adapter: CustomAdapter
    private lateinit var newsDataForTopHeadlines: List<NewsModel>
    private lateinit var newsDataForDown: List<NewsModel>
    var position = INITIAL_POSITION

      override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
          val view = inflater.inflate(R.layout.fragment_general, container, false)
          val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

          recyclerView = view.findViewById(R.id.recyclerView)
          recyclerView.layoutManager = layoutManager

          // Setting recyclerViews adapter
          newsDataForTopHeadlines = MainActivity.generalNews.slice(0 until TOP_HEADLINES_COUNT)
          newsDataForDown = MainActivity.generalNews.slice(TOP_HEADLINES_COUNT until MainActivity.generalNews.size - TOP_HEADLINES_COUNT)
          adapter = CustomAdapter(newsDataForDown)
          recyclerView.adapter = adapter

          imageSlider= view.findViewById(R.id.image_slider)
          val slideModels = newsDataForTopHeadlines.mapNotNull { news ->
              if (!news.image.isNullOrEmpty()) {
                  SlideModel(news.image, news.headline)
              } else {
                  null // Loại bỏ các mục có URL không hợp lệ
              }
          }.toList()

          imageSlider.setImageList(slideModels, ScaleTypes.CENTER_CROP)
          imageSlider.setItemClickListener(object : ItemClickListener {
              override fun onItemSelected(position: Int) {
                  // You can listen here.
                  val intent = Intent(context, ReadNewsActivity::class.java).apply {
                      putExtra(NEWS_URL, newsDataForTopHeadlines[position].url)
                      putExtra(NEWS_TITLE, newsDataForTopHeadlines[position].headline)
                      putExtra(NEWS_IMAGE_URL, newsDataForTopHeadlines[position].image)
                      putExtra(NEWS_DESCRIPTION, newsDataForTopHeadlines[position].description)
                      putExtra(NEWS_SOURCE, newsDataForTopHeadlines[position].source)
                      putExtra(NEWS_PUBLICATION_TIME, newsDataForTopHeadlines[position].time)
                      putExtra(NEWS_CONTENT, newsDataForTopHeadlines[position].content)
                  }

                  startActivity(intent)
              }
              override fun doubleClick(position: Int) {
                  // Do not use onItemSelected if you are using a double click listener at the same time.
                  // Its just added for specific cases.
                  // Listen for clicks under 250 milliseconds.
              } })

          // listitem onClick
          adapter.setOnItemClickListener(object : CustomAdapter.OnItemClickListener {
              override fun onItemClick(position: Int) {
                  val intent = Intent(context, ReadNewsActivity::class.java).apply {
                      putExtra(NEWS_URL, newsDataForDown[position].url)
                      putExtra(NEWS_TITLE, newsDataForDown[position].headline)
                      putExtra(NEWS_IMAGE_URL, newsDataForDown[position].image)
                      putExtra(NEWS_DESCRIPTION, newsDataForDown[position].description)
                      putExtra(NEWS_SOURCE, newsDataForDown[position].source)
                      putExtra(NEWS_PUBLICATION_TIME, newsDataForDown[position].time)
                      putExtra(NEWS_CONTENT, newsDataForDown[position].content)
                  }

                  startActivity(intent)
              }
          })

          // Ignore
          adapter.setOnItemLongClickListener(object : CustomAdapter.OnItemLongClickListener {
              override fun onItemLongClick(position: Int) = Unit
          })
          return view
      }


}