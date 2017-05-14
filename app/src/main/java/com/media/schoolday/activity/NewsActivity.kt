package com.media.schoolday.activity

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.media.schoolday.R
import com.media.schoolday.SchoolApp
import com.media.schoolday.fragment.AcountFragment
import com.media.schoolday.fragment.NewsFragment
import com.media.schoolday.models.model.*
import com.media.schoolday.utility.DbLocal
import com.media.schoolday.utility.PfUtil
import com.orhanobut.wasp.Callback
import com.orhanobut.wasp.Response
import com.orhanobut.wasp.WaspError
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.toolbar
import org.json.JSONObject

class NewsActivity : AppCompatActivity(), AnkoLogger,
        NewsFragment.OnItemSelectedListener,
        AcountFragment.OnItemClickListener {
    lateinit var title:String
    var responStatus = false
    lateinit var progres: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progres = ProgressDialog(this@NewsActivity)
        setContentView(R.layout.activity_news)
        val toolbar = findViewById(R.id.toolbarNews) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
        title = intent.getStringExtra("post")
        if (savedInstanceState == null) {
            getFragment(title)
        }

        with(progres){
            setMessage("Please wait...")
            setCancelable(false)
        }
    }

    fun getFragment(title: String) {
        when(title){
            "new" -> {
                supportActionBar!!.title = "New Post"
                changeFragment(NewsFragment.getInstance("News Post"),"newsPost") }

            "account" -> {
                supportActionBar!!.title = "Account User"
                changeFragment(AcountFragment.newInstance("Account"),"account")}

        }
    }
    fun changeFragment(f: Fragment, tag: String, cleanStack: Boolean = false) {
        val ft = supportFragmentManager.beginTransaction()
        if (cleanStack) {
            clearBackStack()
        }
        with(ft){
            setCustomAnimations(
                    R.anim.abc_fade_in, R.anim.abc_fade_out, R.anim.abc_popup_enter, R.anim.abc_popup_exit)
            replace(R.id.news_fragment, f, tag)

            addToBackStack(null)
            commit()
        }

    }

    fun clearBackStack() {
        val manager = supportFragmentManager
        if (manager.backStackEntryCount > 0) {
            val first = manager.getBackStackEntryAt(0)
            manager.popBackStack(first.id, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }
    override fun onBackPressed() {
        val fragmentManager = supportFragmentManager
        if (fragmentManager.backStackEntryCount > 1) {
            fragmentManager.popBackStack()
        } else {
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onGetList(args: String):Boolean {
        registrasi()
        return responStatus
    }

    fun registrasi(){
        val sekolah = ArrayList<String>()
//        var accoutRegister :String? = null
        DbLocal.schoolList().forEach { sekolah.add(it.nama!!)  }

        val account = listOf("Orang tua","Guru")

        selector("Pilih Accout",account) { i ->
            val register = account[i]
            selector("Pilih Sekolah",sekolah){i ->

                val cari = FilterModel(FilterArg(sekolah[i]))

                alert {
                    customView {
                        verticalLayout {
                            toolbar {
                                id = R.id.dialog_toolbar
                                lparams(width = matchParent, height = wrapContent)
                                backgroundColor = ContextCompat.getColor(ctx, R.color.colorPrimary)
                                setTitleTextColor(ContextCompat.getColor(ctx, android.R.color.white))
                                if(register == account[0])
                                    title = "Nomor Nis anak"
                                else
                                    title = "Nomor Handphone anda"
                            }

                            val nis = editText { padding = dip(20) }
                            positiveButton("REGISTER") {
                                val arg = nis.text.toString()

                                if(register == (account[0]))
                                    getAnak(cari, arg)
                                else
                                    getGuru(cari, arg)

                            }
                        }
                    }
                }.show()
            }
        }


    }

    private fun getGuru(filter: FilterModel, telp: String) {

        SchoolApp.rest!!.getGuru(filter, object : Callback<Teachers>{
            override fun onSuccess(response: Response?, t: Teachers?) {
                val data = JSONObject(response!!.body)
                PfUtil.saveJsonObject("sekolah","guru",data)
                val cari = t!!.data.filter { it.telp == (telp) }
                with(cari) {
                    when {
                        isNotEmpty() -> {
                            forEach {
                                alert(it.nama + ", Proses?") {
                                    yesButton { updateProfileGuru(ProfileGuru(Guru(it.id,it.sekolah))) }
                                }.show()
                            }
                        }
                        else -> {
                            alert("data tidak ditemukan") { yesButton {} }.show()

                        }
                    }
                }
            }

            override fun onError(error: WaspError?) {
                alert("network error"){yesButton {  }}.show()
            }
        })
    }

    private fun getAnak(filter: FilterModel, nik: String) {
        progres.show()
        SchoolApp.rest!!.getSiswa(filter, object : Callback<Students> {
                override fun onSuccess(response: Response?, t: Students?) {
                    progres.hide()
                    val data = JSONObject(response!!.body)
                    PfUtil.saveJsonObject("sekolah", "siswa", data)
                    val cari = t!!.data.filter { it.nis == (nik) }
                    with(cari) {
                        when {
                            isNotEmpty() -> {
                                forEach {
                                    alert(it.nama + ", Proses?") {
                                        yesButton { updateProfileAnak(ProfilAnak(Anak(it.sekolah, it.nis))) }
                                    }.show()
                                }
                            }
                            else -> {
                                alert("data tidak ditemukan") { yesButton {} }.show()

                            }
                        }
                    }
                }

                override fun onError(error: WaspError?) {
                    progres.hide()
                    alert("network error") { yesButton { } }.show()
                }
            })
    }

    private fun updateProfileAnak(anak: ProfilAnak){
        progres.show()
        SchoolApp.rest!!.putProfileAnak(DbLocal.token(),anak, object : Callback<Profile>{
            override fun onError(error: WaspError?) {
                progres.hide()
                alert("Network Failed"){yesButton {}}.show()
            }
            override fun onSuccess(response: Response?, t: Profile?) {
                progres.hide()
                responStatus = true
                getProfile()
                alert("Profile sudah di update"){yesButton {}}.show()

            }
        })
    }
    private fun updateProfileGuru(guru: ProfileGuru){
        progres.show()
        SchoolApp.rest!!.putProfileGuru(DbLocal.token(),guru, object : Callback<Profile>{
            override fun onError(error: WaspError?) {
                progres.hide()
                alert("Network Failed"){yesButton {}}.show()
            }
            override fun onSuccess(response: Response?, t: Profile?) {
                progres.hide()
                responStatus = true
                getProfile()
                alert("Profile sudah di update"){yesButton {}}.show()

            }
        })
    }


    fun getProfile() {
        SchoolApp.rest!!.getProfile(DbLocal.token(),object: Callback<ResponProfile>{
            override fun onError(error: WaspError?) {
            }

            override fun onSuccess(response: Response?, t: ResponProfile?) {
                val data = JSONObject(response!!.body)
                PfUtil.saveJsonObject("user","profile",data)
                val accout = supportFragmentManager.findFragmentByTag("account") as AcountFragment
                accout.updateAdapter()

            }
        })
    }

    override fun onSchoolClick(link: String) {
        alert("Profile sudah di update $link"){yesButton {}}.show()
        debug { link }
    }
}
