package com.mirandasoftworks.remotefacedetector

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
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
    private var searchQuery: String? = null

    private lateinit var searchArrayList : ArrayList<Dosen>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        searchArrayList = arrayListOf()

        @Suppress("DEPRECATION")
        setHasOptionsMenu(true)

        eventChangeListener()

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

//            .whereGreaterThanOrEqualTo("datetime", startOfDayTimestamp)
//            .whereLessThan("datetime", endOfDayTimestamp)
//            .whereEqualTo("nama", searchQuery)
            .orderBy("datetime", Query.Direction.DESCENDING)
            .addSnapshotListener(object : EventListener<QuerySnapshot>{
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    searchArrayList.clear()
                    for (document in value!!){
                        searchArrayList.add(document.toObject(Dosen::class.java))
                        searchAdapter.setData(searchArrayList)
                        Log.d("arraySearch", document.toObject<Dosen>().toString())
                        Log.d("arraySearchList", searchArrayList.toString())
                    }
                    searchAdapter.notifyDataSetChanged()
                }

            })
        Log.d("searchEvent", searchQuery.toString())



        searchAdapter = SearchAdapter()

        with(binding){
            tvNoData.visibility = View.GONE
            rvSearch.layoutManager = LinearLayoutManager(activity)
            rvSearch.setHasFixedSize(true)
            rvSearch.adapter = searchAdapter
        }
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
                searchAdapter.filter.filter(newText)

                return false
            }

        })

    }




//    private fun searchUser(query: String){
//        searchViewModel.setListSearch(query)
//    }

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