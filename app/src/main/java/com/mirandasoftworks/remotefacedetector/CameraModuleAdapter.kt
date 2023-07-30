package com.mirandasoftworks.remotefacedetector

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
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
                tvLocation.text = cameraModule.lokasi.toString().split(' ').joinToString(separator = " ") { word -> word.replaceFirstChar { it.uppercase() } }
                tvMacAddress.text = cameraModule.id

                btnEdit.setOnClickListener {
                    val intent = Intent(binding.root.context, EditCameraModuleActivity::class.java)
                    intent.putExtra(EditCameraModuleActivity.LOCATION, cameraModule.lokasi)
                    intent.putExtra(EditCameraModuleActivity.ID, cameraModule.id)
                    binding.root.context.startActivity(intent)
                }

                btnDelete.setOnClickListener {

                    val dialog = Dialog(binding.root.context)
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    dialog.setCancelable(false)
                    dialog.setContentView(R.layout.dialog_delete_camera_module_alert)
                    val tvCameraModuleAlert: TextView = dialog.findViewById(R.id.tv_camera_module_dialog)
                    val btnYes: Button = dialog.findViewById(R.id.btn_yes)
                    val btnNo: Button = dialog.findViewById(R.id.btn_no)

                    tvCameraModuleAlert.text = "Hapus Modul Kamera ${cameraModule.lokasi}?"

                    btnYes.setOnClickListener {
                        val db = FirebaseFirestore.getInstance()

                        db.collection("alat").document(cameraModule.id.toString())
                            .delete()
                            .addOnSuccessListener {
                                Log.d("addCameraModule", "DocumentSnapshot successfully written!")
                                Toast.makeText(binding.root.context, "Berhasil", Toast.LENGTH_SHORT).show()
                                dialog.dismiss()
                            }
                            .addOnFailureListener { e ->
                                Log.d("addCameraModule", "error : $e")
                                Toast.makeText(binding.root.context, "Gagal", Toast.LENGTH_SHORT).show()
                            }
                    }

                    btnNo.setOnClickListener {
                        dialog.dismiss()
                    }

                    dialog.show()

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
