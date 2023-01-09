package com.mirandasoftworks.remotefacedetector

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.mirandasoftworks.remotefacedetector.databinding.FragmentDosenBinding
import com.mirandasoftworks.remotefacedetector.model.Dosen
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar


class DosenFragment : Fragment() {

    private var _binding: FragmentDosenBinding? = null
    private val binding get() = _binding!!

    private lateinit var dosenArrayList: ArrayList<Dosen>


    private lateinit var dosenAdapter: DosenAdapter
    private lateinit var dosenViewModel: DosenViewModel

    var klik = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentDosenBinding.inflate(inflater, container, false)
        val root: View = binding.root

        dosenAdapter = DosenAdapter()
//        dosenAdapter.notifyDataSetChanged()

        dosenAdapter.setOnItemClickCallback(object : DosenAdapter.OnItemClickCallback {
            override fun onItemClicked(dosen: Dosen) {
                val intent = Intent(activity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_USERNAME, dosen.nama)
                startActivity(intent)
            }

        })

        //get date test1
        val calendar = Calendar.getInstance()
        val currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.time)
        Toast.makeText(requireContext(), currentDate,Toast.LENGTH_SHORT).show()
        Log.d("date", currentDate)



//        val calendar1 = Calendar.getInstance()
//        get date test 2
        val simpleDateFormat = SimpleDateFormat("EEEE, dd LLLL yyyy KK:mm:ss aaa z")
        val dateTime = simpleDateFormat.format(calendar.time).toString()
        Log.d("date1", dateTime)


        //get time test
        val simpleTimeFormat = SimpleDateFormat("KK:mm:ss")
        val time = simpleTimeFormat.format(calendar.time).toString()
        Log.d("date1", time)

        //get date test 2
        val simpleDateFormat1 = SimpleDateFormat("EEEE, D MMMM yyyy")
        var dateTime1 = simpleDateFormat1.format(calendar.time).toString()
        Log.d("date2", dateTime1)

//        showLoading(true)

        dosenArrayList = arrayListOf()

        binding.add.setOnClickListener {
            klik++

                val db = Firebase.firestore
                val test = hashMapOf(
                    "nama" to "Achmad Rifki",
                    "lokasi" to "B100",
                    "date" to dateTime1,
                    "time" to time,
                )

                db.collection("presensi").document("test$klik")
                    .set(test)
                    .addOnSuccessListener {
                        Log.d("firestore test add", "DocumentSnapshot test$klik successfully written!")
                        Toast.makeText(requireContext(), "add data sukses", Toast.LENGTH_SHORT).show()}

                    .addOnFailureListener {
                            e -> Log.w("firestore test add", "Error writing document", e) }
        }






//        dosenViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[DosenViewModel::class.java]

        with(binding){
            rvDosen.layoutManager = LinearLayoutManager(activity)
            rvDosen.setHasFixedSize(true)
            rvDosen.adapter = dosenAdapter
        }

        val db = Firebase.firestore
        db.collection("presensi")
            .orderBy("time", Query.Direction.DESCENDING)
            .whereEqualTo("date", dateTime1)
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null){
                        Log.e("firestore", error.message.toString())
                        return
                    }

                    for (dc: DocumentChange in value?.documentChanges!!){
                        if (dc.type == DocumentChange.Type.ADDED){
                            dosenArrayList.add(dc.document.toObject(Dosen::class.java))
                            Log.d("firestore", dc.document.toObject(Dosen::class.java).toString())
                        }

                    }

                    dosenAdapter.setData(dosenArrayList)
//                    showLoading(false)
                    Log.d("firestore", "get new data suskes")
                }

            })
//        eventChangeListener()







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
//        val db = Firebase.firestore
//        db.collection("presensi")
//            .orderBy("time", Query.Direction.ASCENDING)
//            .whereEqualTo("date", "$date")
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
//
//                    }
//
//                dosenAdapter.setData(dosenArrayList)
//                    showLoading(false)
//                Log.d("firestore", "suskses")
//            }
//
//        })
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}