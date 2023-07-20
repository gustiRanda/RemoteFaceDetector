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
        const val CRUD_COMMAND = "extra_crud_command"
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
        val crudCommand = intent.getStringExtra(CRUD_COMMAND)

        supportActionBar?.title = actionBarName

        with(binding){

            textInputEditTextRoomName.setText(location)
            textInputEditTextIpAddress.setText(id)
            btnAddCameraModule.text = save

            btnAddCameraModule.setOnClickListener {
                val roomName = textInputEditTextRoomName.text.toString().split(' ').joinToString(separator = " ") { word -> word.replaceFirstChar { it.uppercase() } }
                val ipAddress = textInputEditTextIpAddress.text.toString()

                if (roomName.isEmpty()){
                    textInputEditTextRoomName.error = "Silakan Isi Nama Ruangan"
                    textInputEditTextRoomName.requestFocus()
                } else if (ipAddress.isEmpty()){
                    textInputEditTextIpAddress.error = "Silakan Isi Alamat IP"
                    textInputEditTextIpAddress.requestFocus()
                } else if (crudCommand == "add"){
                    addCameraModule(roomName, ipAddress)
                } else {
                    editCameraModule(roomName, ipAddress, id!!)
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

    private fun editCameraModule(roomName: String, ipAddress: String, id: String) {
        val db = FirebaseFirestore.getInstance()

        val cameraModule = hashMapOf(
            "id" to ipAddress,
            "lokasi" to roomName
        )
//
//        const firestore = firebase.firestore();
//// get the data from 'name@xxx.com'
//        firestore.collection("users").doc("name@xxx.com").get().then(function (doc) {
//            if (doc && doc.exists) {
//                var data = doc.data();
//                // saves the data to 'name'
//                firestore.collection("users").doc("name").set(data).then({
//                    // deletes the old document
//                    firestore.collection("users").doc("name@xxx.com").delete();
//                });
//            }
//        });

//        val firestore = FirebaseFirestore.getInstance()
//// get the data from 'name@xxx.com'
//        firestore.collection("users").document("name@xxx.com").get().addOnSuccessListener { doc ->
//            if (doc != null && doc.exists()) {
//                val data = doc.data
//                // saves the data to 'name'
//                firestore.collection("users").document("name").set(data).addOnSuccessListener {
//                    // deletes the old document
//                    firestore.collection("users").document("name@xxx.com").delete()
//                }
//            }
//        }

        if (id != ipAddress){
            db.collection("alat").document(id)
                .get().addOnSuccessListener { doc ->
                    if (doc != null && doc.exists()) {
//                        val data = doc.data
//                        Log.d("addCameraModule", "data : $data")
//                        Log.d("addCameraModule", "data : ${data!!["id"]}")
//                        Log.d("addCameraModule", "data : ${data["lokasi"]}")
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

//            .set(cameraModule)
//            .addOnSuccessListener {
//                Log.d("addCameraModule", "DocumentSnapshot successfully written!")
//                Log.d("addCameraModule", "IP = $ipAddress")
//                Log.d("addCameraModule", "Ruangan = $roomName")
//                Toast.makeText(this, "Berhasil", Toast.LENGTH_SHORT).show()
//                finish()
//            }
//            .addOnFailureListener { e ->
//                Log.d("addCameraModule", "error : $e")
//                Toast.makeText(this, "Gagal", Toast.LENGTH_SHORT).show()
//            }
    }
}