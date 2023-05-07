package com.mirandasoftworks.remotefacedetector

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
import com.mirandasoftworks.remotefacedetector.databinding.FragmentSearchBinding
import com.mirandasoftworks.remotefacedetector.model.Dosen
import java.sql.Timestamp
import java.time.LocalDate
import java.time.ZoneId


class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var searchAdapter: SearchAdapter

    private lateinit var searchArrayList : ArrayList<Dosen>
    private lateinit var searchArrayListFull : ArrayList<Dosen>
    private lateinit var searchArrayListFullTemp : ArrayList<Dosen>

    private var searchState = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        searchArrayList = arrayListOf()
        searchArrayListFull = arrayListOf()
        searchArrayListFullTemp = arrayListOf()

        @Suppress("DEPRECATION")
        setHasOptionsMenu(true)

        eventChangeListener()

        searchAdapter = SearchAdapter()

        with(binding){
            tvNoData.visibility = View.GONE
            rvSearch.layoutManager = LinearLayoutManager(activity)
            rvSearch.setHasFixedSize(true)
            rvSearch.adapter = searchAdapter
        }

//        if (!searchState){
//            eventChangeListener()
//        } else{
//            Log.d("timestamp", "Aaa")
//        }

        return root
    }
    private fun eventChangeListener() {


        val startOfDay = LocalDate.now().atStartOfDay(ZoneId.of("Asia/Jakarta")).toInstant().toEpochMilli()
        val startOfDayTimestamp = Timestamp(startOfDay)
        Log.d("timestamp", startOfDay.toString())
        Log.d("timestamp", startOfDayTimestamp.toString())


        val endOfDay = LocalDate.now().atStartOfDay(ZoneId.of("Asia/Jakarta")).plusDays(1).toInstant().toEpochMilli()
        val endOfDayTimestamp = Timestamp(endOfDay)
        Log.d("timestamp", endOfDay.toString())
        Log.d("timestamp", endOfDayTimestamp.toString())




        val db = Firebase.firestore
        db.collection("presensi")
//            .whereEqualTo("pekerjaan", "dosen")

//            .whereGreaterThanOrEqualTo("datetime", startOfDayTimestamp)
//            .whereLessThan("datetime", endOfDayTimestamp)
//            .whereEqualTo("nama", searchQuery)
            .orderBy("datetime", Query.Direction.DESCENDING)
            .addSnapshotListener(object : EventListener<QuerySnapshot>{
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (!searchState){
                        searchArrayList.clear()
                        for (document in value!!){
                            searchArrayList.add(document.toObject(Dosen::class.java))
                            searchAdapter.setData(searchArrayList)
                            Log.d("arraySearch", document.toObject<Dosen>().toString())
                            Log.d("arraySearch", document.get("nama").toString())
                            Log.d("arraySearchList", searchArrayList.toString())
                            /***
                            D/arraySearchList: [Dosen(nama=Yosua Alvin, lokasi=null, date=null, time=null, alat_id=f4:7b:09:0d:c3:20, datetime=Timestamp(seconds=1681277366,
                            nanoseconds=92067000), tipe_akun=null)]
                            ***/


//                            db.collection("akun")
//                                .whereEqualTo("nama", document.get("nama").toString())
//                                .addSnapshotListener(object : EventListener<QuerySnapshot>{
//                                    override fun onEvent(
//                                        value: QuerySnapshot?,
//                                        error: FirebaseFirestoreException?
//                                    ) {
//                                        for (documents in value!!){
//                                            Log.d("arraySearchh", documents.toString())
//                                            Log.d("arraySearchh", documents.toObject<Dosen>().toString())
//                                            /***
//                                            Dosen(nama=Raden, lokasi=null, date=null, time=null, alat_id=null, datetime=null, tipe_akun=mahasiswa)
//                                             ***/
//
//                                            Log.d("arraySearchh", documents.get("tipe_akun").toString())
//
//                                            if (documents.get("tipe_akun").toString() == "mahasiswa"){
//
//                                                val int = searchArrayListFullTemp.indexOf(Dosen(nama = "Ilham"))
//                                                Log.d("arraySearchh", int.toString())
//                                                searchArrayListFullTemp.add(documents.toObject(Dosen::class.java))
////                                                val map = searchArrayListFullTemp.associateBy { it.nama }
////                                                Log.d("arraySearchListtMap",
////                                                    map.toString()
////                                                )
////                                                /***
////                                                Ilham Gusti=Dosen(nama=Ilham Gusti, lokasi=null, date=null, time=null, alat_id=null, datetime=null, tipe_akun=mahasiswa
////                                                 ***/
////                                                val map2 = searchArrayListFullTemp.groupBy { it.nama }
////                                                Log.d("arraySearchListtMapp",
////                                                    map2.toString()
////                                                )
////                                                /***
////                                                Ilham Gusti=[Dosen(nama=Ilham Gusti, lokasi=null, date=null, time=null, alat_id=null, datetime=null, tipe_akun=mahasiswa)]
////                                                 ***/
//                                                Log.d("arraySearchListt",
//                                                    searchArrayListFullTemp.toString()
//                                                )
//                                                Log.d("arraySearchListt",
//                                                    searchArrayListFullTemp[0].tipe_akun.toString()
//                                                )
////                                                Log.d("arraySearchListt",
////                                                    searchArrayListFullTemp
////                                                )
//
////                                                val set: MutableSet<Dosen> = LinkedHashSet(searchArrayList)
////                                                set.addAll(searchArrayListFullTemp)
////                                                val list3 = ArrayList(set)
////                                                Log.d("arraySearchListtt", list3.toString())
////                                                /***
////                                                Dosen(nama=Raden, lokasi=null, date=null, time=null, alat_id=08:f8:bc:60:02:2f, datetime=Timestamp(seconds=1675754316, nanoseconds=492000000), tipe_akun=null),
////                                                Dosen(nama=Ilham Gusti, lokasi=null, date=null, time=null, alat_id=null, datetime=null, tipe_akun=mahasiswa)
////                                                 ***/
//                                            }
//                                        }
//
//                                    }
//
//                                })

                        }
                        searchAdapter.notifyDataSetChanged()
                    }
                }
            })


//        if (searchState){
//            Log.d("searchStateTrue", searchState.toString())
//            Log.d("searchStateQueryRemove", searchState.toString())
//            query.remove()
//
//        } else{
//            Log.d("searchStateElse", searchState.toString())
//        }




    }


    /// need fix
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
                    searchAdapter.setData(searchArrayList)
                    eventChangeListener()
                    Log.d("searchStateToFalse", searchArrayList.toString())
                } else{
                    searchState = true
                    searchAdapter.filter.filter(newText)
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