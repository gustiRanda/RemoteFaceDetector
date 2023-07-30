package com.mirandasoftworks.remotefacedetector

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.mirandasoftworks.remotefacedetector.databinding.ActivityLoginBinding
import com.mirandasoftworks.remotefacedetector.model.Person
import java.security.MessageDigest
import java.util.*
import kotlin.collections.ArrayList

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        with(binding){
            btnLogin.setOnClickListener {
                val username = textInputEditTextUsername.text.toString()
                val password = textInputEditTextPassword.text.toString()

                if (username.isEmpty()){
                    textInputEditTextUsername.error = "Silakan Isi NIP/NIM Anda"
                    textInputEditTextUsername.requestFocus()
                } else if (password.isEmpty()){
                    textInputEditTextPassword.error = "Silakan Isi Password Anda"
                    textInputEditTextPassword.requestFocus()
                } else{
                    pushLogin(username, password)
                }
            }
        }


    }

    private fun pushLogin(username: String, password: String) {

        val initialPassword = password.toByteArray()
        Log.d("password", "initialPassword = $password")
        val messageDigest = MessageDigest.getInstance("SHA-256")
        Log.d("password", "messageDigest = $messageDigest")
        val bytes = messageDigest.digest(initialPassword)
        Log.d("password", "bytes = $bytes")
        val finalPassword = Base64.getEncoder().encodeToString(bytes)
        Log.d("password", "finalPassword = $finalPassword")

        val db = Firebase.firestore
        val query = db.collection("akun").document(username)
        query.get()
            .addOnSuccessListener { snapshot ->
                if (username == snapshot.get("id")){
                    Log.d("loginUsername", "Username ${snapshot.get("nama")} ada")
                    Log.d("loginUsername", "Username ${snapshot.get("id")} ada")
                    Log.d("loginUsername", "Username ${snapshot.get("password")} ada")
                    Log.d("loginUsername", "Username ${snapshot.get("tipe_akun")} ada")

                    query.get()
                        .addOnSuccessListener { snapshot1 ->
                            if (finalPassword == snapshot1.get("password")){
                                Log.d("loginUsernamePassword", "Password ${snapshot1["password"]} Anda Benar")
                                Log.d("loginUsernamePassword", "Password $finalPassword Anda Benar")

                                getSharedPreferences("PREFERENCES", MODE_PRIVATE).edit().putBoolean("loginState", true).apply()
                                getSharedPreferences("PREFERENCES", MODE_PRIVATE).edit().putString("accountType", "${snapshot.get("tipe_akun")}").apply()
                                getSharedPreferences("PREFERENCES", MODE_PRIVATE).edit().putString("name", "${snapshot.get("nama")}").apply()
                                getSharedPreferences("PREFERENCES", MODE_PRIVATE).edit().putString("nimNIP", "${snapshot.get("id")}").apply()

                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                                overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out)
                                finishAffinity()
                            } else{
                                Log.d("loginUsernamePassword", "Password ${snapshot1["password"]} Anda Salah")
                                Log.d("loginUsernamePassword", "Password $finalPassword Anda Salah")
                                Toast.makeText(this, "Password Yang Anda Masukkan Salah",Toast.LENGTH_SHORT).show()
                            }
                        }
                } else{
                    Log.d("loginUsername", "Username $username tidak Terdaftar")
                    Toast.makeText(this, "NIM/NIP Anda Tidak Terdaftar",Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Log.d("loginUsername", "system error $exception")
            }
    }
}