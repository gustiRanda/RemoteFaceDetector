package com.mirandasoftworks.remotefacedetector

import android.app.Dialog
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
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
                tvJobType.text = account.jenis_pekerjaan.toString().split(' ').joinToString(separator = " ") { word -> word.replaceFirstChar { it.uppercase() } }

                btnEdit.setOnClickListener {
                    val intent = Intent(binding.root.context, EditAccountActivity::class.java)
                    intent.putExtra(EditAccountActivity.ID, account.id)
                    intent.putExtra(EditAccountActivity.NAME, account.nama)
                    binding.root.context.startActivity(intent)
                }

                btnDelete.setOnClickListener {
                    val dialog = Dialog(binding.root.context)
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    dialog.setCancelable(false)
                    dialog.setContentView(R.layout.dialog_delete_account_alert)
                    val tvAccountDialog: TextView = dialog.findViewById(R.id.tv_account_dialog)
                    val btnYes: Button = dialog.findViewById(R.id.btn_yes)
                    val btnNo: Button = dialog.findViewById(R.id.btn_no)

                    tvAccountDialog.text = "Hapus Akun ${account.nama}?"

                    btnYes.setOnClickListener {
                        val db = FirebaseFirestore.getInstance()

                        db.collection("akun").document(account.id.toString())
                            .delete()
                            .addOnSuccessListener {
                                Log.d("addCameraModule", "DocumentSnapshot successfully written!")
                                Toast.makeText(binding.root.context, "Berhasil", Toast.LENGTH_SHORT).show()
                                dialog.dismiss()
                            }
                            .addOnFailureListener { e ->
                                Log.d("addCameraModule", "error : $e")
                                Toast.makeText(binding.root.context, "Gagal", Toast.LENGTH_SHORT).show()
                            }
                    }

                    btnNo.setOnClickListener {
                        dialog.dismiss()
                    }

                    dialog.show()
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
