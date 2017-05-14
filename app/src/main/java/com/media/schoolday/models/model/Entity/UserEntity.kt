package com.media.schoolday.models.model.Entity

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey



/**
 * Created by yosi on 03/05/2017.
 */

open class UserEntity:RealmObject() {

    @PrimaryKey
    var email: String? = null

    var uid: String? = null

    var name: String? = null

    var token: String? = null
}

open class AccountEntiry: RealmObject(){
    @SerializedName("_id")
    @PrimaryKey
    var id: String? = null

    var sekolah: String? = null

    var kelas: String? = null

    var nama: String? = null

}

open class StudentEntity : RealmObject() {
    @SerializedName("_id")
    var id: String? = null

    var sekolah: String? = null

    var kelas: String? = null

    var nama: String? = null

    @PrimaryKey
    var nis: String? = null
}

open class TeacherEntity:RealmObject(){
    @SerializedName("id")
    @PrimaryKey
    var id: String? = null

    var sekolah: String? = null

    var nama: String? = null

    var jabatan: String? = null

    var alamat: String? = null

    var telp: String? = null


}

