package com.media.schoolday.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import com.facebook.FacebookSdk.getApplicationContext
import com.media.schoolday.R
import com.media.schoolday.models.model.Comment
import com.media.schoolday.utility.DbLocal
import kotlinx.android.synthetic.main.list_item_find.view.*
import kotlinx.android.synthetic.main.list_news_home.view.*
import org.jetbrains.anko.AnkoLogger
import java.text.SimpleDateFormat
import java.util.*




/*
 * Created by yosi on 10/05/2017.
 */

class ReadFragment: Fragment(),AnkoLogger{

    val KEY_ID = "id"
    var find: String? = null
    lateinit var adapter:ArrayAdapter<Comment>
    interface FindListener {
        fun AlertMesage(s: String)
        fun onProcess(data: HashMap<String,String>) {}
    }
    var callback: FindListener? = null

    companion object {
        fun newInstance(s: String, id: String): ReadFragment {
            val fragment = newInstance()
            val args = Bundle()
            args.putString("title", s)
            args.putString("id",id)
            fragment.arguments = args
            return fragment
        }
        fun newInstance(): ReadFragment {
            return ReadFragment()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(arguments != null && arguments.containsKey(KEY_ID)){
            find = arguments.getString(KEY_ID)
        }
        callback = activity as? FindListener
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.fragment_find,container,false)
        val news = DbLocal.newsList().filter { it.id == find }.first()
        val listComment = view.findViewById(R.id.listReadComment) as ListView
        val comments = ArrayList<Comment>()
        val adapter = CommentAdapter(context,comments) as ArrayAdapter<Comment>
        listComment.adapter = adapter
        with(view){
            tvHomeItemUser.text = news.userName
            tvHomeItemDate.text = SimpleDateFormat("dd-MM-yyyy HH:mm").format(news.timeCreated).toString()
            tvHomeItemTitle.text = news.title
            tvHomeItemDescription.text = news.description
            tvHomeItemTopic.text = news.topic
            tvHomeItemUserTitle.text = news.userTitle
        }

//        if (news.comments != null) {
//            news.comments.forEach {
//                val view2 = LayoutInflater.from(activity.baseContext).inflate(R.layout.list_item_find, null)
//                view2.tvFindItemUser.text = it.user
//                view2.tvFindItemDescription.text = it.comment
//                view2.tvFindItemDate.text = SimpleDateFormat("dd-MM-yyyy HH:mm").format(it.timeCreated).toString()
//                view2.tvFindItemUserTitle.text = it.title
//                view.linearReadComment.addView(view2)
//            }
//

        if(news.comments != null) {
            with(adapter){
                clear()
                notifyDataSetChanged()
                addAll(news.comments)
                notifyDataSetChanged()
            }

        }
//
// listComment
//        lv = view.findViewById(R.id.lvFind) as ListView
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val news = DbLocal.newsList().filter { it.id == find }.first()
//        if (news.comments != null)
//            addComment(news.comments)

//            listReadNewsListComment.adapter = CommentAdapter(context,news.comments)
//            val adapter = CommentAdapter(context, news.comments)
//            listComment.adapter = adapter
//        }


    }
    fun addComment(comment: ArrayList<Comment>){
        val vi = getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v = vi.inflate(R.layout.fragment_find, null)

        val insertView = v.findViewById(R.id.listReadComment) as ViewGroup
        val i = 0
        comment.forEach {
           val text =  v.findViewById(R.id.tvFindItemUser) as TextView
            text.text = it.user

//            v.tvFindItemUser.text = it.user
//            v.tvFindItemDescription.text = it.comment
//            v.tvFindItemDate.text = SimpleDateFormat("dd-MM-yyyy HH:mm").format(it.timeCreated).toString()
//            v.tvFindItemUserTitle.text = it.title
            insertView.addView(v,i,ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT))
        }

    }

}


class CommentAdapter(ctx: Context, comment: ArrayList<Comment>): ArrayAdapter<Comment>(ctx,0,comment){

    val comments = comment

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val comment = getItem(position)
        var v = convertView
        if (v == null)
             v = LayoutInflater.from(context).inflate(R.layout.list_item_find, parent, false)

        with(comment){
            v?.tvFindItemUser?.text = user
            v?.tvFindItemDescription?.text = comment.comment
            v?.tvFindItemDate?.text = SimpleDateFormat("dd-MM-yyyy HH:mm").format(timeCreated).toString()
            v?.tvFindItemUserTitle?.text = title
        }
        return v!!
    }

    override fun getCount(): Int {
        return comments.size
    }

    override fun getPosition(item: Comment?): Int {
        return super.getPosition(item)
    }
}

