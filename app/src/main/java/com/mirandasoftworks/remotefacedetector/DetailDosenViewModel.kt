package com.mirandasoftworks.remotefacedetector

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mirandasoftworks.remotefacedetector.api.Retrofit
import com.mirandasoftworks.remotefacedetector.model.DetailDosenResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailDosenViewModel : ViewModel() {

    val detailDosen = MutableLiveData<DetailDosenResponse>()

    fun setDetailDosen(username: String){
        Retrofit.apiInstance
            .getDetailDosen(username)
            .enqueue(object : Callback<DetailDosenResponse> {
                override fun onResponse(
                    call: Call<DetailDosenResponse>,
                    response: Response<DetailDosenResponse>
                ) {
                    if (response.isSuccessful){
                        detailDosen.postValue(response.body())
                        Log.d("Get Detail Dosen Success", response.code().toString())
                    }
                }

                override fun onFailure(call: Call<DetailDosenResponse>, t: Throwable) {
                    t.message?.let { Log.d("Get Detail Dosen Failed", it) }
                }

            })
    }

    fun getDetailDosen(): LiveData<DetailDosenResponse> {
        return detailDosen
    }
}