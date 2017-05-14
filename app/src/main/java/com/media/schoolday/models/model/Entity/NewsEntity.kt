package com.media.schoolday.models.model.Entity

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

/*
 * Created by yosi on 05/05/2017.
 */

open class NewsEntity: RealmObject(){
        @SerializedName("_id")
        @PrimaryKey
        var id: String? = null
        var sekolah: String? = null
        var title: String? = null
        var topic: String? = null
        var description: String? = null
        var category: String? = null
        var publish: String? = null
        var userId: String? = null
        var userName: String? = null
        var userTitle: String? = null
        var timeCreated: Date? = null

}