package com.mirandasoftworks.remotefacedetector

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
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

        with(binding){
            btnCreateAccount.setOnClickListener {
                val name = textInputEditTextUsername.text.toString().split(' ').joinToString(separator = " ") { word -> word.replaceFirstChar { it.uppercase() } }
//                val sentence = "Welcome to Kotlin!"
//                val words = sentence.split(' ');
//                println(words.joinToString(separator = "_") { word -> word.replaceFirstChar { it.lowercase() } })
                val nimNIP = textInputEditTextNipNim.text.toString()

                if (name.isEmpty()){
                    textInputEditTextUsername.error = "Silakan Isi Nama"
                    textInputEditTextUsername.requestFocus()
                } else if (nimNIP.isEmpty()){
                    textInputEditTextNipNim.error = "Silakan Isi NIM/NIP"
                    textInputEditTextNipNim.requestFocus()
                } else if (radioGroup.checkedRadioButtonId == -1){
                    Toast.makeText(this@CreateAccountActivity, "Pilih Jenis Akun", Toast.LENGTH_SHORT).show()
                }
                else{
                    val radio:RadioButton = findViewById(radioGroup.checkedRadioButtonId)

                    addAccount(name, nimNIP, radio.text.toString().lowercase())
                }
            }
        }

//        // Get radio group selected item using on checked change listener
//        binding.radioGroup.setOnCheckedChangeListener(
//            RadioGroup.OnCheckedChangeListener { group, checkedId ->
//                val radio: RadioButton = findViewById(checkedId)
//                Toast.makeText(this," On checked change :"+
//                        " ${radio.text}",
//                    Toast.LENGTH_SHORT).show()
//            })
//        // Get radio group selected status and text using button click event
//        binding.btnCreateAccount.setOnClickListener{
//            // Get the checked radio button id from radio group
//            var id: Int = binding.radioGroup.checkedRadioButtonId
//            if (id!=-1){ // If any radio button checked from radio group
//                // Get the instance of radio button using id
//                val radio:RadioButton = findViewById(id)
//                Toast.makeText(this,"On button click :" +
//                        " ${radio.text}",
//                    Toast.LENGTH_SHORT).show()
//            }else{
//                // If no radio button checked in this radio group
//                Toast.makeText(this,"On button click :" +
//                        " nothing selected",
//                    Toast.LENGTH_SHORT).show()
//            }
//        }

//        setOnCheckedChangeListener()
    }

//    fun radio_button_click(view: View){
//        // Get the clicked radio button instance
//        val radio: RadioButton = findViewById(binding.radioGroup.checkedRadioButtonId)
//        Toast.makeText(applicationContext,"On click : ${radio.text}",
//            Toast.LENGTH_SHORT).show()
//    }

    private fun addAccount(name: String, nimNIP: String, text: String) {
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
            "nim_nip" to nimNIP,
            "password" to finalPassword,
            "tipe_akun" to text

        )

        db.collection("akun").document(nimNIP)
            .set(account)
            .addOnSuccessListener {
                Log.d("addAccount", "DocumentSnapshot successfully written!")
                Log.d("addAccount", "nama = $name")
                Log.d("addAccount", "nim_nip = $nimNIP")
                Log.d("addAccount", "radio = ${binding.radioGroup.checkedRadioButtonId}")
                Log.d("addAccount", "radio = $text")
                Log.d("addAccount", "radio = $finalPassword")
                Toast.makeText(this, "Akun Berhasil Dibuat", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.d("addAccount", "error : $e")
                Toast.makeText(this, "Akun Gagal Dibuat, Silakan Coba Lagi", Toast.LENGTH_SHORT).show()
            }
    }
}