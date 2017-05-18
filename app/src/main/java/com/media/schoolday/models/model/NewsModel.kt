package com.media.schoolday.models.model

import com.google.gson.annotations.SerializedName
import io.realm.annotations.PrimaryKey
import java.util.*

/**
 * Created by yosi on 03/05/2017.
 */

class ResponNews(@SerializedName("news") val data: ArrayList<NewsModel>)

data class NewsModel(
    @SerializedName("_id")
    @PrimaryKey
    val id: String,
    val sekolah: String,
    val title: String,
    val topic: String,
    val description: String,
    val category: String,
    val publish: String,
    val userId: String,
    val userName: String,
    val userTitle: String,
    val timeCreated: Date,
    val comments: ArrayList<Comment>?
)

data class Comment(val comment: String, val user: String, val title: String, val timeCreated: Date )