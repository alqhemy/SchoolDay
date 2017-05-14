package com.media.schoolday.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.media.schoolday.R
import com.media.schoolday.SchoolApp
import com.media.schoolday.models.model.Entity.StudentEntity
import io.realm.Realm
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlinx.android.synthetic.main.list_child_profile.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.support.v4.toast

/*
 * Created by yosi on 03/05/2017.
 */

class ProfileFragment: Fragment(), AnkoLogger {
    private val realm
        get() = Realm.getDefaultInstance()

    lateinit var adapter:ProfileChildAdapter


    companion object {
        fun getInstance(s: String): ProfileFragment {
            val fragment = ProfileFragment()
            val args = Bundle()
            args.putString("title", s)
            fragment.arguments = args
            return ProfileFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_profile,container,false)
        val user = SchoolApp.prefs!!.loadJSONObject("user","user")

        view.tvProfileEmail.text = user.get("email").toString()
        view.tvProfileName.text = user.get("name").toString()

        view.tvAccountAnak.setOnClickListener { View.OnClickListener { toast(user.get("name").toString()) } }

//        val data = ArrayList(realm.where(StudentEntity::class.java).findAll())
//        if(data.size > 0) {
//            adapter = ProfileChildAdapter(activity.baseContext, data)
//            val list = view.findViewById(R.id.child_list) as ListView
//            list.adapter = adapter
//        }
        return view
    }

    fun data(){

    }
}

class ProfileChildAdapter(ctx: Context, student: ArrayList<StudentEntity>): ArrayAdapter<StudentEntity>(ctx,0, student){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var v = convertView
        // Get the data item for this position
        val user = getItem(position)
        // Check if an existing view is being reused, otherwise inflate the view
        v = LayoutInflater.from(context).inflate(R.layout.list_child_profile, parent, false)
        v.tvName.text = user.nama
        v.tvNis.text = user.nis
        // Return the completed view to render on screen
        return v
    }
}
