package com.mirandasoftworks.remotefacedetector

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mirandasoftworks.remotefacedetector.databinding.FragmentSearchBinding


class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var searchAdapter: SearchAdapter
    private lateinit var searchViewModel: SearchViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        searchAdapter = SearchAdapter()
        searchAdapter.notifyDataSetChanged()

        searchViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(SearchViewModel::class.java)

        binding.apply {
            rvSearch.layoutManager = LinearLayoutManager(activity)
            rvSearch.setHasFixedSize(true)
            rvSearch.adapter = searchAdapter
        }

        showLoading(true)
        searchViewModel.setListSearch()

        searchViewModel.getListSearch().observe(viewLifecycleOwner) {
            searchAdapter.setData(it)
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