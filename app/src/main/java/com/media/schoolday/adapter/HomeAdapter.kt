package com.media.schoolday.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.media.schoolday.R
import com.media.schoolday.activity.NewsActivity
import com.media.schoolday.models.model.NewsModel
import com.media.schoolday.utility.inflate
import kotlinx.android.synthetic.main.list_news_home.view.*
import org.jetbrains.anko.onClick
import org.jetbrains.anko.startActivity
import java.text.SimpleDateFormat
import java.util.*

/*
 * Created by yosi on 14/05/2017.
 */
class HomeAdapter(data: ArrayList<NewsModel>, ctx: Context): RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {
    private val news = data
    private val contex = ctx
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            HomeViewHolder = HomeViewHolder(parent.inflate(contex, R.layout.list_news_home))
    override fun getItemCount(): Int = news.size

    override fun onBindViewHolder(vh: HomeViewHolder, i: Int) = vh.bind(news[i])


    class HomeViewHolder(var itemview: View) : RecyclerView.ViewHolder(itemview) {
        fun bind(news: NewsModel) = with(itemview) {
            tvHomeItemUser.text = news.userName
            tvHomeItemDate.text = SimpleDateFormat("dd-MM-yyyy HH:mm").format(news.timeCreated).toString()
            tvHomeItemTitle.text = news.title
            tvHomeItemDescription.text = news.description
            tvHomeItemTopic.text = news.topic
            tvHomeItemUserTitle.text = news.userTitle

            onClick {
                if(adapterPosition != RecyclerView.NO_POSITION){
                    context.startActivity<NewsActivity>("post" to "read","id" to news.id)
                }

            }
        }

    }
}