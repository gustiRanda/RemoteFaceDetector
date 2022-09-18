package com.mirandasoftworks.remotefacedetector

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mirandasoftworks.remotefacedetector.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

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
            userAdapter.setListUser(it)
            showLoading(false)
        }

//        userViewModel.getSearchUser().observe(this){
//            userAdapter.setListUser(it)
//            showLoading(false)
//        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.rvProgressBar.visibility = View.VISIBLE
        } else {
            binding.rvProgressBar.visibility = View.GONE
        }
    }

    private fun searchUser(query: String){
        userViewModel.setSearchUser(query)
//        showLoading(true)
//        userViewModel.getSearchUser()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        val search = menu.findItem(R.id.menu_search)
        val searchView = search?.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)

        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if (query != null){
            searchUser(query)
        }

        return true
    }
}