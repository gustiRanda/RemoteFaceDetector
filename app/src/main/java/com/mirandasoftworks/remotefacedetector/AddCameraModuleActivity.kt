package com.mirandasoftworks.remotefacedetector

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.mirandasoftworks.remotefacedetector.databinding.ActivityAddCameraModuleBinding

class AddCameraModuleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddCameraModuleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCameraModuleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Tambah Modul Kamera"

        with(binding){

            btnAddCameraModule.setOnClickListener {
                val roomName = textInputEditTextRoomName.text.toString().split(' ').joinToString(separator = " ") { word -> word.replaceFirstChar { it.uppercase() } }
                val ipAddress = textInputEditTextIpAddress.text.toString()

                if (roomName.isEmpty()){
                    textInputEditTextRoomName.error = "Silakan Isi Nama Ruangan"
                    textInputEditTextRoomName.requestFocus()
                } else if (ipAddress.isEmpty()){
                    textInputEditTextIpAddress.error = "Silakan Isi Alamat IP"
                    textInputEditTextIpAddress.requestFocus()
                } else {
                    addCameraModule(roomName, ipAddress)
                }
            }
        }
    }

    private fun addCameraModule(roomName: String, ipAddress: String) {
        val db = FirebaseFirestore.getInstance()

        val cameraModule = hashMapOf(
            "id" to ipAddress,
            "lokasi" to roomName
        )

        db.collection("alat").document(ipAddress)
            .set(cameraModule)
            .addOnSuccessListener {
                Log.d("addCameraModule", "DocumentSnapshot successfully written!")
                Log.d("addCameraModule", "IP = $ipAddress")
                Log.d("addCameraModule", "Ruangan = $roomName")
                Toast.makeText(this, "Berhasil", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                Log.d("addCameraModule", "error : $e")
                Toast.makeText(this, "Gagal", Toast.LENGTH_SHORT).show()
            }
    }
}