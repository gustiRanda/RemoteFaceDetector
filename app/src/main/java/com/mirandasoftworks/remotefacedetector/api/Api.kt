package com.mirandasoftworks.remotefacedetector.api

import com.mirandasoftworks.remotefacedetector.model.DetailDosenResponse
import com.mirandasoftworks.remotefacedetector.model.Dosen
import com.mirandasoftworks.remotefacedetector.model.Mahasiswa
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface Api {
//    @GET("search/users")
//    @Headers("Authorization: token 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")
//    fun getSearchUser(
//        @Query("q") query: String
//    ): Call<SearchResponse>

    @GET("users")
    @Headers("Authorization: token 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")
    fun getListDosen(): Call<ArrayList<Dosen>>

    @GET("users")
    @Headers("Authorization: token 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")
    fun getListMahasiswa(): Call<ArrayList<Mahasiswa>>

    @GET("users/{username}")
    @Headers("Authorization: token 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")
    fun getDetailDosen(
        @Path("username") username : String
    ): Call<DetailDosenResponse>

//    @GET("users/{username}/followers")
//    @Headers("Authorization: token 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")
//    fun getFollowers(
//        @Path("username") username: String
//    ): Call<ArrayList<User>>
//
//    @GET("users/{username}/following")
//    @Headers("Authorization: token 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")
//    fun getFollowing(
//        @Path("username") username: String
//    ): Call<ArrayList<User>>

//    @GET("users/{username}/repos")
//    @Headers("Authorization: token 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")
//    fun getRepository(
//        @Path("username") username: String
//    ): Call<ArrayList<Repository>>
}