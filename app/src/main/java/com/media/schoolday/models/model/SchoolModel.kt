package com.media.schoolday.models.model

import com.google.gson.annotations.SerializedName

/*
 * Created by yosi on 06/05/2017.
 */

class ResponSchool(val data:ArrayList<SchoolModel>)

class SchoolModel {
    @SerializedName("_id")
    val id: String? = null
    val kategori: String? = null
    val nama: String? = null
    val alamat: String? = null
    val telp1: String? = null
    val telp2: String? = null
    val kota: String? = null
    val activity: ArrayList<String>? = null
    val kelas: ArrayList<String>? = null
}

class FilterModel(val filter: FilterArg)

class FilterArg(val sekolah: String)

class ProfilAnak(val child: Anak )

class Anak(val sekolah: String, val nis: String)

class ProfileGuru(val teacher: Guru)

class Guru(val id: String, val sekolah: String)