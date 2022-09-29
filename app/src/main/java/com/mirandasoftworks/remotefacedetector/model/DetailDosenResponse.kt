package com.mirandasoftworks.remotefacedetector.model

data class DetailDosenResponse(
    val login : String,
    val avatar_url : String,
    val followers_url : String,
    val following_url : String,
    val repos_url : String,
    val name : String,
    val company : String,
    val location : String,
    val public_repos : Int,
    val followers : Int,
    val following : Int
)