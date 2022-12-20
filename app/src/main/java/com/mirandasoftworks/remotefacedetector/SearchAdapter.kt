package com.mirandasoftworks.remotefacedetector

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.mirandasoftworks.remotefacedetector.databinding.UserListBinding
import com.mirandasoftworks.remotefacedetector.model.Dosen

class SearchAdapter : RecyclerView.Adapter<SearchAdapter.ListViewHolder>() {

    private val listSearch = ArrayList<Dosen>()

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setData(list: ArrayList<Dosen>){
        listSearch.clear()
        listSearch.addAll(list)
        notifyDataSetChanged()
    }

    fun clearData(){
        listSearch.clear()
        notifyDataSetChanged()
    }



    inner class ListViewHolder(itemView: UserListBinding) : RecyclerView.ViewHolder(itemView.root) {
        private val binding = itemView

        fun bind(dosen: Dosen) {
            with(binding){

                tvUsername.text = dosen.login
                tvLocation.text = dosen.location
                tvTime.text = dosen.id.toString()
//                tvLocation.text = user.location
//                tvTime.text = user.time
                root.setOnClickListener {
                    onItemClickCallback.onItemClicked(dosen)
                    Toast.makeText(it.context, dosen.login, Toast.LENGTH_SHORT).show()

                    //uses snackbar recomended uses coordinator layoit or anchor view
                    Snackbar.make(it, dosen.login, Snackbar.LENGTH_SHORT).setAction("Action", null).show()

                }
            }
        }

//        fun bind(dosen: Dosen) {
//            with(binding){
////                Glide.with(itemView)
////                    .load(mahasiswa.avatar_url)
////                    .into(ivAvatar)
//
//                tvUsername.text = dosen.login
//                tvLocation.text = dosen.location
//                tvTime.text = dosen.time
//            }
//        }



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(UserListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listSearch[position])
    }

    override fun getItemCount(): Int = listSearch.size

    interface OnItemClickCallback {
        fun onItemClicked(dosen: Dosen)
    }
}