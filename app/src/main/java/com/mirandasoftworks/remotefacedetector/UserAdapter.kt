package com.mirandasoftworks.remotefacedetector

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mirandasoftworks.remotefacedetector.databinding.UserListBinding
import com.mirandasoftworks.remotefacedetector.model.User

class UserAdapter : RecyclerView.Adapter<UserAdapter.ListViewHolder>() {

    private val listUser = ArrayList<User>()

    fun setListUser(list: ArrayList<User>){
        listUser.clear()
        listUser.addAll(list)
        notifyDataSetChanged()
    }



    class ListViewHolder(itemView: UserListBinding) : RecyclerView.ViewHolder(itemView.root) {
        fun bind(user: User) {
            with(binding){
                Glide.with(itemView)
                    .load(user.avatar_url)
                    .into(ivAvatar)

                tvUsername.text = user.login
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
        holder.bind(listUser[position])
    }

    override fun getItemCount(): Int = listUser.size
}