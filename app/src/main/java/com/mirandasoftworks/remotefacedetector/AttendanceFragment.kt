package com.mirandasoftworks.remotefacedetector

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.mirandasoftworks.remotefacedetector.databinding.FragmentAttendanceBinding
import com.mirandasoftworks.remotefacedetector.model.Person
import java.sql.Timestamp
import java.time.LocalDate
import java.time.ZoneId


class AttendanceFragment : Fragment() {

    private var _binding: FragmentAttendanceBinding? = null
    private val binding get() = _binding!!


    private var db = Firebase.firestore

    private lateinit var attendanceAdapter: AttedanceAdapter

    private lateinit var searchArrayList : ArrayList<Person>

    private var searchState = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAttendanceBinding.inflate(inflater, container, false)
        val root: View = binding.root

        searchArrayList = arrayListOf()

        @Suppress("DEPRECATION")
        setHasOptionsMenu(true)

        val sharedPreferences = this.requireActivity().getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE)
        if (sharedPreferences.getString("accountType", "") == "mahasiswa"){
            getLecturerData()
        } else if (sharedPreferences.getString("accountType", "") == "dosen/karyawan"){
            getLecturerData()
        } else if (sharedPreferences.getString("accountType", "") == "pejabat"){
            getAllData()
        } else{
            getAllData()
        }

        Log.d("account_type", "${sharedPreferences.getString("accountType", "")}")

        attendanceAdapter = AttedanceAdapter()

        with(binding){
            rvSearch.layoutManager = LinearLayoutManager(activity)
            rvSearch.setHasFixedSize(true)
            rvSearch.adapter = attendanceAdapter
        }


        return root
    }
    private fun getAllData() {
        val startOfDay = LocalDate.now().atStartOfDay(ZoneId.of("Asia/Jakarta")).toInstant().toEpochMilli()
        val startOfDayTimestamp = Timestamp(startOfDay)
        Log.d("timestamp", startOfDay.toString())
        Log.d("timestamp", startOfDayTimestamp.toString())


        val endOfDay = LocalDate.now().atStartOfDay(ZoneId.of("Asia/Jakarta")).plusDays(1).toInstant().toEpochMilli()
        val endOfDayTimestamp = Timestamp(endOfDay)
        Log.d("timestamp", endOfDay.toString())
        Log.d("timestamp", endOfDayTimestamp.toString())

        db.collection("presensi")
//            .whereGreaterThanOrEqualTo("datetime", startOfDayTimestamp)
//            .whereLessThan("datetime", endOfDayTimestamp)
            .orderBy("datetime", Query.Direction.DESCENDING)
            .addSnapshotListener(object : EventListener<QuerySnapshot>{
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (!searchState){
                        searchArrayList.clear()
                        Log.d("arraySearchList state", searchState.toString())
                        for (document in value!!){
                            searchArrayList.add(document.toObject(Person::class.java))
                            attendanceAdapter.setData(searchArrayList)
                            Log.d("arraySearch", document.toObject<Person>().toString())
                            Log.d("arraySearch", document.get("nama").toString())
                            Log.d("arraySearchList", searchArrayList.toString())
                            Log.d("arraySearchList", searchArrayList.size.toString())
                        }
                        if (searchArrayList.isEmpty()){
                            binding.tvNoData.visibility = View.VISIBLE
                        } else{
                            binding.tvNoData.visibility = View.GONE
                            attendanceAdapter.notifyDataSetChanged()
                        }

                    } else {
                        Log.d("arraySearchList state", searchState.toString())
//                        binding.tvNoData.visibility = View.VISIBLE
                    }
                }
            })
    }
    private fun getLecturerData() {
        val startOfDay = LocalDate.now().atStartOfDay(ZoneId.of("Asia/Jakarta")).toInstant().toEpochMilli()
        val startOfDayTimestamp = Timestamp(startOfDay)
        Log.d("timestamp", startOfDay.toString())
        Log.d("timestamp", startOfDayTimestamp.toString())


        val endOfDay = LocalDate.now().atStartOfDay(ZoneId.of("Asia/Jakarta")).plusDays(1).toInstant().toEpochMilli()
        val endOfDayTimestamp = Timestamp(endOfDay)
        Log.d("timestamp", endOfDay.toString())
        Log.d("timestamp", endOfDayTimestamp.toString())

        db.collection("presensi")
            .whereEqualTo("jenis_pekerjaan", "dosen")
//            .whereGreaterThanOrEqualTo("datetime", startOfDayTimestamp)
//            .whereLessThan("datetime", endOfDayTimestamp)
            .orderBy("datetime", Query.Direction.DESCENDING)
            .addSnapshotListener(object : EventListener<QuerySnapshot>{
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (!searchState){
                        searchArrayList.clear()
                        Log.d("arraySearchList", searchState.toString())
                        for (document in value!!){
                            searchArrayList.add(document.toObject(Person::class.java))
                            attendanceAdapter.setData(searchArrayList)
                        }
                        if (searchArrayList.isEmpty()){
                            binding.tvNoData.visibility = View.VISIBLE
                        } else{
                            binding.tvNoData.visibility = View.GONE
                            attendanceAdapter.notifyDataSetChanged()
                        }
                    }
                }
            })
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        @Suppress("DEPRECATION")
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.main_menu, menu)
        val searchView = context?.let { SearchView(it) }
        menu.findItem(R.id.menu_search).apply {
            setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW or MenuItem.SHOW_AS_ACTION_IF_ROOM)
            actionView = searchView
        }

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {

                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isEmpty()){
                    searchState = false
                    Log.d("searchStateToFalse", searchState.toString())
                    attendanceAdapter.setData(searchArrayList)
                    val sharedPreferences = requireActivity().getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE)
                    if (sharedPreferences.getString("accountType", "") == "mahasiswa"){
                        getLecturerData()
                    } else if (sharedPreferences.getString("accountType", "") == "dosen/karyawan"){
                        getLecturerData()
                    } else if (sharedPreferences.getString("accountType", "") == "pejabat"){
                        getAllData()
                    } else{
                        getAllData()
                    }
                    Log.d("searchStateToFalse", searchArrayList.toString())
                } else{
                    searchState = true
                    attendanceAdapter.filter.filter(newText)
                    Log.d("searchStateToTrue", searchState.toString())
                }

                return false
            }

        })

    }

    override fun onStart() {
        super.onStart()
        Log.d("firebaseFirestoreListener", "onStart")
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Log.d("firebaseFirestoreListener", "onDestroyView")
    }


}