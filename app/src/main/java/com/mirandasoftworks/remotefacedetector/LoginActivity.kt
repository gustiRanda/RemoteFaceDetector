package com.mirandasoftworks.remotefacedetector

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mirandasoftworks.remotefacedetector.databinding.ActivityLoginBinding
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val calendar = Calendar.getInstance()

            //get date test 2
            val simpleDateFormat1 = SimpleDateFormat("EEEE, D MMMM yyyy")
            val simpleTimeFormat = SimpleDateFormat("KK:mm:ss")
            val date = simpleDateFormat1.format(calendar.time).toString()
            val time = simpleTimeFormat.format(calendar.time).toString()


            for (i in 3..100) {
                println(i)
                val db = Firebase.firestore
                val test = hashMapOf(
                    "nama" to "ilham$i",
                    "lokasi" to "B100",
                    "date" to date,
                    "time" to time,
                )

                db.collection("presensi").document("test$i")
                    .set(test)
                    .addOnSuccessListener { Log.d("test add", "DocumentSnapshot successfully written!") }
                    .addOnFailureListener { e -> Log.w("test add", "Error writing document", e) }
            }

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out)
            finish()
        }


    }
}