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
            textInputEditTextMacAddress.setText(id)

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
                    editCameraModule(roomName, macAddress, id!!)
                }
            }
        }
    }

    private fun editCameraModule(roomName: String, macAddress: String, id: String) {
        val db = FirebaseFirestore.getInstance()

        val cameraModule = hashMapOf(
            "id" to macAddress,
            "lokasi" to roomName
        )
        if (id != macAddress){
            db.collection("alat").document(id)
                .get().addOnSuccessListener { doc ->
                    if (doc != null && doc.exists()) {
                        db.collection("alat").document(macAddress)
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