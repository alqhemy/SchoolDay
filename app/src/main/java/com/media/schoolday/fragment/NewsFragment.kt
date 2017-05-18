package com.media.schoolday.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
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
import com.kosalgeek.android.photoutil.CameraPhoto
import com.kosalgeek.android.photoutil.GalleryPhoto
import com.kosalgeek.android.photoutil.ImageLoader
import com.kosalgeek.android.photoutil.PhotoLoader
import com.media.schoolday.R
import com.media.schoolday.utility.DbLocal
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
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
    private val cameraPhoto =  CameraPhoto(getApplicationContext());
    private val GALERY_REQUEST = 100
    private val CAMERA_REQUEST = 200
    private val realm = Realm.getDefaultInstance()

    interface OnItemSelectedListener {
        fun onPictureClick(link: String)
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
            callback!!.onPictureClick("takepicture")
        }
        ibNewsPostChild.setOnClickListener {

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

    fun getListClass():ArrayList<String>?{
        val profile = DbLocal.getProfile()
        val sekolah = DbLocal.schoolList()
        if(sekolah != null) {
            val filter = sekolah.filter { it.nama == profile!!.teacher.first().sekolah }
            return filter.first().kelas
        }else{
            return null
        }
    }

    private fun removePhoto() {
        linear.removeAllViews()
        listPhoto.clear()
        activity.toast("clear photo")
    }

    fun takePhoto(option: String){
        when(option){
            "Galery" ->{
                startActivityForResult(galeryPhoto.openGalleryIntent(),GALERY_REQUEST)
            }
            "Camera" ->{
                startActivityForResult(cameraPhoto.takePhotoIntent(),CAMERA_REQUEST)
            }
            else -> {
                CropImage.activity(null)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(context, this@NewsFragment);
            }
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(resultCode){
            RESULT_OK -> {
                when(requestCode){
                    GALERY_REQUEST -> {
                        galeryPhoto.setPhotoUri(data!!.data)
                        addPhoto(galeryPhoto.path, "galery")
                    }
                    CAMERA_REQUEST -> {
                        addPhoto(cameraPhoto.photoPath,"camera")
                    }
                    CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                        val result = CropImage.getActivityResult(data)
                        addPhoto(result.uri.path,"crop")
                    }
                }
            }
        }
    }

    fun addPhoto(photo: String,option: String){
        listPhoto.add(photo)
        var bitmap: Bitmap? = null
        try {
            if(option == "galery") {
                bitmap = PhotoLoader.init().from(galeryPhoto.path).requestSize(512, 512).bitmap
            }
            else{
                try {
                    bitmap = ImageLoader.init().from(photo).requestSize(512, 512).getBitmap()
                }catch (e: FileNotFoundException){
                    e.printStackTrace()
                }
            }

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