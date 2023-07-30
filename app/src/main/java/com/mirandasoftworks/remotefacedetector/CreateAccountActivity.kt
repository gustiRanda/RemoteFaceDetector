package com.mirandasoftworks.remotefacedetector

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.mirandasoftworks.remotefacedetector.databinding.ActivityCreateAccountBinding
import java.security.MessageDigest
import java.util.*

class CreateAccountActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Buat Akun"

        with(binding){

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

                    addAccount(name, nimNIP, selectedJob, selectedAccountType)
                }
            }
        }
    }

    private fun addAccount(name: String, nimNIP: String, selectedJob: String, selectedAccountType: String) {

        val db = FirebaseFirestore.getInstance()

        val initialPassword = nimNIP.subSequence(nimNIP.length-5 , nimNIP.length)
        Log.d("password", "password = $initialPassword")
        val password = initialPassword.toString().toByteArray()
        Log.d("password", "password = $password")
        val messageDigest = MessageDigest.getInstance("SHA-256")
        val bytes = messageDigest.digest(password)
        val finalPassword = Base64.getEncoder().encodeToString(bytes)
        Log.d("password", "password = $finalPassword")


        val account = hashMapOf(
            "nama" to name,
            "id" to nimNIP,
            "password" to finalPassword,
            "jenis_pekerjaan" to selectedJob,
            "tipe_akun" to selectedAccountType

        )



        db.collection("akun").document(nimNIP)
            .set(account)
            .addOnSuccessListener {
                Toast.makeText(this, "Berhasil", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                Log.d("addAccount", "error : $e")
                Toast.makeText(this, "Gagal", Toast.LENGTH_SHORT).show()
            }
    }
}