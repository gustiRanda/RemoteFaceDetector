package com.mirandasoftworks.remotefacedetector

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

import com.mirandasoftworks.remotefacedetector.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        countData()

        return root
    }

    private fun countData(){
        val db = FirebaseFirestore.getInstance()
        val collection = db.collection("presensiIlhamTest")
        val query = collection
            .whereEqualTo("date", "Wednesday, 18 January 2023")
        val countQuery = query.count()

        countQuery.get(AggregateSource.SERVER).addOnCompleteListener {
            if (it.isSuccessful) {
                val snapshot = it.result
                Toast.makeText(activity, "Count: ${snapshot.count}", Toast.LENGTH_SHORT).show()
                Log.d("firebase firestore count", "Count: ${snapshot.count}")
            } else {
                Log.d("firebase firestore count", "Count failed: ", it.exception)
                Toast.makeText(activity, "Count failed: ${it.exception}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}