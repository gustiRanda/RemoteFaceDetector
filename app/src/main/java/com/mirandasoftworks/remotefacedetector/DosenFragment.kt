package com.mirandasoftworks.remotefacedetector

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mirandasoftworks.remotefacedetector.databinding.FragmentDosenBinding
import com.mirandasoftworks.remotefacedetector.model.Dosen


class DosenFragment : Fragment() {

    private var _binding: FragmentDosenBinding? = null
    private val binding get() = _binding!!

    private lateinit var dosenAdapter: DosenAdapter
    private lateinit var dosenViewModel: DosenViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        val mainViewModel =
//            ViewModelProvider(this).get(UserViewModel::class.java)


        // Inflate the layout for this fragment

        _binding = FragmentDosenBinding.inflate(inflater, container, false)
        val root: View = binding.root

        dosenAdapter = DosenAdapter()
        dosenAdapter.notifyDataSetChanged()

        dosenAdapter.setOnItemClickCallback(object : DosenAdapter.OnItemClickCallback {
            override fun onItemClicked(dosen: Dosen) {
                val intent = Intent(activity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_USERNAME, dosen.login)
                startActivity(intent)
            }

        })

        dosenViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[DosenViewModel::class.java]

        binding.rvDosen.layoutManager = LinearLayoutManager(activity)
        binding.rvDosen.setHasFixedSize(true)
        binding.rvDosen.adapter = dosenAdapter

        showLoading(true)
        dosenViewModel.setListDosen()

        dosenViewModel.getListDosen().observe(viewLifecycleOwner) {
//            if (it!=null){
//                userAdapter.setListUser(it)
//                showLoading(false)
//            }

            dosenAdapter.setData(it)
            showLoading(false)
        }


        return root
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.rvProgressBar.visibility = View.VISIBLE
        } else {
            binding.rvProgressBar.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}