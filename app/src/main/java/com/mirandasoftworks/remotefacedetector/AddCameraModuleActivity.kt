package com.mirandasoftworks.remotefacedetector

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.mirandasoftworks.remotefacedetector.databinding.ActivityAddCameraModuleBinding

class AddCameraModuleActivity : AppCompatActivity() {

    companion object{
        const val LOCATION = "extra_location"
        const val ID = "extra_id"
        const val BUTTON_NAME = "extra_button_name"
        const val ACTION_BAR_NAME = "extra_action_bar_name"
    }

    private lateinit var binding: ActivityAddCameraModuleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCameraModuleBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val location = intent.getStringExtra(LOCATION)
        val id = intent.getStringExtra(ID)
        val save = intent.getStringExtra(BUTTON_NAME)
        val actionBarName = intent.getStringExtra(ACTION_BAR_NAME)

        supportActionBar?.title = actionBarName

        with(binding){

            textInputEditTextRoomName.setText(location)
            textInputEditTextIpAddress.setText(id)
            btnAddCameraModule.text = save

            btnAddCameraModule.setOnClickListener {
                val roomName = textInputEditTextRoomName.text.toString()
                val ipAddress = textInputEditTextIpAddress.text.toString()

                if (roomName.isEmpty()){
                    textInputEditTextRoomName.error = "Silakan Isi Nama Ruangan"
                    textInputEditTextRoomName.requestFocus()
                } else if (ipAddress.isEmpty()){
                    textInputEditTextIpAddress.error = "Silakan Isi Alamat IP"
                    textInputEditTextIpAddress.requestFocus()
                } else{
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

        db.collection("alat").document("$ipAddress")
            .set(cameraModule)
            .addOnSuccessListener {
                Log.d("addCameraModule", "DocumentSnapshot successfully written!")
                Log.d("addCameraModule", "IP = $ipAddress")
                Log.d("addCameraModule", "Ruangan = $roomName")
                Toast.makeText(this, "Berhasil", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.d("addCameraModule", "error : $e")
                Toast.makeText(this, "Gagal", Toast.LENGTH_SHORT).show()
            }
    }
}