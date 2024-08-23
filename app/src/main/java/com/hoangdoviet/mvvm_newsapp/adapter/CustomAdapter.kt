package com.hoangdoviet.mvvm_newsapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.model.Article
import com.hoangdoviet.mvvm_newsapp.R
import com.hoangdoviet.mvvm_newsapp.model.NewsModel
import java.time.Duration
import java.time.Instant
import java.time.ZoneId

class CustomAdapter(private var newsList: List<NewsModel>) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
    private lateinit var context: Context
    private lateinit var mClickListener: OnItemClickListener
    private lateinit var mLongClickListener: OnItemLongClickListener

    init {
        this.notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    interface OnItemLongClickListener {
        fun onItemLongClick(position: Int)
    }

    fun setOnItemLongClickListener(listener: OnItemLongClickListener) {
        mLongClickListener = listener
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mClickListener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        context = parent.context
        return ViewHolder(view, mClickListener, mLongClickListener)
    }

    override fun getItemCount(): Int {
       return newsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val data =  newsList[position]
        holder.itemView.apply {
            holder.headLine.text=data.headline
            val time: String? = data.time
            val imgUrl = data.image
            if(imgUrl.isNullOrEmpty()){
                Glide.with(this).load(R.drawable.samplenews).into(holder.image)
            } else {
                Glide.with(this).load(data.image).into(holder.image)
            }
            if(context.toString().contains("SavedNews")){
                val date = " " + time?.substring(0, time.indexOf('T',0))
                holder.newsPublicationTime.text= date
            } else {
                val currentTimeInHours = Instant.now().atZone(ZoneId.of("Asia/Kolkata"))
                val newsTimeInHours = Instant.parse(time).atZone(ZoneId.of("Asia/Kolkata"))
                val hoursDifference = Duration.between(currentTimeInHours, newsTimeInHours)
                val hoursAgo = " " + hoursDifference.toHours().toString().substring(1) + " hour ago"
                holder.newsPublicationTime.text = hoursAgo
            }

        }

    }

    inner class ViewHolder(
        itemView: View,
        listener: OnItemClickListener,
        listener2: OnItemLongClickListener
    ) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.img)
        val headLine: TextView = itemView.findViewById(R.id.news_title)
        val newsPublicationTime: TextView = itemView.findViewById(R.id.news_publication_time)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }

            itemView.setOnLongClickListener {
                listener2.onItemLongClick(adapterPosition)
                return@setOnLongClickListener true
            }
        }
    }
}