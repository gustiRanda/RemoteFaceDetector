package com.mirandasoftworks.remotefacedetector

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.mirandasoftworks.remotefacedetector.databinding.ActivityEditCameraModuleBinding

class EditCameraModuleActivity : AppCompatActivity() {

    companion object{
        const val LOCATION = "extra_location"
        const val ID = "extra_id"
    }

    private lateinit var binding: ActivityEditCameraModuleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditCameraModuleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val location = intent.getStringExtra(LOCATION)
        val id = intent.getStringExtra(ID)

        supportActionBar?.title = "Edit Modul Kamera"

        with(binding){

            textInputEditTextRoomName.setText(location)
            textInputEditTextIpAddress.setText(id)

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
                    editCameraModule(roomName, ipAddress, id!!)
                }
            }
        }
    }

    private fun editCameraModule(roomName: String, ipAddress: String, id: String) {
        val db = FirebaseFirestore.getInstance()

        val cameraModule = hashMapOf(
            "id" to ipAddress,
            "lokasi" to roomName
        )
        if (id != ipAddress){
            db.collection("alat").document(id)
                .get().addOnSuccessListener { doc ->
                    if (doc != null && doc.exists()) {
                        Log.d("addCameraModule", "data : $cameraModule")
                        Log.d("addCameraModule", "data : ${cameraModule["id"]}")
                        Log.d("addCameraModule", "data : ${cameraModule["lokasi"]}")
                        db.collection("alat").document(ipAddress)
                            .set(cameraModule)
                            .addOnSuccessListener {
                                db.collection("alat").document(id)
                                    .delete()
                                    .addOnSuccessListener {
                                        Toast.makeText(this, "Berhasil", Toast.LENGTH_SHORT).show()
                                        finish()
                                    }
                                    .addOnFailureListener { e ->
                                        Log.d("addCameraModule", "error : $e")
                                        Toast.makeText(this, "Gagal", Toast.LENGTH_SHORT).show()
                                    }
                            }
                    }
                }
        } else {
            db.collection("alat").document(id)
                .update(cameraModule as Map<String, Any>)
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
}