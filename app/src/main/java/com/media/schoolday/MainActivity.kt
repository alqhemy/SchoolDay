package com.media.schoolday

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ResultCodes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.media.schoolday.activity.NewsActivity
import com.media.schoolday.adapter.TabPageAdapter
import com.media.schoolday.models.model.ResponNews
import com.media.schoolday.models.model.ResponProfile
import com.media.schoolday.models.model.ResponSchool
import com.media.schoolday.models.model.ResponUser
import com.media.schoolday.utility.PfUtil
import com.orhanobut.wasp.Callback
import com.orhanobut.wasp.Response
import com.orhanobut.wasp.WaspError
import io.realm.Realm
import kotlinx.android.synthetic.main.content_main.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.longToast
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.json.JSONObject

class MainActivity : AppCompatActivity(),AnkoLogger {
    private val  RC_SIGN_IN = 123

    private val auth = FirebaseAuth.getInstance()
    lateinit var fab:FloatingActionButton
    lateinit var pageAdapter:TabPageAdapter
    val pageTitle = arrayOf("News","Activity")

    private val realm = Realm.getDefaultInstance()

    val token: String
        get() {
            val userid = SchoolApp.prefs!!.loadJSONObject("user","user")
            return userid.get("token").toString()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        supportActionBar!!.title = "Media Sekolah"
        fab = findViewById(R.id.fab) as FloatingActionButton
//        fab.hide()
        fab.setOnClickListener {
           startActivity<NewsActivity>("post" to "new")
        }
        createTab()
        checkAccount()

    }

    private fun checkAccount() {
        val user = auth.currentUser
        if(user == null)
            login()
        else{
            userRegister(user)
            getProfile()
            getSekolah()
        }
    }

    private fun getSekolah() {

        SchoolApp.rest!!.getSekolah(object: Callback<ResponSchool>{
            override fun onError(error: WaspError?) {
                toast("Network Failed")
            }

            override fun onSuccess(response: Response?, t: ResponSchool?) {
                val data = JSONObject(response!!.body)
                SchoolApp.prefs!!.saveJSONObject("sekolah","sekolah",data)
            }
        })
    }

    private fun getProfile() {
        SchoolApp.rest!!.getProfile(token,object: Callback<ResponProfile>{
            override fun onError(error: WaspError?) {
            }

            override fun onSuccess(response: Response?, t: ResponProfile?) {
                val data = JSONObject(response!!.body)
                PfUtil.saveJsonObject("user","profile",data)
            }
        })
    }

    private fun login(){
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setIsSmartLockEnabled(false)
                        .build(),
                RC_SIGN_IN)
    }

    private fun userRegister(user: FirebaseUser) {
        val map = HashMap<String,String>()
        map.put("name",user.displayName!!)
        map.put("email",user.email!!)
        map.put("uid",user.uid)
        SchoolApp.rest!!.getUser(map, object : Callback<ResponUser> {
            override fun onSuccess(response: Response?, t: ResponUser?) {
                val userRef = JSONObject(response!!.body)
                SchoolApp.prefs!!.saveJSONObject("user","user",userRef)
            }

            override fun onError(error: WaspError?) {
                longToast("Network Failed")
            }
        })

    }

    private fun createTab() {
        pageAdapter = TabPageAdapter(pageTitle,supportFragmentManager)
        view_pager.adapter = pageAdapter
        view_pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tab_layout))
        tab_layout.apply({
            setupWithViewPager(view_pager)
            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabReselected(tab: TabLayout.Tab?) {}

                override fun onTabUnselected(tab: TabLayout.Tab?) {}

                override fun onTabSelected(tab: TabLayout.Tab?) {
                    if (tab != null) {
                        view_pager.currentItem = tab.position
                        if (tab.position == 0) {
//                            if(data > 0)
                            fab.show()
                        } else fab.hide()
                    }
                }
            })
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == RC_SIGN_IN && resultCode == ResultCodes.OK){
            val user = auth.currentUser
            userRegister(user!!)
            startActivity<NewsActivity>("post" to "account")
        }else{
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setIsSmartLockEnabled(false)
                            .build(),
                    RC_SIGN_IN)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_logout -> {
                auth.signOut()
                login()
                return true
            }
            R.id.action_account -> {
                startActivity<NewsActivity>("post" to "account")
                return true
            }

        }

        return super.onOptionsItemSelected(item)
    }

    private fun getNews(){

        SchoolApp.rest!!.getNews(token, object : Callback<ResponNews> {
            override fun onSuccess(response: Response?, t: ResponNews?) {
                val news = JSONObject(response!!.body)
                SchoolApp.prefs!!.saveJSONObject("sekolah","news",news)
//                val newsItem = ArrayList<NewsEntity>()
//                for(data in t!!.data){
//                    val news = NewsEntity()
//                    news.apply {
//                        id = data.id
//                        sekolah = data.sekolah
//                        title = data.title
//                        topic = data.topic
//                        description = data.description
//                        category = data.category
//                        publish = data.publish
//                        userId = data.userId
//                        userName = data.userName
//                        userTitle = data.userTitle
//                        timeCreated = data.timeCreated
//                    }
//                    newsItem.add(news)
//                    newsItem.filter { it.sekolah.equals("sekolah") }
//                }
////                removeNews()
//                realm.beginTransaction()
//                realm.copyToRealmOrUpdate(newsItem)
//                realm.commitTransaction()
//
//                val newsFragment = pageAdapter.getItem(0) as HomeFragment
//                newsFragment.updateAdapter()

            }
            override fun onError(error: WaspError?) {}
        })
    }


}
