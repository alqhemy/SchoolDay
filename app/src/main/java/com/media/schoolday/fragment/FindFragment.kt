package com.media.schoolday.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import com.media.schoolday.R
import com.media.schoolday.models.model.Student
import com.media.schoolday.models.model.Teacher
import kotlinx.android.synthetic.main.fragment_find.view.*
import org.jetbrains.anko.AnkoLogger

/*
 * Created by yosi on 10/05/2017.
 */

class FindFragment: Fragment(),AnkoLogger{

    val FIND_KEY = "find_key"
    var find: String? = null
    lateinit var lv:ListView
    lateinit var adapterStudent: ArrayAdapter<Student>
    lateinit var adapterTeacher:ArrayAdapter<Teacher>

    interface FindListener {
        fun AlertMesage(s: String)
        fun onProcess(data: HashMap<String,String>) {
        }
    }
    var callback: FindListener? = null

    companion object {
        fun newInstance(s: String): FindFragment {
            val fragment = newInstance()
            val args = Bundle()
            args.putString("title", s)
            fragment.arguments = args
            return fragment
        }
        fun newInstance(): FindFragment {
            return FindFragment()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(arguments != null && arguments.containsKey(FIND_KEY)){
            find = arguments.getString(FIND_KEY)
        }
        callback = activity as? FindListener
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.fragment_find,container,false)
//        lv = view.findViewById(R.id.lvFind) as ListView
        if(find.equals("students"))
            view.etAccountRegisterNik.hint = "Nomor induk siswa"
        else
            view.etAccountRegisterNik.hint = "Nomor induk guru"
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view!!.btAccoutRegister.setOnClickListener({
            callback!!.AlertMesage("Data akan di proses")
        })
    }

//    private fun getTeacherData() {
//        val students = DbLocal.studentList()
//        if( students.size == 0) {
//            val map = HashMap<String, String>()
//            val filter = map.put("sekolah", data)
//            map.put("filter", filter!!)
//            Rest.getTeachers(map, object : Callback<Teachers> {
//                override fun onError(error: WaspError?) {
//                    //                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//                }
//
//                override fun onSuccess(response: Response?, t: Teachers?) {
//                    val array = JSONObject(response!!.body)
//                    PfUtil.saveJsonObject("sekolah", "guru", array)
//                    adapterTeacher.addAll(t!!.data)
//                    adapterTeacher.notifyDataSetChanged()
//
//                }
//            })
//        }
//
//    }
//
//    private fun getStudentData() {
//        val teachers = DbLocal.studentList()
//        if( teachers.size == 0) {
//            val data = lv.spFind.selectedItem.toString()
//            val map = HashMap<String, String>()
//            val filter = map.put("sekolah", data)
//            map.put("filter", filter!!)
//            Rest.getStudents(map, object : Callback<Students> {
//                override fun onError(error: WaspError?) {
////                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//                }
//                override fun onSuccess(response: Response?, t: Students?) {
//                    val array = JSONObject(response!!.body)
//                    PfUtil.saveJsonObject("sekolah", "siswa", array)
//                    adapterStudent.addAll(t!!.data)
//                    adapterStudent.notifyDataSetChanged()
//                }
//            })
//        }
//    }
//
//    private fun schoolSpinnerList(v: View) {
//        val list = DbLocal.schoolList()
//        val school = ArrayList<String>()
//        if(list.isNotEmpty()) {
//            list.forEach {
//                school.add(it.nama!!)
//
//                val spinner = v.findViewById(R.id.spFind) as Spinner
//                spinner.onItemSelectedListener
//                val adapter = ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, school)
//                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//                spinner.adapter = adapter
//            }
//        }
//    }

}
