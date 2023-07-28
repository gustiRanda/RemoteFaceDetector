package com.mirandasoftworks.remotefacedetector

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.mirandasoftworks.remotefacedetector.databinding.ActivityEditAccountBinding
import java.security.MessageDigest
import java.util.*

class EditAccountActivity : AppCompatActivity() {

    companion object{

        const val ID = "extra_id"
        const val NAME = "extra_name"
    }

    private lateinit var binding: ActivityEditAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getStringExtra(ID)
        val name = intent.getStringExtra(NAME)

        supportActionBar?.title = "Edit Akun"

        with(binding){

            textInputEditTextUsername.setText(name)
            textInputEditTextNipNim.setText(id)

            btnCreateAccount.setOnClickListener {
                val name = textInputEditTextUsername.text.toString().split(' ').joinToString(separator = " ") { word -> word.replaceFirstChar { it.uppercase() } }

                val nimNIP = textInputEditTextNipNim.text.toString()

                if (name.isEmpty()){
                    textInputEditTextUsername.error = "Silakan Isi Nama"
                    textInputEditTextUsername.requestFocus()
                } else if (nimNIP.isEmpty()){
                    textInputEditTextNipNim.error = "Silakan Isi NIM/NIP"
                    textInputEditTextNipNim.requestFocus()
                } else if (nimNIP.length < 5){
                    textInputEditTextNipNim.error = "Silakan Isi NIM/NIP Dengan Benar"
                    textInputEditTextNipNim.requestFocus()
                } else {
                    val selectedJob = spJobType.selectedItem.toString().lowercase()

                    val selectedAccountType = spAccountType.selectedItem.toString().lowercase()

                    editAccount(name, nimNIP, selectedJob, selectedAccountType, id!!)
                }
            }
        }
    }

    private fun editAccount(name: String, nimNIP: String, selectedJob: String, selectedAccountType: String, id: String) {

        val db = FirebaseFirestore.getInstance()

        if (id != nimNIP){
            db.collection("akun").document(id)
                .get().addOnSuccessListener { doc ->
                    if (doc != null && doc.exists()) {
                        val data = doc.data
                        Log.d("addCameraModule", "data : $data")
                        Log.d("addCameraModule", "data : ${data!!["id"]}")
                        Log.d("addCameraModule", "data : ${data["nama"]}")
                        Log.d("addCameraModule", "data : ${data["jenis_pekerjaan"]}")
                        Log.d("addCameraModule", "data : ${data["tipe_akun"]}")

                        val account = hashMapOf(
                            "nama" to name,
                            "id" to nimNIP,
                            "password" to data["password"].toString(),
                            "jenis_pekerjaan" to selectedJob,
                            "tipe_akun" to selectedAccountType
                        )

                        Log.d("addCameraModule", "data : ${data["password"]}")
                        Log.d("addCameraModule", "data : $account")
                        Log.d("addCameraModule", "data : ${account["id"]}")
                        Log.d("addCameraModule", "data : ${account["nama"]}")
                        Log.d("addCameraModule", "data : ${account["jenis_pekerjaan"]}")
                        Log.d("addCameraModule", "data : ${account["tipe_akun"]}")
                        Log.d("addCameraModule", "data : ${account["password"]}")

                        db.collection("akun").document(nimNIP)
                            .set(account)
                            .addOnSuccessListener {
                                db.collection("akun").document(id)
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
            val account = hashMapOf(
                "nama" to name,
                "id" to nimNIP,
                "jenis_pekerjaan" to selectedJob,
                "tipe_akun" to selectedAccountType
            )

            db.collection("akun").document(id)
                .update(account as Map<String, Any>)
                .addOnSuccessListener {
                    Log.d("addAccount", "DocumentSnapshot successfully written!")
                    Log.d("addAccount", "nama = $name")
                    Log.d("addAccount", "id = $nimNIP")
                    Log.d("addAccount", "radio = $selectedJob")
                    Log.d("addAccount", "radio = $selectedAccountType")
                    Toast.makeText(this, "Berhasil", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { e ->
                    Log.d("addAccount", "error : $e")
                    Toast.makeText(this, "Gagal", Toast.LENGTH_SHORT).show()
                }
        }

    }
}