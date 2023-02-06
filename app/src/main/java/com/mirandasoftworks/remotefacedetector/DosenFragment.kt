package com.mirandasoftworks.remotefacedetector

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mirandasoftworks.remotefacedetector.databinding.FragmentDosenBinding
import com.mirandasoftworks.remotefacedetector.model.Dosen
import java.sql.Timestamp
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.*
import java.util.*


class DosenFragment : Fragment() {

    private var _binding: FragmentDosenBinding? = null
    private val binding get() = _binding!!

    private lateinit var dosenAdapter: DosenAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentDosenBinding.inflate(inflater, container, false)
        val root: View = binding.root


//        dosenAdapter = DosenAdapter()

//        dosenAdapter.setOnItemClickCallback(object : DosenAdapter.OnItemClickCallback {
//            override fun onItemClicked(dosen: Dosen) {
//                val intent = Intent(activity, DetailActivity::class.java)
//                intent.putExtra(DetailActivity.EXTRA_USERNAME, dosen.nama)
//                startActivity(intent)
//            }
//
//        })

        //get date test1
//        val calendar = Calendar.getInstance()
//        val currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.time)
////        Toast.makeText(requireContext(), currentDate,Toast.LENGTH_SHORT).show()
//        Log.d("date", currentDate)



////        val calendar1 = Calendar.getInstance()
////        get date test 2
//        val simpleDateFormat = SimpleDateFormat("EEEE, dd LLLL yyyy KK:mm:ss aaa z")
//        val dateTime = simpleDateFormat.format(calendar.time).toString()
//        Log.d("date1", dateTime)
//
//
//        //get date test 2
//        val simpleDateFormat1 = SimpleDateFormat("EEEE, D MMMM yyyy")
//        var dateTime1 = simpleDateFormat1.format(calendar.time).toString()
//        Log.d("date2", dateTime1)
//
//        //get time test
//        val simpleTimeFormat = SimpleDateFormat("KK:mm:ss")
//        val time = simpleTimeFormat.format(calendar.time).toString()
//        Log.d("date3", time)
//
////        showLoading(true)
//
//
//
//        binding.add.setOnClickListener {
//            klik++
//
//                val db = FirebaseFirestore.getInstance()
//                val test = hashMapOf(
//                    "nama" to "Achmad Rifki",
//                    "lokasi" to "B100",
//                    "date" to dateTime1,
//                    "time" to time,
//                )
//
//                db.collection("presensiIlhamTest").document("test$klik")
//                    .set(test)
//                    .addOnSuccessListener {
//                        Log.d("firestore", "add data sukses test$klik")
////                        Toast.makeText(requireContext(), "add data sukses", Toast.LENGTH_SHORT).show()
//                    }
//
//                    .addOnFailureListener {
//                            e -> Log.w("firestore test add", "Error writing document", e) }
//        }
//
//




//        dosenViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[DosenViewModel::class.java]
//
//        with(binding){
//            rvDosen.layoutManager = LinearLayoutManager(activity)
//            rvDosen.setHasFixedSize(true)
//            rvDosen.adapter = dosenAdapter
//        }

//        val db = Firebase.firestore
//        db.collection("presensiIlhamTest")
//            .orderBy("time", Query.Direction.DESCENDING)
////            .whereEqualTo("nama", "Achmad Rifki")
//            .addSnapshotListener(object : EventListener<QuerySnapshot> {
//                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
//                    if (error != null){
//                        Log.e("firestore", error.message.toString())
//                        return
//                    }
//
//                    for (dc: DocumentChange in value?.documentChanges!!){
//                        if (dc.type == DocumentChange.Type.ADDED){
//                            dosenArrayList.add(dc.document.toObject(Dosen::class.java))
//                            Log.d("firestore", dc.document.toObject(Dosen::class.java).toString())
//                        }
//                        if (dc.type == DocumentChange.Type.MODIFIED){
//                            dosenArrayList.add(dc.document.toObject(Dosen::class.java))
//                            Log.d("firestore", dc.document.toObject(Dosen::class.java).toString())
//                        }
//                        if (dc.type == DocumentChange.Type.REMOVED){
//                            dosenArrayList.add(dc.document.toObject(Dosen::class.java))
//                            Log.d("firestore", dc.document.toObject(Dosen::class.java).toString())
//                        }
//
//                    }
//
//                    dosenAdapter.setData(dosenArrayList)
////                    showLoading(false)
//                    Log.d("firestore", "get new data suskes")
//        Toast.makeText(requireContext(), "get new data suskes", Toast.LENGTH_SHORT).show()
//                }
//
//            })

        @Suppress("DEPRECATION")
        setHasOptionsMenu(true)

        eventChangeListener()







//        showLoading(true)
//        dosenViewModel.setListDosen()
//
//        dosenViewModel.getListDosen().observe(viewLifecycleOwner) {
////            if (it!=null){
////                userAdapter.setListUser(it)
////                showLoading(false)
////            }
//
//            dosenAdapter.setData(it)
//            showLoading(false)
//        }


        return root
    }





//    private fun eventChangeListener() {
//        val db = FirebaseFirestore.getInstance()
//        db.collection("presensiIlhamTest")
////            .whereEqualTo("nama", "Achmad Rifki")
////            .orderBy("time", Query.Direction.DESCENDING)
//            .addSnapshotListener(object : EventListener<QuerySnapshot> {
//                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
//                    if (error != null){
//                        Log.e("firestore", error.message.toString())
//                        return
//                    }
//
//                    if (value!= null){
////                        for (dc: DocumentChange in value.documentChanges){
////                            if (dc.type == DocumentChange.Type.ADDED){
////                                dosenArrayList.add(dc.document.toObject(Dosen::class.java))
////                                Log.d("firestore", "--------------------------------------------------------")
////                                Log.d("firestore", dc.document.id)
////                                Log.d("firestore", dc.document.toObject(Dosen::class.java).toString())
////
////                            }
//                        for (dc: DocumentChange in value.documentChanges){
////                            dc.document.data
//
//                            dosenArrayList.add(dc.document.toObject(Dosen::class.java))
//                                Log.d("firestore", "--------------------------------------------------------")
//                                Log.d("firestore", dc.document.id)
//                                Log.d("firestore", dc.document.data.toString())
//                                Log.d("firestore", dc.document.toObject(Dosen::class.java).toString())
//                       }
//                    }
//
//                dosenAdapter.setData(dosenArrayList)
////                    showLoading(false)
//                Log.d("firestore", "get new data suskes")
////                    Toast.makeText(requireContext(), "get new data suskes", Toast.LENGTH_SHORT).show()
//            }
//
//        })
//    }

//    private fun eventChangeListener() {
//        val db = Firebase.firestore
//        db.collection("presensiIlhamTest")
//            .whereEqualTo("nama", "Achmad Rifki")
//            .orderBy("time", Query.Direction.DESCENDING)
//            .addSnapshotListener(object : EventListener<QuerySnapshot> {
//                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
//                    if (error != null){
//                        Log.e("firestore", error.message.toString())
//                        return
//                    }
//
//                    for (dc: DocumentChange in value?.documentChanges!!){
//                        if (dc.type == DocumentChange.Type.ADDED){
//                            dosenArrayList[dc.oldIndex] = dc.document.toObject(Dosen::class.java)
//                            dosenArrayList.add(dc.newIndex, dc.document.toObject(Dosen::class.java))
////                            dosenArrayList.add(dc.document.toObject(Dosen::class.java))
//                            Log.d("firestore", dc.document.toObject(Dosen::class.java).toString())
//                        }
//
//                    }
//
//                dosenAdapter.setData(dosenArrayList)
////                    showLoading(false)
//                Log.d("firestore", "get new data suskes")
//                    Toast.makeText(requireContext(), "get new data suskes", Toast.LENGTH_SHORT).show()
//            }
//
//        })
//    }

    private fun eventChangeListener() {

        val startOfDay = LocalDate.now().atStartOfDay(ZoneId.of("Asia/Jakarta")).toInstant().toEpochMilli()
        val startOfDayTimestamp = Timestamp(startOfDay)
        Log.d("timestamp", startOfDay.toString())
        Log.d("timestamp", startOfDayTimestamp.toString())


        val endOfDay = LocalDate.now().atStartOfDay(ZoneId.of("Asia/Jakarta")).plusDays(1).toInstant().toEpochMilli()
        val endOfDayTimestamp = Timestamp(endOfDay)
        Log.d("timestamp", endOfDay.toString())
        Log.d("timestamp", endOfDayTimestamp.toString())


        val db = FirebaseFirestore.getInstance()
        val collection = db.collection("presensi")
        val query = collection
            .whereGreaterThanOrEqualTo("datetime", startOfDayTimestamp)
            .whereLessThan("datetime", endOfDayTimestamp)
            .orderBy("datetime", Query.Direction.DESCENDING)

        val option = FirestoreRecyclerOptions.Builder<Dosen>()
            .setQuery(query, Dosen::class.java)
            .build()

        dosenAdapter = DosenAdapter(option)

        with(binding){
                tvNoData.visibility = View.GONE
                rvDosen.layoutManager = LinearLayoutManager(activity)
                rvDosen.setHasFixedSize(true)
                rvDosen.adapter = dosenAdapter
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
                if (newText.isNotEmpty()){
                    Log.d("search", newText)
                } else{
//                    searchAdapter.clearData()
                    binding.tvNoData.visibility = View.VISIBLE
                }

                return false
            }

        })

    }

//    private fun eventChangeListener() {
//        val calendar = Calendar.getInstance()
//
//        //get date test 2
//        val simpleDateFormat1 = SimpleDateFormat("EEEE, D MMMM yyyy")
////        val simpleDateFormat1 = SimpleDateFormat("MMMM D, yyyy")
//        val dateTime1 = simpleDateFormat1.format(calendar.time).toString()
//        Log.d("date2", dateTime1)
//
//
//        val db = FirebaseFirestore.getInstance()
//        val collection = db.collection("presensiIlhamTest")
//        val query = collection
//            .whereEqualTo("date", dateTime1)
//            .orderBy("time", Query.Direction.DESCENDING)
//            .addSnapshotListener { snapshot, e ->
//                if (e != null) {
//                    Log.w("firebase firestore graph", "Listen failed.", e)
//                    return@addSnapshotListener
//                }
//
//                if (snapshot != null) {
//                    Log.d("firebase firestore graph", "Current data: ${snapshot.documents}")
//                    dosenArrayList.add(snapshot.documentChanges)
//                } else {
//                    Log.d("firebase firestore graph", "Current data: null")
//                }
//            }
//
//
//
//
//
//        dosenAdapter = DosenAdapter()
//        dosenArrayList = arrayListOf()
//
//        with(binding){
//            rvDosen.layoutManager = LinearLayoutManager(activity)
//            rvDosen.setHasFixedSize(true)
//            rvDosen.adapter = dosenAdapter
//        }
//    }


    //pagination
//    private fun eventChangeListener() {
//        val db = Firebase.firestore
//        //membuat query untuk 25 list pertama, diurutkan berdasar waktu
//
//        val firstPage = db.collection("presensi")
//            .orderBy("time", Query.Direction.ASCENDING)
//            .whereEqualTo("nama", "Achmad Rifki")
//            .limit(25)
//        firstPage.get()
//            .addOnSuccessListener {
//                // get list terakhir dari 25 list pertama
//                val lastVisibleItem = it.documents[it.size()-1]
//                dosenArrayList.add(it.toObjects(Dosen::class.java))
//
//
//                //membuat query baru untuk 25 list berikutnya
//                val nextItem = db.collection("presensi")
//                    .orderBy("time", Query.Direction.ASCENDING)
//                    .whereEqualTo("nama", "Achmad Rifki")
//                    .startAfter(lastVisibleItem)
//                    .limit(25)
//
//                // Use the query for pagination
//                // ...
//
//            }
//
//    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.rvProgressBar.visibility = View.VISIBLE
        } else {
            binding.rvProgressBar.visibility = View.GONE
        }
    }

    override fun onStart() {
        super.onStart()
        dosenAdapter.startListening()
        Log.d("firebaseFirestoreListener", "onStart")
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        dosenAdapter.stopListening()
        Log.d("firebaseFirestoreListener", "onDestroyView")
    }

}