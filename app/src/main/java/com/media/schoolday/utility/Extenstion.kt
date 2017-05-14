package com.media.schoolday.utility

/**
 * Created by yosi on 06/05/2017.
 */


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
//import com.bumptech.glide.Glide

/**
 * Created by yosi on 20/04/2017.
 */

//fun ImageView.loadUrl(url:String){
//    Glide.with(context)
//            .load(url)
//            .into(this)
//}

fun ViewGroup.inflate(layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}

fun ViewGroup.inflate(ctx: Context, layoutRes: Int): View {
    return LayoutInflater.from(ctx).inflate(layoutRes, this, false)
}