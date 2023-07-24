package com.mirandasoftworks.remotefacedetector

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.mirandasoftworks.remotefacedetector.databinding.AccountListBinding
import com.mirandasoftworks.remotefacedetector.databinding.CameraModuleListBinding
import com.mirandasoftworks.remotefacedetector.databinding.PhotoListBinding
import com.mirandasoftworks.remotefacedetector.model.Account

class PhotoListAdapter() : RecyclerView.Adapter<PhotoListAdapter.ListViewHolder>() {

    private var listData = ArrayList<Uri>()

    fun setData(list: ArrayList<Uri>) {
        listData.clear()
        listData.addAll(list)
        notifyDataSetChanged()
    }

    inner class ListViewHolder(itemView: PhotoListBinding) : RecyclerView.ViewHolder(itemView.root){
        private val binding = itemView

        fun bind(uri: Uri){
            with(binding){
                Glide.with(binding.root.context)
                    .load(uri)
                    .into(ivSelectedPhoto)
                //delete fun
                //not working
//                ivSelectedPhoto.setOnClickListener{
//                    Toast.makeText(binding.root.context, "Gambar Berhasil Dihapus $absoluteAdapterPosition", Toast.LENGTH_SHORT).show()
//                    listData.removeAt(absoluteAdapterPosition)
//                    notifyDataSetChanged()
//                    UploadPhotoActivity.
//                }
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(PhotoListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listData[position])
    }

    override fun getItemCount(): Int = listData.size
}
