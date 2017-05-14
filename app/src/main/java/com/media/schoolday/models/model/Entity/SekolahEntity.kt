package com.media.schoolday.models.model.Entity

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/*
 * Created by yosi on 06/05/2017.
 */

open class SchoolEntity: RealmObject(){
    @PrimaryKey
    var kategori: String? = null
    var nama: String? = null
    var alamat: String? = null
    var telp1: String? = null
    var telp2: String? = null
    var kota: String? = null
}