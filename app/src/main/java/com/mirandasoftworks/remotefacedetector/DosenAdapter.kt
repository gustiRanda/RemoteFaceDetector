package com.mirandasoftworks.remotefacedetector

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mirandasoftworks.remotefacedetector.databinding.UserListBinding
import com.mirandasoftworks.remotefacedetector.model.Dosen

class DosenAdapter : RecyclerView.Adapter<DosenAdapter.ListViewHolder>() {

    private val listDosen = ArrayList<Dosen>()

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setData(list: ArrayList<Dosen>){
        listDosen.clear()
        listDosen.addAll(list)
        notifyDataSetChanged()
    }



    class ListViewHolder(itemView: UserListBinding) : RecyclerView.ViewHolder(itemView.root) {
        private val binding = itemView
//      fun bind(dosen: Dosen) {
//          itemView.rootView
//
//      }

//        fun bind(dosen: Dosen) {
//            binding.tvUsername.text = dosen.login
//            Glide.with(binding.root)
//                .load(dosen.avatar_url)
//                .into(binding.ivAvatar)
//            binding.userList.setOnClickListener {  }
//
//        }
//
//
//        private val binding = itemView

        fun bind(dosen: Dosen) {
            with(binding){
                Glide.with(itemView)
                    .load(dosen.avatar_url)
                    .into(ivAvatar)

                tvUsername.text = dosen.login
//                tvLocation.text = user.location
//                tvTime.text = user.time
                root.setOnClickListener {
                    Toast.makeText(itemView.context, dosen.login, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(UserListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listDosen[position])
    }

    override fun getItemCount(): Int = listDosen.size

    interface OnItemClickCallback {
        fun onItemClicked(dosen: Dosen)
    }
}