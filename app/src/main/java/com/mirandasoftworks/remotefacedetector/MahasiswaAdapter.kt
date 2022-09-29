package com.mirandasoftworks.remotefacedetector

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mirandasoftworks.remotefacedetector.databinding.UserListBinding
import com.mirandasoftworks.remotefacedetector.model.Dosen
import com.mirandasoftworks.remotefacedetector.model.Mahasiswa

class MahasiswaAdapter : RecyclerView.Adapter<MahasiswaAdapter.ListViewHolder>() {

    private val listMahasiswa = ArrayList<Mahasiswa>()

    fun setData(list: ArrayList<Mahasiswa>){
        listMahasiswa.clear()
        listMahasiswa.addAll(list)
        notifyDataSetChanged()
    }



    class ListViewHolder(itemView: UserListBinding) : RecyclerView.ViewHolder(itemView.root) {
        fun bind(mahasiswa: Mahasiswa) {
            with(binding){
                Glide.with(itemView)
                    .load(mahasiswa.avatar_url)
                    .into(ivAvatar)

                tvUsername.text = mahasiswa.login
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
        holder.bind(listMahasiswa[position])
    }

    override fun getItemCount(): Int = listMahasiswa.size
}