package com.mirandasoftworks.remotefacedetector

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mirandasoftworks.remotefacedetector.databinding.ActivityLoginBinding
import java.security.MessageDigest
import java.util.*

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
//        val db = Firebase.firestore
//        val collection = db.collection("akun")
//        val query = collection
//            .whereEqualTo("nim_nip", username)
//
//        query.addSnapshotListener{ snapshot, e ->
//            try {
//                if (e != null){
//                    Log.w("firebaseFirestoreProfile", "Listen failed.", e)
//                    return@addSnapshotListener
//                }
//                if (snapshot != null){
//                    Log.d("loginUsername", "Username $username ada")
//                }
//                else{
//                    Log.d("loginUsername", "Username $username tidak ditemukan")
//                }
//            } catch (e: Exception) {
//                Log.d("firebaseFirestoreProfile", "system error $e")
//                Toast.makeText(this, "system error", Toast.LENGTH_SHORT).show()
//            }
//        }


        val initialPassword = password.toByteArray()
        Log.d("password", "password = $password")
        val messageDigest = MessageDigest.getInstance("SHA-256")
        val bytes = messageDigest.digest(initialPassword)
        val finalPassword = Base64.getEncoder().encodeToString(bytes)
        Log.d("password", "password = $finalPassword")

        val db = Firebase.firestore
        val query = db.collection("akun").document(username)
        query.get()

            //salah dikit
            .addOnSuccessListener { snapshot ->
                if (username == snapshot.get("nim_nip")){
                    Log.d("loginUsername", "Username ${snapshot.get("nama")} ada")
                    Log.d("loginUsername", "Username ${snapshot.get("nim_nip")} ada")
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
                    Log.d("loginUsername", "Username ${snapshot["nim_nip"]} tidak Terdaftar")
                    Log.d("loginUsername", "Username $username tidak Terdaftar")
                    Toast.makeText(this, "NIM/NIP Anda Tidak Terdaftar",Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Log.d("loginUsername", "system error $exception")
            }
    }
}