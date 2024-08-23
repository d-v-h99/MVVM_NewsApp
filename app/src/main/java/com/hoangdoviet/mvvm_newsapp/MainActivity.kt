package com.hoangdoviet.mvvm_newsapp

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.hoangdoviet.mvvm_newsapp.adapter.FragmentAdapter
import com.hoangdoviet.mvvm_newsapp.databinding.ActivityMainBinding
import com.hoangdoviet.mvvm_newsapp.model.NewsModel
import com.hoangdoviet.mvvm_newsapp.ui.NewsViewModel
import com.hoangdoviet.mvvm_newsapp.util.Constants.Companion.BUSINESS
import com.hoangdoviet.mvvm_newsapp.util.Constants.Companion.ENTERTAINMENT
import com.hoangdoviet.mvvm_newsapp.util.Constants.Companion.GENERAL
import com.hoangdoviet.mvvm_newsapp.util.Constants.Companion.HEALTH
import com.hoangdoviet.mvvm_newsapp.util.Constants.Companion.HOME
import com.hoangdoviet.mvvm_newsapp.util.Constants.Companion.SCIENCE
import com.hoangdoviet.mvvm_newsapp.util.Constants.Companion.SPORTS
import com.hoangdoviet.mvvm_newsapp.util.Constants.Companion.TECHNOLOGY
import com.hoangdoviet.mvvm_newsapp.util.Constants.Companion.TOTAL_NEWS_TAB

class MainActivity : AppCompatActivity() {

    // Tabs Title
    private val newsCategories = arrayOf(
        HOME, BUSINESS,
        ENTERTAINMENT, SCIENCE,
        SPORTS, TECHNOLOGY, HEALTH
    )
    private lateinit var viewModel: NewsViewModel
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var fragmentAdapter :FragmentAdapter
    private lateinit var shimmerLayout: ShimmerFrameLayout
    private var totalRequestCount = 0
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set action bar
        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        tabLayout=binding.tabLayout
        viewPager=binding.viewPager
        shimmerLayout=binding.shimmerLayout
        viewModel=ViewModelProvider(this)[NewsViewModel::class.java]
        if(!isNetworkAvailable(applicationContext)){
            shimmerLayout.visibility = View.GONE
            val showError: TextView = binding.displayError
            showError.text = "Check your Internet Connection and Try Again!"
            showError.visibility=View.VISIBLE
        }
        // Send request call for news data
        requestNews(GENERAL, generalNews)
        requestNews(BUSINESS, businessNews)
        requestNews(ENTERTAINMENT, entertainmentNews)
        requestNews(HEALTH, healthNews)
        requestNews(SCIENCE, scienceNews)
        requestNews(SPORTS, sportsNews)
        requestNews(TECHNOLOGY, techNews)

        fragmentAdapter = FragmentAdapter(supportFragmentManager, lifecycle)
        viewPager.adapter = fragmentAdapter
        viewPager.visibility = View.GONE

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_item_mainactivity, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        intent = Intent(applicationContext, SavedNewsActivity::class.java)
        startActivity(intent)
        return super.onOptionsItemSelected(item)
    }
    private fun requestNews(newsCategory: String, newsData: MutableList<NewsModel>){
        viewModel.getNews(category = newsCategory)?.observe(this){
            newsData.addAll(it)
            totalRequestCount += 1
            if(newsCategory == GENERAL){
                shimmerLayout.stopShimmer()
                shimmerLayout.hideShimmer()
                shimmerLayout.visibility = View.GONE
                setViewPager()
            }
            if(totalRequestCount == TOTAL_NEWS_TAB){
                viewPager.offscreenPageLimit = 7
            }
        }
    }
    private fun setViewPager() {
        if (!apiRequestError) {
            viewPager.visibility = View.VISIBLE
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = newsCategories[position]
            }.attach()
        } else {
            val showError: TextView = findViewById(R.id.display_error)
            showError.text = errorMessage
            showError.visibility = View.VISIBLE
        }
    }
    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            if (connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!.isConnectedOrConnecting) {
                return true
            }
        }
        return false
    }
    companion object {
        var generalNews: ArrayList<NewsModel> = ArrayList()
        var entertainmentNews: MutableList<NewsModel> = mutableListOf()
        var businessNews: MutableList<NewsModel> = mutableListOf()
        var healthNews: MutableList<NewsModel> = mutableListOf()
        var scienceNews: MutableList<NewsModel> = mutableListOf()
        var sportsNews: MutableList<NewsModel> = mutableListOf()
        var techNews: MutableList<NewsModel> = mutableListOf()
        var apiRequestError = false
        var errorMessage = "error"
    }
}