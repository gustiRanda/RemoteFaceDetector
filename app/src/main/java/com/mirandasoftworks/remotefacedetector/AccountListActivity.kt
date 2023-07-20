package com.mirandasoftworks.remotefacedetector

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mirandasoftworks.remotefacedetector.databinding.ActivityAccountListBinding
import com.mirandasoftworks.remotefacedetector.model.Account
import com.mirandasoftworks.remotefacedetector.model.CameraModule
import com.mirandasoftworks.remotefacedetector.model.Person

class AccountListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAccountListBinding

    private var db = Firebase.firestore

    private lateinit var accountListAdapter: AccountListAdapter

    private lateinit var accountList : ArrayList<Account>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAccountListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        accountList = arrayListOf()

        accountListAdapter = AccountListAdapter()

        supportActionBar?.title = "Daftar Akun"

        getData()

        with(binding){
            rvAccountList.layoutManager = LinearLayoutManager(this@AccountListActivity)
            rvAccountList.setHasFixedSize(true)
            rvAccountList.adapter = accountListAdapter
        }

        with(binding){
            btnAddAccount.setOnClickListener {
                val intent = Intent(this@AccountListActivity, CreateAccountActivity::class.java)
                intent.putExtra(CreateAccountActivity.BUTTON_NAME, "Buat Akun")
                intent.putExtra(CreateAccountActivity.ACTION_BAR_NAME, "Buat Akun")
                intent.putExtra(CreateAccountActivity.CRUD_COMMAND, "add")
                startActivity(intent)
            }
        }
    }

    private fun getData() {
        db.collection("akun")
            .orderBy("nama", Query.Direction.ASCENDING)
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    accountList.clear()
                    for (document in value!!){
                        accountList.add(document.toObject(Account::class.java))
                        accountListAdapter.setData(accountList)
                    }
                }

            })
    }
}