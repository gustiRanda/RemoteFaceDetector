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
import java.sql.Timestamp
import java.text.SimpleDateFormat

class DosenAdapter(options: FirestoreRecyclerOptions<Dosen>) : FirestoreRecyclerAdapter<Dosen, DosenAdapter.ListViewHolder>(
    options
) {

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
                            val location = document.toObjects(Alat::class.java)[0].lokasi
                            tvLocation.text = location
                        } catch (e: Exception){
                            Log.d("rv", "system error $e")
                        }

                    }
                    .addOnFailureListener { exception ->
                        Log.d("rv", "get failed with ", exception)
                    }

                tvUsername.text = dosen.nama

                val simpleDateFormat = SimpleDateFormat("EEEE, dd LLLL yyyy")
                val date = simpleDateFormat.format(dosen.datetime!!.toDate())
                Log.d("rvTime", date)
                tvDate.text = date

                val simpleTimeFormat = SimpleDateFormat("KK:mm:ss aaa")
                val time = simpleTimeFormat.format(dosen.datetime.toDate())
                Log.d("rvTime", time)
                tvTime.text = time
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(UserListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int, model: Dosen) {
        holder.bind(model)
    }
}