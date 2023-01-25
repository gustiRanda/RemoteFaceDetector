package com.mirandasoftworks.remotefacedetector

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mirandasoftworks.remotefacedetector.databinding.UserListBinding
import com.mirandasoftworks.remotefacedetector.model.Alat
import com.mirandasoftworks.remotefacedetector.model.Dosen

class DosenAdapter(options: FirestoreRecyclerOptions<Dosen>) : FirestoreRecyclerAdapter<Dosen, DosenAdapter.ListViewHolder>(
    options
) {

//    private val listDosen = ArrayList<Dosen>()

//    private lateinit var onItemClickCallback: OnItemClickCallback
//
//    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
//        this.onItemClickCallback = onItemClickCallback
//    }

//    fun setData(list: ArrayList<Dosen>){
//        listDosen.clear()
//        listDosen.addAll(list)
//        notifyDataSetChanged()
//    }



    inner class ListViewHolder(itemView: UserListBinding) : RecyclerView.ViewHolder(itemView.root) {
        private val binding = itemView

        fun bind(dosen: Dosen) {



            with(binding){
                val db = Firebase.firestore
                val collection = db.collection("alat")
                    .whereEqualTo("id", dosen.alat_id)
                collection.get()
                    .addOnSuccessListener { document ->
                        try {
                            if (document != null) {
                                Log.d("rv", "DocumentSnapshot data: ${document.documents}")
                                val a = document.toObjects(Alat::class.java)
                                Log.d("rv", "DocumentSnapshot data a: $a")
                                val b = a[0].lokasi
                                Log.d("rv", "DocumentSnapshot data b: $b")
                                tvLocation.text = b

                            } else {
                                Log.d("rv", "No such document")
                            }
                        } catch (e: Exception){
                            Log.d("rv", "system error $e")
                        }

                    }
                    .addOnFailureListener { exception ->
                        Log.d("rv", "get failed with ", exception)
                    }


//                val db = FirebaseFirestore.getInstance()
//                val collection = db.collection("alat")
//                    .whereEqualTo("id", dosen.alat_id)
//                collection.addSnapshotListener { snapshot, e ->
//                    if (e != null) {
//                        Log.w("rc", "Listen failed.", e)
//                        return@addSnapshotListener
//                    }
//
//                    if (snapshot != null) {
//                        Log.d("rc", "Current data: ${snapshot.documents}")
//                        for (snapshot in snapshot.documents){
//
//                        }
//                    } else {
//                        Log.d("rc", "Current data: null")
//                    }
//                }

//                for (alatId in dosen.alat_id!!){
//                    val db = FirebaseFirestore.getInstance()
//                    val collection = db.collection("alat")
//                        .whereEqualTo("id", dosen.alat_id)
//                        collection.addSnapshotListener { snapshot, e ->
//                        if (e != null) {
//                            Log.w("rc", "Listen failed.", e)
//                            return@addSnapshotListener
//                        }
//
//                        if (snapshot != null) {
//                            Log.d("rc", "Current data: ${snapshot.documents}")
//                            tvLocation.text = alatId.toString()
//                        } else {
//                            Log.d("rc", "Current data: null")
//                        }
//                    }
//                }



                tvUsername.text = dosen.nama
//                tvLocation.text = dosen.alat_id
                tvTime.text = dosen.time


//                tvLocation.text = user.location
//                tvTime.text = user.time
//                root.setOnClickListener {
//                    onItemClickCallback.onItemClicked(dosen)
//                    Toast.makeText(it.context, dosen.nama, Toast.LENGTH_SHORT).show()
//
//                        //uses snackbar recomended uses coordinator layoit or anchor view
//                    dosen.nama?.let { it1 -> Snackbar.make(it, it1, Snackbar.LENGTH_SHORT).setAction("Action", null).show() }
//
//                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(UserListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }
//
//    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
//        holder.bind(listDosen[position])
//    }
//
//    override fun getItemCount(): Int = listDosen.size

//    interface OnItemClickCallback {
//        fun onItemClicked(dosen: Dosen)
//    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int, model: Dosen) {
        holder.bind(model)
    }
}