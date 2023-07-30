package com.mirandasoftworks.remotefacedetector

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mirandasoftworks.remotefacedetector.databinding.ActivityChangePasswordBinding
import java.security.MessageDigest
import java.util.*

class ChangePasswordActivity : AppCompatActivity() {

    companion object{

        const val ID = "extra_id"
    }

    private lateinit var binding: ActivityChangePasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Ubah Password"


        with(binding){
            btnChangePassword.setOnClickListener {
                val oldPassword = textInputEditTextOldPassword.text.toString()
                val newPassword = textInputEditTextNewPassword.text.toString()
                val newPasswordRetype = textInputEditTextNewPasswordRetype.text.toString()

                if (oldPassword.isEmpty()){
                    textInputEditTextOldPassword.error = "Silakan Isi Password Lama Anda"
                    textInputEditTextOldPassword.requestFocus()
                } else if (newPassword.isEmpty()){
                    textInputEditTextNewPassword.error = "Silakan Isi Password Baru Anda"
                    textInputEditTextNewPassword.requestFocus()
                } else if (newPasswordRetype.isEmpty()){
                    textInputEditTextNewPasswordRetype.error = "Silakan Tulis Ulang Password Baru Anda"
                    textInputEditTextNewPasswordRetype.requestFocus()
                } else if (newPassword != newPasswordRetype){
                    Toast.makeText(this@ChangePasswordActivity, "Password Baru Anda Tidak Cocok",Toast.LENGTH_SHORT).show()
                }else{
                    changePassword(oldPassword, newPassword)
                }
            }
        }


    }

    private fun changePassword(oldPassword: String, newPassword: String) {


        val id = getSharedPreferences("PREFERENCES", MODE_PRIVATE).getString("nimNIP", "")

        val initialPassword = oldPassword.toByteArray()
        val messageDigest = MessageDigest.getInstance("SHA-256")
        val bytes = messageDigest.digest(initialPassword)
        val finalPassword = Base64.getEncoder().encodeToString(bytes)

        val db = Firebase.firestore
        val query = db.collection("akun").document(id.toString())
        query.get()
            .addOnSuccessListener { snapshot ->
                if (finalPassword == snapshot.get("password")){

                    val newInitialPassword = newPassword.toByteArray()
                    val newMessageDigest = MessageDigest.getInstance("SHA-256")
                    val newBytes = messageDigest.digest(newInitialPassword)
                    val newFinalPassword = Base64.getEncoder().encodeToString(newBytes)

                    query
                        .update("password", newFinalPassword)
                        .addOnSuccessListener { snapshot1 ->
                            Log.d("changePassword", "Password Anda Berhasil Diubah")
                            Toast.makeText(this, "Password Anda Berhasil Diubah",Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        .addOnFailureListener { exception ->
                            Log.d("changePassword", "system error $exception")
                            Toast.makeText(this, "Gagal, Silakan Coba Lagi",Toast.LENGTH_SHORT).show()
                        }
                } else{
                    Log.d("changePassword", "Password Lama Anda Salah")
                    Toast.makeText(this, "Password Lama Anda Salah",Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Log.d("changePassword", "system error $exception")
                Toast.makeText(this, "Gagal, Silakan Coba Lagi",Toast.LENGTH_SHORT).show()
            }
    }
}