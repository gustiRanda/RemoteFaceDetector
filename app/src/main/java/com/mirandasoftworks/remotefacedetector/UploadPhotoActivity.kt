package com.mirandasoftworks.remotefacedetector

import android.app.Dialog
import android.app.ProgressDialog
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.mirandasoftworks.remotefacedetector.databinding.ActivityUploadPhotoBinding
import java.util.UUID

class UploadPhotoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadPhotoBinding

    private lateinit var photoListAdapter: PhotoListAdapter

    lateinit var photoList : ArrayList<Uri>

    private val pickPhoto =
        registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(100)) { uris ->
            if (uris.isNotEmpty()) {
                photoList.addAll(uris)
                photoListAdapter.setData(photoList)
                Toast.makeText(this, "${uris.size} Foto Dipilih", Toast.LENGTH_SHORT).show()
                Log.d("PhotoPicker", "Number of items selected: $photoList")
            } else {
                Toast.makeText(this, "Batal", Toast.LENGTH_SHORT).show()
                Log.d("PhotoPicker", "No media selected")
            }
        }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        photoList = arrayListOf()

        photoListAdapter = PhotoListAdapter()


        with(binding){

            rvPhotoList.layoutManager = GridLayoutManager(this@UploadPhotoActivity, 2)
            rvPhotoList.setHasFixedSize(true)
            rvPhotoList.adapter = photoListAdapter

            btnSelectPhoto.setOnClickListener {
                pickPhoto.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }

            btnUploadPhoto.setOnClickListener {
                val name = textInputEditTextName.text.toString().split(' ').joinToString(separator = " ") { word -> word.replaceFirstChar { it.uppercase() } }

                if (name.isEmpty()){
                    textInputEditTextName.error = "Silakan Isi Nama"
                    textInputEditTextName.requestFocus()
                } else if (photoList.isEmpty()){
                    Toast.makeText(this@UploadPhotoActivity, "Silakan Pilih Foto", Toast.LENGTH_SHORT).show()
                } else{
                    uploadPhoto(name, photoList)
                }
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun uploadPhoto(name: String, photoList: ArrayList<Uri>) {
        val storage = FirebaseStorage.getInstance()
        var photoUploaded = 0
        val progressDialog = ProgressDialog(this)
        progressDialog.setCancelable(false)
        progressDialog.setTitle("Uploading...")
        progressDialog.show()

        for (photoUri in photoList){
            val randomUUID = UUID.randomUUID()

            val storageReference = storage.reference.child("imagesAttendance/$name/${name + "_" + randomUUID}.jpg" )
            storageReference.putFile(photoUri)

                .addOnSuccessListener {
                    Log.d("UploadPhoto", "Success " + name + "_" + randomUUID + ".jpg")
                    photoUploaded += 1
                    Log.d("UploadPhoto", "Success $photoUploaded")
                    if (photoUploaded == photoList.size){
                        Toast.makeText(this@UploadPhotoActivity, "Foto Berhasil Di-Upload", Toast.LENGTH_SHORT).show()
                        progressDialog.dismiss()
                        finish()
                    }
                }

                .addOnFailureListener {
                    Toast.makeText(this@UploadPhotoActivity, "Foto Gagal Di-Upload ${it.message}", Toast.LENGTH_SHORT).show()
                    Log.d("UploadPhoto", "OnFailure " + "Message :" +it.message + "Cause" + it.cause)
                    progressDialog.dismiss()
                }

                .addOnProgressListener {
                    taskSnapshot -> val progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
                    Log.d("UploadPhoto", "OnProgressUploading ${name + "_" + randomUUID + ".jpg"} ${progress.toInt()}%")
                    progressDialog.setMessage("Uploading " + progress.toInt() + "%\n$photoUploaded/${photoList.size}")
                }
        }

    }
}