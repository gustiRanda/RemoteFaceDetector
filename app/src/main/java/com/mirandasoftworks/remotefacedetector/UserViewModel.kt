package com.mirandasoftworks.remotefacedetector

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mirandasoftworks.remotefacedetector.api.Retrofit
import com.mirandasoftworks.remotefacedetector.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel: ViewModel() {

    private val listUser = MutableLiveData<ArrayList<User>>()

    fun setListUser(){
        Retrofit.apiInstance
            .getListUser()
            .enqueue(object : Callback<ArrayList<User>>{
                override fun onResponse(
                    call: Call<ArrayList<User>>,
                    response: Response<ArrayList<User>>
                ) {
                    if (response.isSuccessful){
                        listUser.postValue(response.body())
                        Log.d("Get List Success", response.code().toString())
                    }
                }

                override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                    t.message?.let { Log.d("Get List Failed", it) }
                }

            })
    }

    fun getListUser() : LiveData<ArrayList<User>>{
        return listUser
    }


    fun setSearchUser(query: String){
        Retrofit.apiInstance
            .getSearchUser(query)
            .enqueue(object : Callback<SearchResponse>{
                override fun onResponse(
                    call: Call<SearchResponse>,
                    response: Response<SearchResponse>
                ) {
                    if (response.isSuccessful){
                        listUser.postValue(response.body()?.items)
                        Log.d("Get Search Success", response.code().toString())
                    }
                }

                override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                    t.message?.let { Log.d("Get Search Failure", it) }
                }

            })
    }

    fun getSearchUser() : LiveData<ArrayList<User>>{
        return listUser
    }
}