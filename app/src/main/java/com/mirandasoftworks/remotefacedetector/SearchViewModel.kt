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

class SearchViewModel: ViewModel() {

    private val listSearch = MutableLiveData<ArrayList<Dosen>>()

    fun setListSearch(){
        Retrofit.apiInstance
            .getListDosen()
            .enqueue(object : Callback<ArrayList<Dosen>>{
                override fun onResponse(
                    call: Call<ArrayList<Dosen>>,
                    response: Response<ArrayList<Dosen>>
                ) {
                    if (response.isSuccessful){
                        listSearch.postValue(response.body())
                        Log.d("Get List Search Success", response.code().toString())
                    }
                }

                override fun onFailure(call: Call<ArrayList<Dosen>>, t: Throwable) {
                    t.message?.let { Log.d("Get List Search Failed", it) }
                }

            })
    }

    fun getListSearch() : LiveData<ArrayList<Dosen>>{
        return listSearch
    }
}