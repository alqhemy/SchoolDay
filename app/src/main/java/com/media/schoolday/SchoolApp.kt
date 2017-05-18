package com.media.schoolday

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.media.schoolday.api.ApiService
import com.media.schoolday.utility.JSONSharedPreferences
import com.orhanobut.wasp.Wasp
import com.orhanobut.wasp.utils.LogLevel
import io.realm.Realm
import io.realm.RealmConfiguration

class SchoolApp: Application(){
    companion object {
        var prefs: JSONSharedPreferences? = null
        var rest: ApiService? = null
        var user: FirebaseAuth? = null

    }

    override fun onCreate() {
        super.onCreate()
        prefs = JSONSharedPreferences(this)
        user = FirebaseAuth.getInstance()
        restApi()
        initRealm()
    }

    private fun initRealm() {

        Realm.init(this)
        val realmConfiguration: RealmConfiguration = RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build()
        Realm.setDefaultConfiguration(realmConfiguration)
    }

    private fun restApi(){
        //cloud server
        val url = "http://128.199.199.232/web/api"
        //local server
//        val url = "http://192.168.100.8:3000/web/api"
        rest = Wasp.Builder(this)
                .setEndpoint(url)
                .setLogLevel(LogLevel.FULL_REST_ONLY)
                .build().create(ApiService::class.java)
    }

}