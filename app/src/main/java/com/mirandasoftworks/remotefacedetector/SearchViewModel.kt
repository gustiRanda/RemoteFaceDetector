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

    fun setListSearch(query: String){
        Retrofit.apiInstance
            .getSearchUser(query)
            .enqueue(object : Callback<SearchResponse>{

                override fun onResponse(
                    call: Call<SearchResponse>,
                    response: Response<SearchResponse>
                ) {
                    if (response.isSuccessful){
                        listSearch.postValue(response.body()?.items)
                        Log.d("Get Search Success", response.code().toString())
                    }
                }

                override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                    t.message?.let { Log.d("Get Search Failure", it) }
                }

            })
    }

    fun getListSearch() : LiveData<ArrayList<Dosen>>{
        return listSearch
    }
}