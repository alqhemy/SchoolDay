package com.media.schoolday.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.media.schoolday.R
import com.media.schoolday.SchoolApp
import com.media.schoolday.models.model.NewsModel
import com.media.schoolday.models.model.ResponNews
import com.media.schoolday.utility.DbLocal
import com.media.schoolday.utility.inflate
import com.orhanobut.wasp.Callback
import com.orhanobut.wasp.Response
import com.orhanobut.wasp.WaspError
import kotlinx.android.synthetic.main.list_news_home.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.onClick
import org.jetbrains.anko.toast
import org.json.JSONObject
import java.text.SimpleDateFormat

/*
 * Created by yosi on 03/05/2017.
 */

class HomeFragment: Fragment(), AnkoLogger {
    private val realm
        get() = io.realm.Realm.getDefaultInstance()
    lateinit var list:RecyclerView

    val newsList = ArrayList<NewsModel>()
    companion object {
        fun newInstance(s: String):HomeFragment {
            val fragment = newInstance()
            val args = Bundle()
            args.putString("title",s)
            fragment.arguments = args
            return fragment
        }
        fun newInstance(): HomeFragment{
            return HomeFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =  inflater!!.inflate(R.layout.fragment_home,container,false)
        list = view.findViewById(R.id.home_list) as RecyclerView
        newsList.addAll(DbLocal.newsList())
        list.apply {
            layoutManager = LinearLayoutManager(activity)
//            val data:OrderedRealmCollection<NewsEntity> = realm.where(NewsEntity::class.java).findAll()
            list.adapter = HomeAdapter(newsList,activity)

        }

        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateNews()
    }

    fun updateAdapter(news: ArrayList<NewsModel>){

        val currentSise = newsList.size
        if(currentSise > 0) {
            newsList.clear()
            list.adapter.notifyItemRangeRemoved(0, currentSise)
        }

        newsList.addAll(news)
        list.adapter.notifyItemRangeInserted(0,news.size)
    }
    fun updateNews(){
        SchoolApp.rest!!.getNews(DbLocal.token(), object : Callback<ResponNews> {
            override fun onSuccess(response: Response?, t: ResponNews?) {
                val news = JSONObject(response!!.body)
                SchoolApp.prefs!!.saveJSONObject("sekolah","news",news)
                updateAdapter(t!!.data)
            }
            override fun onError(error: WaspError?) {}
        })
    }
}

class HomeAdapter(data: ArrayList<NewsModel>, ctx: Context): RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {
    private val news = data
    private val contex = ctx
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            HomeViewHolder = HomeViewHolder(parent.inflate(contex,R.layout.list_news_home))
    override fun getItemCount(): Int = news.size

    override fun onBindViewHolder(vh: HomeViewHolder, i: Int) = vh.bind(news!![i])


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
                    context.toast(news.id.toString())
                }

            }
        }

    }
}