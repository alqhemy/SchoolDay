package com.media.schoolday.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.media.schoolday.R
import com.media.schoolday.SchoolApp
import com.media.schoolday.models.model.ResponProfile
import com.media.schoolday.utility.DbLocal
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import org.jetbrains.anko.AnkoLogger


class AcountFragment: Fragment(), AnkoLogger {
    lateinit var adapterChild:ArrayAdapter<String>
    lateinit var viewAccount : View

    interface OnItemClickListener {
        fun onGetList(args: String):Boolean

        fun OnProfileUpdate(args: String)
    }
    var callback: OnItemClickListener? = null

    companion object {
        fun newInstance(s: String): AcountFragment {
            val fragment = newInstance()
            val args = Bundle()
            args.putString("title", s)
            fragment.arguments = args
            return fragment
        }
        fun newInstance(): AcountFragment {
            return AcountFragment()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        callback = activity as? OnItemClickListener
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        viewAccount = inflater!!.inflate(R.layout.fragment_profile,container,false)

        val profile = DbLocal.getProfile()
        adapterChild = object : ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, getArrayData(profile)) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
                return super.getView(position, convertView, parent)
            }
        }
        adapterChild.setNotifyOnChange(true)
        viewAccount.apply {
            tvProfileEmail.text = DbLocal.getProfile()?.user?.email
            tvProfileName.text = DbLocal.getProfile()?.user?.name
            listAccountProfile.adapter = adapterChild
        }

        return viewAccount
    }

    fun getArrayData(data: ResponProfile?): ArrayList<String>{
        val listArray = ArrayList<String>()
        with(listArray){
            data?.child?.forEach { add(it.nama) }
            data?.teacher?.forEach { add(it.nama) }
        }
        return listArray
    }
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvAccountAnak.setOnClickListener { callback!!.onGetList("students") }
        val user = SchoolApp.user?.currentUser
        tvProfileEmail.text = user?.email
        tvProfileName.text = user?.displayName
    }

    fun updateAdapter(){
        with(adapterChild){
            clear()
            notifyDataSetChanged()

            val data = getArrayData(DbLocal.getProfile())
            addAll(data)
            notifyDataSetChanged()
        }

    }

    override fun onDetach() {
        callback = null
        super.onDetach()
    }

}


