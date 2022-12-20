package com.mirandasoftworks.remotefacedetector

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mirandasoftworks.remotefacedetector.databinding.FragmentSearchBinding
import com.mirandasoftworks.remotefacedetector.model.Dosen


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

        searchAdapter.setOnItemClickCallback(object : SearchAdapter.OnItemClickCallback{
            override fun onItemClicked(dosen: Dosen) {
                val intent = Intent(activity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_USERNAME, dosen.login)
                startActivity(intent)
            }

        })

        searchViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(SearchViewModel::class.java)

        binding.apply {
            rvSearch.layoutManager = LinearLayoutManager(activity)
            rvSearch.setHasFixedSize(true)
            rvSearch.adapter = searchAdapter
        }


//        showLoading(true)
//        searchViewModel.setListSearch()

        searchViewModel.getListSearch().observe(viewLifecycleOwner) {
            searchAdapter.setData(it)
            showLoading(false)
        }

        @Suppress("DEPRECATION")
        setHasOptionsMenu(true)

        return root
    }

    private fun showLoading(state: Boolean){
        if (state) {
            binding.rvProgressBar.visibility = View.VISIBLE
        } else {
            binding.rvProgressBar.visibility = View.GONE
        }
    }

//    @Deprecated("Deprecated in Java")
//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        @Suppress("DEPRECATION")
//        val searchManager = requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
//        val searchView = menu.findItem(R.id.menu_search).actionView as SearchView
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
//        searchView.queryHint = "Cari"
//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                if (newText != null){
//                    showLoading(true)
//                    searchUser(newText)
//                } else{
//                    showLoading(false)
//                    searchAdapter.clearData()
//                }
//
//                return false
//            }
//
//        })
//        @Suppress("DEPRECATION")
//        super.onCreateOptionsMenu(menu, inflater)
//
//    }

    /// need fix
    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        @Suppress("DEPRECATION")
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.main_menu, menu)
        val searchView = context?.let { SearchView(it) }
        menu.findItem(R.id.menu_search).apply {
            setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW or MenuItem.SHOW_AS_ACTION_IF_ROOM)
            actionView = searchView
        }

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
//                if (query.isNotEmpty()){
//                    showLoading(true)
//                    searchUser(query)
//                }
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isNotEmpty()){
                    showLoading(true)
                    searchUser(newText)
                } else{
                    showLoading(false)
                    searchAdapter.clearData()
//                    searchViewModel.getListSearch()
                }
//                if (newText.isEmpty()){
//                    searchAdapter.clearData()
//                }
//
                return false
            }

        })

//        //didnt work?
//        searchView?.setOnCloseListener(object : SearchView.OnCloseListener{
//            override fun onClose(): Boolean {
//                showLoading(false)
//                return false
//            }
//
//        })
//        searchView.setOnClickListener {view ->  }
    }




    private fun searchUser(query: String){
        searchViewModel.setListSearch(query)
//        showLoading(true)
//        userViewModel.getSearchUser()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}