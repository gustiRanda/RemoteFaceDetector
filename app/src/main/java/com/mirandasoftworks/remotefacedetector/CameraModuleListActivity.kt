package com.mirandasoftworks.remotefacedetector

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.mirandasoftworks.remotefacedetector.databinding.ActivityCameraModuleListBinding
import com.mirandasoftworks.remotefacedetector.databinding.CameraModuleListBinding
import com.mirandasoftworks.remotefacedetector.model.CameraModule
import com.mirandasoftworks.remotefacedetector.model.Person

class CameraModuleListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraModuleListBinding

    private var db = Firebase.firestore

    private lateinit var cameraModuleAdapter: CameraModuleAdapter

    private lateinit var cameraModuleList : ArrayList<CameraModule>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCameraModuleListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cameraModuleList = arrayListOf()

        cameraModuleAdapter = CameraModuleAdapter()

        supportActionBar?.title = "Daftar Modul Kamera"

        getData()

        with(binding){
            rvCameraModule.layoutManager = LinearLayoutManager(this@CameraModuleListActivity)
            rvCameraModule.setHasFixedSize(true)
            rvCameraModule.adapter = cameraModuleAdapter
        }

        with(binding){
            btnAddCameraModule.setOnClickListener {
                val intent = Intent(this@CameraModuleListActivity, AddCameraModuleActivity::class.java)
                startActivity(intent)
            }
        }


    }

    private fun getData() {
        db.collection("alat")
            .addSnapshotListener(object : EventListener<QuerySnapshot>{
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    cameraModuleList.clear()
                    for (document in value!!){
                        cameraModuleList.add(document.toObject(CameraModule::class.java))
                        cameraModuleAdapter.setData(cameraModuleList)
                    }
                }

            })
    }
}