package com.mirandasoftworks.remotefacedetector

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mirandasoftworks.remotefacedetector.databinding.FragmentMahasiswaBinding


class MahasiswaFragment : Fragment() {

    private var _binding: FragmentMahasiswaBinding? = null
    private val binding get() = _binding!!

    private lateinit var mahasiswaAdapter: MahasiswaAdapter
    private lateinit var mahasiswaViewModel: MahasiswaViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMahasiswaBinding.inflate(inflater, container, false)
        val root: View = binding.root

        mahasiswaAdapter = MahasiswaAdapter()
        mahasiswaAdapter.notifyDataSetChanged()

        mahasiswaViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MahasiswaViewModel::class.java)

        binding.apply {
            rvMahasiswa.layoutManager = LinearLayoutManager(activity)
            rvMahasiswa.setHasFixedSize(true)
            rvMahasiswa.adapter = mahasiswaAdapter
        }

        showLoading(true)
        mahasiswaViewModel.setListMahasiswa()

        mahasiswaViewModel.getListMahasiswa().observe(viewLifecycleOwner) {
            mahasiswaAdapter.setData(it)
            showLoading(false)
        }

        return root
    }

    private fun showLoading(state: Boolean){
        if (state) {
            binding.rvProgressBar.visibility = View.VISIBLE
        } else {
            binding.rvProgressBar.visibility = View.INVISIBLE
        }
    }


}