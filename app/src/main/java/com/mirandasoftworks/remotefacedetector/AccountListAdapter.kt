package com.mirandasoftworks.remotefacedetector

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.mirandasoftworks.remotefacedetector.databinding.AccountListBinding
import com.mirandasoftworks.remotefacedetector.databinding.CameraModuleListBinding
import com.mirandasoftworks.remotefacedetector.model.Account

class AccountListAdapter() : RecyclerView.Adapter<AccountListAdapter.ListViewHolder>() {

    private var listData = ArrayList<Account>()

    fun setData(list: ArrayList<Account>) {
        listData.clear()
        listData.addAll(list)
        notifyDataSetChanged()
    }

    inner class ListViewHolder(itemView: AccountListBinding) : RecyclerView.ViewHolder(itemView.root){
        private val binding = itemView

        fun bind(account: Account){
            with(binding){
                tvUsername.text = account.nama
                tvNimNip.text = account.id
                tvJobType.text = account.jenis_pekerjaan

                btnEdit.setOnClickListener {
                    val intent = Intent(binding.root.context, CreateAccountActivity::class.java)
                    intent.putExtra(CreateAccountActivity.ID, account.id)
                    intent.putExtra(CreateAccountActivity.NAME, account.nama)
                    intent.putExtra(CreateAccountActivity.BUTTON_NAME, "Simpan Perubahan")
                    intent.putExtra(CreateAccountActivity.ACTION_BAR_NAME, "Edit Akun")
                    binding.root.context.startActivity(intent)
                }

                btnDelete.setOnClickListener {
                    val db = FirebaseFirestore.getInstance()

                    db.collection("akun").document(account.id.toString())
                        .delete()
                        .addOnSuccessListener {
                            Log.d("addCameraModule", "DocumentSnapshot successfully written!")
                            Toast.makeText(binding.root.context, "Berhasil", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            Log.d("addCameraModule", "error : $e")
                            Toast.makeText(binding.root.context, "Gagal", Toast.LENGTH_SHORT).show()
                        }
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountListAdapter.ListViewHolder {
        return ListViewHolder(AccountListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: AccountListAdapter.ListViewHolder, position: Int) {
        holder.bind(listData[position])
    }

    override fun getItemCount(): Int = listData.size

}
