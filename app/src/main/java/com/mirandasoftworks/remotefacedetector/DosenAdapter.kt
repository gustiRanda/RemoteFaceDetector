package com.mirandasoftworks.remotefacedetector

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.snackbar.Snackbar
import com.mirandasoftworks.remotefacedetector.databinding.UserListBinding
import com.mirandasoftworks.remotefacedetector.model.Dosen

class DosenAdapter(options: FirestoreRecyclerOptions<Dosen>) : FirestoreRecyclerAdapter<Dosen, DosenAdapter.ListViewHolder>(
    options
) {

    private val listDosen = ArrayList<Dosen>()

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

                tvUsername.text = dosen.nama
                tvLocation.text = dosen.lokasi
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