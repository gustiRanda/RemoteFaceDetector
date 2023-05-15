package com.mirandasoftworks.remotefacedetector.model

import com.google.firebase.Timestamp

data class Person(


    val nama: String? = null,
    val lokasi: String? = null,
    val date: String? = null,
    val time: String? = null,
    val alat_id: String? = null,
    val datetime: Timestamp? = null,
    val tipe_akun: String? = null,
    val jenis_pekerjaan: String? = null
)

