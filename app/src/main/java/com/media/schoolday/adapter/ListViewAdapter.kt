package com.media.schoolday.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.media.schoolday.R
import com.media.schoolday.models.model.Student
import com.media.schoolday.models.model.Teacher
import kotlinx.android.synthetic.main.list_child_profile.view.*
import kotlinx.android.synthetic.main.list_teacher_profile.view.*

/*
 * Created by yosi on 11/05/2017.
 */

class ChildAdapter(ctx: Context, child: List<Student>): ArrayAdapter<Student>(ctx,0,child){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var v = convertView
        // Get the data item for this position
        val user = getItem(position)
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            v = LayoutInflater.from(context).inflate(R.layout.list_child_profile, parent, false)
        }
        v!!.tvName.text = user.nama
        v.tvNis.text = user.nis
        // Return the completed view to render on screen
        return v
    }
}

class TeacherAdapter(ctx: Context, child: ArrayList<Teacher>): ArrayAdapter<Teacher>(ctx,0,child){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var v = convertView
        // Get the data item for this position
        val user = getItem(position)
        // Check if an existing view is being reused, otherwise inflate the view
        if (v == null) {
            v = LayoutInflater.from(context).inflate(R.layout.list_teacher_profile, parent, false)
        }
        v!!.tvAccountListTeacherName.text  = user.nama
//        convertView.tvAccountListTeacherNik.setText(user.jabatan)
        // Return the completed view to render on screen
        return v
    }
}