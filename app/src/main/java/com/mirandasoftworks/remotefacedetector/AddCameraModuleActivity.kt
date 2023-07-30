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
                val macAddress = textInputEditTextMacAddress.text.toString()

                if (roomName.isEmpty()){
                    textInputEditTextRoomName.error = "Silakan Isi Nama Ruangan"
                    textInputEditTextRoomName.requestFocus()
                } else if (macAddress.isEmpty()){
                    textInputEditTextMacAddress.error = "Silakan Isi Alamat MAC"
                    textInputEditTextMacAddress.requestFocus()
                } else {
                    addCameraModule(roomName, macAddress)
                }
            }
        }
    }

    private fun addCameraModule(roomName: String, macAddress: String) {
        val db = FirebaseFirestore.getInstance()

        val cameraModule = hashMapOf(
            "id" to macAddress,
            "lokasi" to roomName
        )

        db.collection("alat").document(macAddress)
            .set(cameraModule)
            .addOnSuccessListener {
                Log.d("addCameraModule", "DocumentSnapshot successfully written!")
                Log.d("addCameraModule", "IP = $macAddress")
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