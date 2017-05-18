package com.media.schoolday.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.media.schoolday.MainActivity
import com.media.schoolday.R
import com.media.schoolday.SchoolApp
import com.media.schoolday.adapter.HomeAdapter
import com.media.schoolday.models.model.NewsModel
import com.media.schoolday.models.model.ResponNews
import com.media.schoolday.utility.DbLocal
import com.orhanobut.wasp.Callback
import com.orhanobut.wasp.Response
import com.orhanobut.wasp.WaspError
import org.jetbrains.anko.AnkoLogger
import org.json.JSONObject

/*
 * Created by yosi on 03/05/2017.
 */

class HomeFragment: Fragment(), AnkoLogger,MainActivity.OnItemClickListener {
    private val realm
        get() = io.realm.Realm.getDefaultInstance()
    lateinit var list:RecyclerView
    var title: String? = null
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = arguments.getString("title")
    }
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =  inflater!!.inflate(R.layout.fragment_home,container,false)
        list = view.findViewById(R.id.home_list) as RecyclerView
        val default = ArrayList<NewsModel>()
        newsList.addAll(default)
        list.apply {
            layoutManager = LinearLayoutManager(activity)
            list.adapter = HomeAdapter(newsList,activity)
        }
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        updateNews()
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
       if(!DbLocal.token().isNullOrBlank()){
            SchoolApp.rest?.getNews(DbLocal.token(), object : Callback<ResponNews> {
                override fun onSuccess(response: Response?, t: ResponNews?) {
                    val news = JSONObject(response?.body)
                    SchoolApp.prefs?.saveJSONObject("sekolah","news",news)
                    updateAdapter(t!!.data)
                }
                override fun onError(error: WaspError?) {}
            })
        }

    }

    override fun OnDataUpdate(args: ArrayList<NewsModel>) {
        updateAdapter(args)
    }
}

