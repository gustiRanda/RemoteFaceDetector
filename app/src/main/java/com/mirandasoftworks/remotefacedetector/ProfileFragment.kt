package com.mirandasoftworks.remotefacedetector

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.FirebaseFirestore

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

        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("presensi").document("test")
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("firestore", "DocumentSnapshot data: ${document.data}")
                } else {
                    Log.d("forestore", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("firestore", "get failed with ", exception)
            }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}