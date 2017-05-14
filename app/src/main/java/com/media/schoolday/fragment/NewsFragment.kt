package com.media.schoolday.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import com.facebook.FacebookSdk.getApplicationContext
import com.kosalgeek.android.photoutil.GalleryPhoto
import com.kosalgeek.android.photoutil.PhotoLoader
import com.media.schoolday.R
import com.media.schoolday.utility.DbLocal
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_news.*
import kotlinx.android.synthetic.main.fragment_news_post.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.bottomPadding
import org.jetbrains.anko.imageBitmap
import org.jetbrains.anko.toast
import java.io.FileNotFoundException



class NewsFragment: Fragment(), AnkoLogger {

    lateinit private var linear:LinearLayout
    lateinit var spinner: Spinner
    private val listPhoto:ArrayList<String> = ArrayList()
    private val galeryPhoto = GalleryPhoto(getApplicationContext())
    private val GALERY_REQUEST = 100
    private val realm = Realm.getDefaultInstance()

    interface OnItemSelectedListener {
        fun onSchoolClick(link: String)
    }
    var callback: OnItemSelectedListener? = null


    companion object {
        fun getInstance(s: String):NewsFragment {
            val fragment = HomeFragment()
            val args = Bundle()
            args.putString("title",s)
            fragment.arguments = args
            return NewsFragment()
        }

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =  inflater!!.inflate(R.layout.fragment_news_post,container,false)
        linear = view.findViewById(R.id.linearMain) as LinearLayout
        callback = activity as? OnItemSelectedListener
        getSpinClass(view)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity.toolbarNews.title = "News Post"

        ibNewsPostDelete.setOnClickListener { removePhoto()}
        ibNewsPostImage.setOnClickListener {
            startActivityForResult(galeryPhoto.openGalleryIntent(),GALERY_REQUEST)
        }
        ibNewsPostChild.setOnClickListener {
            callback!!.onSchoolClick(spinner.selectedItem.toString())
        }
        btNewsPostDraft.setOnClickListener { }
        btNewsPostSend.setOnClickListener { }
    }
    fun getSpinClass(v: View?){

        spinner = v!!.findViewById(R.id.spNewsPostTopic) as Spinner
        spinner.onItemSelectedListener
        val adapter = ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, getListClass())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    fun getListClass():ArrayList<String>{
        val profile = DbLocal.getProfile()
        val sekolah = DbLocal.schoolList()
        val kelas  = sekolah.filter { it.nama == profile.teacher.sekolah }.first()
        return kelas.kelas!!
    }

    private fun removePhoto() {
        linear.removeAllViews()
        listPhoto.clear()
        activity.toast("clear photo")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK){
            if(requestCode == GALERY_REQUEST){
                galeryPhoto.setPhotoUri(data!!.data)
                addPhoto(galeryPhoto.path)
            }

        }
    }

    fun addPhoto(photo: String){
        listPhoto.add(photo)
        try {
            val bitmap = PhotoLoader.init().from(galeryPhoto.path).requestSize(512,512).bitmap
            val imageView = ImageView(getApplicationContext())
            val layoutParam = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
            imageView.apply {
                layoutParams = layoutParam
                scaleType = ImageView.ScaleType.FIT_CENTER
                bottomPadding = 10
                adjustViewBounds = true
                imageBitmap = bitmap
            }

            linear.addView(imageView)
        }catch (e: FileNotFoundException) {
            activity.toast("something wrong")
        }
    }



}