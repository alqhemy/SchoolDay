package com.media.schoolday

import android.app.Application
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

    }

    override fun onCreate() {
        super.onCreate()
        prefs = JSONSharedPreferences(this)
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
        val url = "http://192.168.100.8:3000/web/api"
        rest = Wasp.Builder(this)
                .setEndpoint(url)
                .setLogLevel(LogLevel.FULL_REST_ONLY)
                .build().create(ApiService::class.java)
    }

}