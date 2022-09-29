package com.mirandasoftworks.remotefacedetector

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.mirandasoftworks.remotefacedetector.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_USERNAME = "username"
    }

    private lateinit var binding: ActivityDetailBinding

    private lateinit var detailDosenViewModel: DetailDosenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra(EXTRA_USERNAME)

        detailDosenViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DetailDosenViewModel::class.java)

        detailDosenViewModel.setDetailDosen(username!!)
        detailDosenViewModel.getDetailDosen().observe(this, Observer {
            binding.apply {
                tvDetailDosenUsername.text = it.login
                tvDetailDosenName.text = it.name
                ivDetailDosen.apply {
                    Glide.with(this)
                        .load(it.avatar_url)
                        .into(ivDetailDosen)
                }
            }

//            Glide.with(this)
//                .load(it.avatar_url)
//                .into(binding.ivDetailDosen)
        })

    }
}