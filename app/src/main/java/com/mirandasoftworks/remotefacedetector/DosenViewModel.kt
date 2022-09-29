package com.mirandasoftworks.remotefacedetector

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mirandasoftworks.remotefacedetector.api.Retrofit
import com.mirandasoftworks.remotefacedetector.model.Dosen
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DosenViewModel: ViewModel() {

    private val listDosen = MutableLiveData<ArrayList<Dosen>>()

    fun setListDosen(){
        Retrofit.apiInstance
            .getListDosen()
            .enqueue(object : Callback<ArrayList<Dosen>>{
                override fun onResponse(
                    call: Call<ArrayList<Dosen>>,
                    response: Response<ArrayList<Dosen>>
                ) {
                    if (response.isSuccessful){
                        listDosen.postValue(response.body())
                        Log.d("Get List Dosen Success", response.code().toString())
                    }
                }

                override fun onFailure(call: Call<ArrayList<Dosen>>, t: Throwable) {
                    t.message?.let { Log.d("Get List Dosen Failed", it) }
                }

            })
    }

    fun getListDosen() : LiveData<ArrayList<Dosen>>{
        return listDosen
    }
}