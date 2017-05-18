package com.media.schoolday

import android.app.ProgressDialog
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
import com.google.firebase.auth.FirebaseUser
import com.media.schoolday.activity.NewsActivity
import com.media.schoolday.adapter.TabPageAdapter
import com.media.schoolday.fragment.HomeFragment
import com.media.schoolday.models.model.*
import com.media.schoolday.utility.DbLocal
import com.media.schoolday.utility.PfUtil
import com.orhanobut.wasp.Callback
import com.orhanobut.wasp.Response
import com.orhanobut.wasp.WaspError
import kotlinx.android.synthetic.main.content_main.*
import org.jetbrains.anko.*
import org.json.JSONObject

class MainActivity : AppCompatActivity(),AnkoLogger {

    private val  RC_SIGN_IN = 123
    private val  RC_PROFILE = 112
    lateinit var fab:FloatingActionButton
    lateinit var pageAdapter:TabPageAdapter
    val pageTitle = arrayOf("Berita","Aktivitas")
    lateinit  var progressDialog: ProgressDialog
//    private val realm = Realm.getDefaultInstance()

    interface OnItemClickListener {
        fun OnDataUpdate(args: ArrayList<NewsModel>)
    }
    var callback: OnItemClickListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        callback = ctx as? OnItemClickListener
        setContentView(R.layout.activity_main)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        supportActionBar!!.title = "Media Sekolah"
        fab = findViewById(R.id.fab) as FloatingActionButton
        fab.hide()
        fab.setOnClickListener {
           startActivity<NewsActivity>("post" to "new","id" to "0")
        }
//        progressDialog = ProgressDialog(this@MainActivity)
//        progressDialog.setMessage("Please wait...")
//        progressDialog.show()
        createTab()
        checkAccount()
//        progressDialog.hide()
    }

    private fun checkAccount() {
        val user = SchoolApp.user?.currentUser
        if(user == null)
            login()
        else {

            getSekolah()
            userRegister("current", user)
        }
    }

    private fun getSekolah() {

        SchoolApp.rest!!.getSekolah(object: Callback<ResponSchool>{
            override fun onError(error: WaspError?) {
                toast("Network Failed")
            }

            override fun onSuccess(response: Response?, t: ResponSchool?) {
                val data = JSONObject(response?.body)
                SchoolApp.prefs!!.saveJSONObject("sekolah","sekolah",data)

            }
        })
    }

    private fun getProfile(token: String) {
            SchoolApp.rest!!.getProfile(token,object: Callback<ResponProfile>{
                override fun onError(error: WaspError?) {
//                    newsAdapter(DbLocal.newsList())
                }
                override fun onSuccess(response: Response?, t: ResponProfile?) {
                    val data = JSONObject(response!!.body)
                    PfUtil.saveJsonObject("user","profile",data)
                    if(t!!.teacher.size > 0){
                        fab.show()
                    }
                    getNews(token)
                }
            })
    }
    fun getNews(token: String){

        SchoolApp.rest?.getNews(token, object : Callback<ResponNews> {
            override fun onError(error: WaspError?) {
                toast("Network Failed")
            }
            override fun onSuccess(response: Response?, t: ResponNews?) {
                val data = JSONObject(response?.body)
                SchoolApp.prefs?.saveJSONObject("sekolah", "news", data)
                newsAdapter(t!!.data)
            }
        })
    }
    fun newsAdapter(data: ArrayList<NewsModel>){
        val home = pageAdapter.getFragment(0) as HomeFragment
        home.updateAdapter(data)

        val activities = pageAdapter.getFragment(1) as HomeFragment
        activities.updateAdapter(newsFilter(data))
    }
    private fun login(){
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setIsSmartLockEnabled(false)
                        .build(),
                RC_SIGN_IN)

    }

    private fun userRegister(mode: String ,user: FirebaseUser) {

        val map = HashMap<String,String>()
        map.put("name",user.displayName!!)
        map.put("email",user.email!!)
        map.put("uid",user.uid)
        SchoolApp.rest!!.getUser(map, object : Callback<ResponUser> {
            override fun onSuccess(response: Response?, t: ResponUser?) {
                val userRef = JSONObject(response!!.body)
                SchoolApp.prefs!!.saveJSONObject("user", "user", userRef)
                getProfile(userRef.getString("token"))
                if (mode == "new") {
                    startActivityForResult<NewsActivity>(RC_PROFILE, "post" to "account","id" to "0")
                    getNews(DbLocal.token())
                }
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

                override fun onTabSelected(tab: TabLayout.Tab?) {}
            })
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == RC_SIGN_IN) {
            if (resultCode == ResultCodes.OK) {
                val user = SchoolApp.user?.currentUser
                userRegister("new",user!!)
            }
            else{
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setIsSmartLockEnabled(false)
                                .build(),
                        RC_SIGN_IN)
            }
        }

        if(requestCode == RC_PROFILE){
            val title = data?.extras?.get("title")
            if(resultCode == ResultCodes.OK){

//                val title = data?.extras?.get("title")
                if(title == "account") {
                    info { title }
//                    updateHomeNew()
                }
            }
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
                SchoolApp.user?.signOut()
//                clear cache
                PfUtil.clear("user","user")
                PfUtil.clear("user","profile")
                PfUtil.clear("sekolah","news")

                login()
                return true
            }

            R.id.action_account -> {
                startActivity<NewsActivity>("post" to "account","id" to "0")
                return true
            }

            R.id.action_update -> {
                updateHomeNew()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    fun updateHomeNew(){
       getProfile(DbLocal.token())
    }

    override fun onResume() {
//        updateHomeNew()
        super.onResume()

    }

    fun newsFilter(news: ArrayList<NewsModel>): ArrayList<NewsModel>{
        val list = ArrayList<String>()
        if (DbLocal.getProfile()?.child!!.isNotEmpty()){
            list.addAll(DbLocal.getProfile()!!.child.map { it.kelas } as ArrayList<String>)
        }
        info { list.toString() }
        val filter = news.filter { it.topic in list || it.userId == DbLocal.getProfile()!!.user.email } as ArrayList<NewsModel>
        return filter
    }
}
