package com.mirandasoftworks.remotefacedetector

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mirandasoftworks.remotefacedetector.api.Retrofit
import com.mirandasoftworks.remotefacedetector.model.Dosen
import com.mirandasoftworks.remotefacedetector.model.Mahasiswa
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MahasiswaViewModel: ViewModel() {

    private val listMahasiswa = MutableLiveData<ArrayList<Mahasiswa>>()

    fun setListMahasiswa(){
        Retrofit.apiInstance
            .getListMahasiswa()
            .enqueue(object : Callback<ArrayList<Mahasiswa>>{
                override fun onResponse(
                    call: Call<ArrayList<Mahasiswa>>,
                    response: Response<ArrayList<Mahasiswa>>
                ) {
                    if (response.isSuccessful){
                        listMahasiswa.postValue(response.body())
                        Log.d("Get List Mahasiswa Success", response.code().toString())
                    }
                }

                override fun onFailure(call: Call<ArrayList<Mahasiswa>>, t: Throwable) {
                    t.message?.let { Log.d("Get List Mahasiswa Failed", it) }
                }

            })
    }

    fun getListMahasiswa() : LiveData<ArrayList<Mahasiswa>>{
        return listMahasiswa
    }
}