package com.mirandasoftworks.remotefacedetector

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.mirandasoftworks.remotefacedetector.databinding.ActivityDetailBinding
import com.mirandasoftworks.remotefacedetector.databinding.FragmentDosenBinding
import com.mirandasoftworks.remotefacedetector.model.Dosen

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_USERNAME = "username"
    }

    private lateinit var binding: ActivityDetailBinding

    private lateinit var detailDosenViewModel: DetailDosenViewModel

    private lateinit var dosenAdapter: DosenAdapter
    private lateinit var dosenViewModel: DosenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra(EXTRA_USERNAME)

        detailDosenViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DetailDosenViewModel::class.java)

        detailDosenViewModel.setDetailDosen(username!!)
        showLoading(true)

        detailDosenViewModel.getDetailDosen().observe(this, Observer {

            with(binding){
                tvDetailDosenUsername.text = it.login
                tvDetailDosenName.text = it.name
                ivDetailDosen.apply {
                    Glide.with(this)
                        .load(it.avatar_url)
                        .into(ivDetailDosen)
                }
                showLoading(false)
            }

//            Glide.with(this)
//                .load(it.avatar_url)
//                .into(binding.ivDetailDosen)
        })


        //just for data fill purposes
//        dosenAdapter = DosenAdapter()
//        dosenAdapter.notifyDataSetChanged()

//        dosenAdapter.setOnItemClickCallback(object : DosenAdapter.OnItemClickCallback {
//            override fun onItemClicked(dosen: Dosen) {
//                val intent = Intent(activity, DetailActivity::class.java)
//                intent.putExtra(DetailActivity.EXTRA_USERNAME, dosen.login)
//                startActivity(intent)
//            }
//
//        })

        dosenViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[DosenViewModel::class.java]


        binding.rvDosen.layoutManager = LinearLayoutManager(this)
        binding.rvDosen.setHasFixedSize(true)
        binding.rvDosen.adapter = dosenAdapter

        showLoading(true)
        dosenViewModel.setListDosen()

        dosenViewModel.getListDosen().observe(this) {
//            if (it!=null){
//                userAdapter.setListUser(it)
//                showLoading(false)
//            }

//            dosenAdapter.setData(it)
            showLoading(false)
        }


    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.rvProgressBar.visibility = View.VISIBLE
        } else {
            binding.rvProgressBar.visibility = View.GONE
        }
    }
}