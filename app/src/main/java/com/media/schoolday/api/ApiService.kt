package com.media.schoolday.api

import com.media.schoolday.models.model.*
import com.orhanobut.wasp.Callback
import com.orhanobut.wasp.http.*

/*
 * Created by yosi on 03/05/2017.
 */
interface ApiService {
    @POST("/register")
    fun getUser(@BodyMap user: HashMap<String,String>, callback: Callback<ResponUser>)

    @GET("/profile")
    fun getProfile(@Header("Authorization") a: String, callback: Callback<ResponProfile>)

    @GET("/news")
    fun getNews(@Header("Authorization") a: String, callback: Callback<ResponNews>)

    @GET("/sekolah")
    fun getSekolah(callback: Callback<ResponSchool>)

    @POST("/sekolah/siswa")
    fun getSiswa(@Body filter: FilterModel, callback: Callback<Students>)

    @POST("/sekolah/guru")
    fun getGuru(@Body filter: FilterModel, callback: Callback<Teachers>)

    @PUT("/profile/anak")
    fun putProfileAnak(@Header("Authorization") a: String, @Body child: ProfilAnak, callback: Callback<Profile>)

    @PUT("/profile/guru")
    fun putProfileGuru(@Header("Authorization") a: String, @Body guru: ProfileGuru, callback: Callback<Profile>)



}