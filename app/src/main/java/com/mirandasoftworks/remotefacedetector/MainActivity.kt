package com.mirandasoftworks.remotefacedetector

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mirandasoftworks.remotefacedetector.databinding.ActivityMainBinding
import com.mirandasoftworks.remotefacedetector.model.User

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

//    private val listUser = ArrayList<User>()
    private lateinit var userAdapter: UserAdapter
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        userAdapter = UserAdapter(listUser)
        userAdapter = UserAdapter()
        userAdapter.notifyDataSetChanged()

        userViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[UserViewModel::class.java]

        binding.rvUser.layoutManager = LinearLayoutManager(this)
        binding.rvUser.setHasFixedSize(true)
        binding.rvUser.adapter = userAdapter

        showLoading(true)
        userViewModel.setListUser()

        userViewModel.getListUser().observe(this) {
//            if (it!=null){
//                userAdapter.setListUser(it)
//                showLoading(false)
//            }

            userAdapter.setListUser(it)
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