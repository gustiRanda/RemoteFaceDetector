package com.mirandasoftworks.remotefacedetector

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.mirandasoftworks.remotefacedetector.databinding.CameraModuleListBinding
import com.mirandasoftworks.remotefacedetector.databinding.PersonListBinding
import com.mirandasoftworks.remotefacedetector.model.CameraModule

class CameraModuleAdapter() : RecyclerView.Adapter<CameraModuleAdapter.ListViewHolder>(){

    private var listData = ArrayList<CameraModule>()

    fun setData(list: ArrayList<CameraModule>){
        listData.clear()
        listData.addAll(list)
        notifyDataSetChanged()
    }

    inner class ListViewHolder(itemView: CameraModuleListBinding) : RecyclerView.ViewHolder(itemView.root){
        private val binding = itemView

        fun bind(cameraModule: CameraModule){
            with(binding){
                tvLocation.text = cameraModule.lokasi
                tvIpAddress.text = cameraModule.id

                btnEdit.setOnClickListener {
                    val intent = Intent(binding.root.context, AddCameraModuleActivity::class.java)
                    intent.putExtra(AddCameraModuleActivity.LOCATION, cameraModule.lokasi)
                    intent.putExtra(AddCameraModuleActivity.ID, cameraModule.id)
                    intent.putExtra(AddCameraModuleActivity.BUTTON_NAME, "Simpan Perubahan")
                    intent.putExtra(AddCameraModuleActivity.ACTION_BAR_NAME, "Edit Modul Kamera")
                    binding.root.context.startActivity(intent)
                }

                btnDelete.setOnClickListener {

                    val db = FirebaseFirestore.getInstance()

                    db.collection("alat").document(cameraModule.id.toString())
                        .delete()
                        .addOnSuccessListener {
                            Log.d("addCameraModule", "DocumentSnapshot successfully written!")
                            Toast.makeText(binding.root.context, "Berhasil", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            Log.d("addCameraModule", "error : $e")
                            Toast.makeText(binding.root.context, "Gagal", Toast.LENGTH_SHORT).show()
                        }
                }
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(CameraModuleListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listData[position])


    }

    override fun getItemCount(): Int = listData.size

}
