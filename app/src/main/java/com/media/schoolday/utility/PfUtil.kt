package com.media.schoolday.utility
import android.content.Context
import com.media.schoolday.SchoolApp
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/*
 * Created by yosi on 08/05/2017.
 */

object PfUtil{

    val util: JSONSharedPreferences = SchoolApp.prefs!!

    fun getJsonArray(pref: String, key: String): JSONArray{
        return util.loadJSONArray(pref,key)
    }

    fun  getJsonObject(pref: String, key: String):JSONObject{
        return util.loadJSONObject(pref,key)
    }

    fun saveJsonArray(pref: String, key: String, js: JSONArray){
        util.saveJSONArray(pref, key, js)
    }

    fun saveJsonObject(pref: String, key: String, js: JSONObject){
        util.saveJSONObject(pref,key,js)
    }

    fun clear(key: String, value: String){
        util.remove(key,value)
    }
}

class JSONSharedPreferences(ctx : Context) {
    val c = ctx
    fun saveJSONObject(prefName: String, key: String, `object`: JSONObject) {
        val settings = c.getSharedPreferences(prefName, 0)
        val editor = settings.edit()
        editor.putString(JSONSharedPreferences.PREFIX.json + key, `object`.toString())
        editor.apply()
    }

    object PREFIX {
        val json = "json"
    }

    fun saveJSONArray(prefName: String, key: String, array: JSONArray) {
        val settings = c.getSharedPreferences(prefName, 0)
        val editor = settings.edit()
        editor.putString(JSONSharedPreferences.PREFIX.json + key, array.toString())
        editor.apply()
    }

    @Throws(JSONException::class)
    fun loadJSONObject(prefName: String, key: String): JSONObject {
        val settings = c.getSharedPreferences(prefName,0)
        return JSONObject(settings.getString(JSONSharedPreferences.PREFIX.json + key, "{}"))
    }

    @Throws(JSONException::class)
    fun loadJSONArray(prefName: String, key: String): JSONArray {
        val settings = c.getSharedPreferences(prefName, 0)
        return JSONArray(settings.getString(JSONSharedPreferences.PREFIX.json + key, "[]"))
    }

    fun remove(prefName: String, key: String) {
        val settings = c.getSharedPreferences(prefName, 0)
        if (settings.contains(JSONSharedPreferences.PREFIX.json + key)) {
            val editor = settings.edit()
            editor.remove(JSONSharedPreferences.PREFIX.json + key)
            editor.apply()
        }
    }
}