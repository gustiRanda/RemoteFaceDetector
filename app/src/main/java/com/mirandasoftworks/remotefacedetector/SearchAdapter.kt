package com.mirandasoftworks.remotefacedetector

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mirandasoftworks.remotefacedetector.databinding.UserListBinding
import com.mirandasoftworks.remotefacedetector.model.Dosen

class SearchAdapter : RecyclerView.Adapter<SearchAdapter.ListViewHolder>() {

    private val listSearch = ArrayList<Dosen>()

    fun setData(list: ArrayList<Dosen>){
        listSearch.clear()
        listSearch.addAll(list)
        notifyDataSetChanged()
    }



    class ListViewHolder(itemView: UserListBinding) : RecyclerView.ViewHolder(itemView.root) {
        fun bind(dosen: Dosen) {
            with(binding){
//                Glide.with(itemView)
//                    .load(mahasiswa.avatar_url)
//                    .into(ivAvatar)

                tvUsername.text = dosen.login
//                tvLocation.text = user.location
//                tvTime.text = user.time
            }
        }

        private val binding = itemView

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(UserListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listSearch[position])
    }

    override fun getItemCount(): Int = listSearch.size
}