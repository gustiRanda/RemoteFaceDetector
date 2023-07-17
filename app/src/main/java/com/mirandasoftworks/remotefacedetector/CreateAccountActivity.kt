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

    companion object{

        const val ID = "extra_id"
        const val NAME = "extra_name"
        const val BUTTON_NAME = "extra_button_name"
        const val ACTION_BAR_NAME = "extra_action_bar_name"
    }

    private lateinit var binding: ActivityCreateAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)


//        val list0 = listOf("Mahasiswa", "Tendik", "Dosen")
//        val list1 = listOf("Mahasiswa", "Dosen", "Pejabat", "Admin")
//
//        val adapter0 = ArrayAdapter<String>(this, com.google.android.material.R.layout.support_simple_spinner_dropdown_item, list0)
//        binding.sp0.adapter = adapter0
//
//        val adapter1 = ArrayAdapter<String>(this, com.google.android.material.R.layout.support_simple_spinner_dropdown_item, list1)
//        binding.sp1.adapter = adapter1

        val id = intent.getStringExtra(ID)
        val name = intent.getStringExtra(NAME)
        val save = intent.getStringExtra(BUTTON_NAME)
        val actionBarName = intent.getStringExtra(AddCameraModuleActivity.ACTION_BAR_NAME)

        supportActionBar?.title = actionBarName


        with(binding){
//            btnCreateAccount.setOnClickListener {
//                val name = textInputEditTextUsername.text.toString().split(' ').joinToString(separator = " ") { word -> word.replaceFirstChar { it.uppercase() } }
////                val sentence = "Welcome to Kotlin!"
////                val words = sentence.split(' ');
////                println(words.joinToString(separator = "_") { word -> word.replaceFirstChar { it.lowercase() } })
//                val nimNIP = textInputEditTextNipNim.text.toString()
//
//                if (name.isEmpty()){
//                    textInputEditTextUsername.error = "Silakan Isi Nama"
//                    textInputEditTextUsername.requestFocus()
//                } else if (nimNIP.isEmpty()){
//                    textInputEditTextNipNim.error = "Silakan Isi NIM/NIP"
//                    textInputEditTextNipNim.requestFocus()
//                } else if (radioGroup.checkedRadioButtonId == -1){
//                    Toast.makeText(this@CreateAccountActivity, "Pilih Jenis Akun", Toast.LENGTH_SHORT).show()
//                }
//                else{
//                    val radio:RadioButton = findViewById(radioGroup.checkedRadioButtonId)
//
//                    addAccount(name, nimNIP, radio.text.toString().lowercase())
//                }
//            }

//            sp0.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
//                override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                    Log.d("spinner", "spinner = jenis pekerjaan ${adapterView?.getItemAtPosition(position).toString()}")
//                }
//
//                override fun onNothingSelected(p0: AdapterView<*>?) {
//
//                }
//
//            }
//
//
//            sp1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
//                override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                    Log.d("spinner", "spinner = jenis akun ${adapterView?.getItemAtPosition(position).toString()}")
//                }
//
//                override fun onNothingSelected(p0: AdapterView<*>?) {
//
//                }
//
//            }

            textInputEditTextUsername.setText(name)
            textInputEditTextNipNim.setText(id)
            btnCreateAccount.text = save



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

    private fun addAccount(name: String, nimNIP: String, jobType: String, accountType: String) {

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
            "jenis_pekerjaan" to jobType,
            "tipe_akun" to accountType

        )



        db.collection("akun").document(nimNIP)
            .set(account)
            .addOnSuccessListener {
                Log.d("addAccount", "DocumentSnapshot successfully written!")
                Log.d("addAccount", "nama = $name")
                Log.d("addAccount", "id = $nimNIP")
//                Log.d("addAccount", "radio = ${binding.radioGroup.checkedRadioButtonId}")
                Log.d("addAccount", "radio = $jobType")
                Log.d("addAccount", "radio = $accountType")
                Log.d("addAccount", "radio = $finalPassword")
                Toast.makeText(this, "Berhasil", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.d("addAccount", "error : $e")
                Toast.makeText(this, "Gagal", Toast.LENGTH_SHORT).show()
            }
    }
}