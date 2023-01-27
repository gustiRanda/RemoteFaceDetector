package com.mirandasoftworks.remotefacedetector

import android.app.DatePickerDialog
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
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Year
import java.time.ZoneId
import java.util.*


class ProfileFragment : Fragment() {

    private lateinit var startDateTimestamp: Timestamp
    private lateinit var endDateTimestamp: Timestamp


    private var startDate: Long? = null
    private var endDate: Long? = null


    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root


//        //both work but cannot pass date value tp firebase
        //cant get hour minutes etc

//        val calendar = Calendar.getInstance()
//        val year = calendar.get(Calendar.YEAR)
//        val month = calendar.get(Calendar.MONTH)
//        val day = calendar.get(Calendar.DAY_OF_MONTH)
//
//        binding.button.setOnClickListener {
//            val datePickerDialog = DatePickerDialog(requireContext(), { _, year, month, day ->
//                calendar.set(year, month, day)
//                val simpleDateFormat1 = SimpleDateFormat("MMMM D, yyyy")
//                val dateTime1 = simpleDateFormat1.format(calendar.time)
//                Log.d("date2", dateTime1)
//                binding.textview.text = dateTime1
//            }, year, month, day)
//            datePickerDialog.show()
//        }
//
//        binding.button1.setOnClickListener {
//            val datePickerDialog = DatePickerDialog(requireContext(), { _, year, month, day ->
//                calendar.set(year, month, day)
//                val simpleDateFormat1 = SimpleDateFormat("MMMM D, yyyy")
//                val dateTime1 = simpleDateFormat1.format(calendar.time)
//                Log.d("date2", dateTime1)
//                binding.textview1.text = dateTime1
//            }, year, month, day)
//            datePickerDialog.show()
//        }


        //almost work but bugggggggggggggg

        val calendar = Calendar.getInstance()

        val datePickerDialog = DatePickerDialog.OnDateSetListener { _, year, month, day  ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, day)
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            updateLabel(calendar)
//            updateQuery()

        }

        val datePickerDialog1 = DatePickerDialog.OnDateSetListener { _, year, month, day  ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, day)
            calendar.set(Calendar.HOUR_OF_DAY, 23)
            calendar.set(Calendar.MINUTE, 59)
            calendar.set(Calendar.SECOND, 59)
            calendar.set(Calendar.MILLISECOND, 999)
            updateLabel1(calendar)
//            updateQuery()

        }

        binding.button.setOnClickListener {
            DatePickerDialog(requireContext(), datePickerDialog, calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        binding.button1.setOnClickListener {
            DatePickerDialog(requireContext(), datePickerDialog1, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        binding.button2.setOnClickListener {
            try {
                updateQuery()
            } catch (e: Exception){
                Log.d("firebase firestore count", "system error $e")
                Toast.makeText(requireContext(), "Pilih Tanggal Terlebih Dahulu", Toast.LENGTH_SHORT).show()
            }

        }

//        countData()

        return root
    }

    private fun updateQuery() {
        val db = FirebaseFirestore.getInstance()
        val collection = db.collection("rekap")
        val query = collection
            .whereGreaterThanOrEqualTo("datetime", startDateTimestamp)
            .whereLessThanOrEqualTo("datetime", endDateTimestamp)

//        val countQuery = query.count()
//
//        countQuery.get(AggregateSource.SERVER).addOnCompleteListener {
//            if (it.isSuccessful) {
//                val snapshot = it.result
//                Toast.makeText(requireContext(), "Count: ${snapshot.count}", Toast.LENGTH_SHORT).show()
//                Log.d("firebase firestore count", "Count: ${snapshot.count}")
//                binding.textview2.text = snapshot.count.toString()
//            } else {
//                Log.d("firebase firestore count", "Count failed: ", it.exception)
//                Toast.makeText(requireContext(), "Count failed: ${it.exception}", Toast.LENGTH_SHORT).show()
//            }
//        }

        query.addSnapshotListener { snapshot, e ->
            try {
                if (e != null) {
                    Log.w("firebaseFirestoreProfile", "Listen failed.", e)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
//                    Log.d("firebaseFirestoreProfile", "Current data: ${snapshot.documents}")
//                    Log.d("firebaseFirestoreProfile", "Current data: ${snapshot.documents}")
//                    Log.d("firebaseFirestoreProfile", "Current data1: ${snapshot.documents.subList(0,3)}")
//                    Log.d("firebaseFirestoreProfile", "Current data3: ${snapshot.documents[0] }}")
//                    Log.d("firebaseFirestoreProfile", "Current data2: ${snapshot.documents[0].get("nama")}")
                    for (i in snapshot.documents.indices){
                        val aa = snapshot.documents[i].get("datetime")
                        val bb = aa.toString()
                        Log.d("firebaseFirestoreProfile", "Current data aa: $aa")
                        Log.d("firebaseFirestoreProfile", "Current data bb: $bb")
                    }

                } else {
                    Log.d("firebaseFirestoreProfile", "Current data: null")
                }
            } catch (e: Exception){
                Log.d("firebaseFirestoreProfile", "system error $e")
                Toast.makeText(requireContext(), "system error", Toast.LENGTH_SHORT).show()
            }

        }

    }

    private fun updateLabel(calendar: Calendar) {
//        val simpleDateFormat1 = SimpleDateFormat("MMMM D, yyyy")
//        startDate = simpleDateFormat1.format(calendar.time.toInstant().toEpochMilli())
        startDate = calendar.time.toInstant().toEpochMilli()
        Log.d("date2", calendar.time.toString())
        Log.d("date2", startDate.toString())
        startDateTimestamp = Timestamp(startDate!!.toLong())
        Log.d("date2", startDateTimestamp.toString())
        binding.textview.text = startDateTimestamp.toString()

//        val db = FirebaseFirestore.getInstance()
//        val collection = db.collection("presensi")
//        val query = collection
//            .whereGreaterThanOrEqualTo("datetime", startDateTimestamp)
////            .whereGreaterThanOrEqualTo("datetime", "2023-01-11 00:00:00.0")
//
//        val countQuery = query.count()
//
//        countQuery.get(AggregateSource.SERVER).addOnCompleteListener {
//            if (it.isSuccessful) {
//                val snapshot = it.result
//                Toast.makeText(activity, "Count: ${snapshot.count}", Toast.LENGTH_SHORT).show()
//                Log.d("firebase firestore count", "Count: ${snapshot.count}")
//            } else {
//                Log.d("firebase firestore count", "Count failed: ", it.exception)
//                Toast.makeText(activity, "Count failed: ${it.exception}", Toast.LENGTH_SHORT).show()
//            }
//        }
    }

    private fun updateLabel1(calendar: Calendar) {
//        val simpleDateFormat1 = SimpleDateFormat("MMMM D, yyyy")
//        startDate = simpleDateFormat1.format(calendar.time.toInstant().toEpochMilli())

//        if (startDate!!.toInt() < endDate!!.toInt()){
//            endDate = startDate
//
//        }

        endDate = calendar.time.toInstant().toEpochMilli()
        Log.d("date2", calendar.time.toString())
        Log.d("date2", endDate.toString())
        endDateTimestamp = Timestamp(endDate!!.toLong())
        Log.d("date2", endDateTimestamp.toString())
        binding.textview1.text = endDateTimestamp.toString()

//        val db = FirebaseFirestore.getInstance()
//        val collection = db.collection("presensi")
//        val query = collection
//            .whereLessThanOrEqualTo("datetime", endDateTimestamp)
////            .whereGreaterThanOrEqualTo("datetime", "2023-01-11 00:00:00.0")
//
//        val countQuery = query.count()
//
//        countQuery.get(AggregateSource.SERVER).addOnCompleteListener {
//            if (it.isSuccessful) {
//                val snapshot = it.result
//                Toast.makeText(activity, "Count: ${snapshot.count}", Toast.LENGTH_SHORT).show()
//                Log.d("firebase firestore count", "Count: ${snapshot.count}")
//            } else {
//                Log.d("firebase firestore count", "Count failed: ", it.exception)
//                Toast.makeText(activity, "Count failed: ${it.exception}", Toast.LENGTH_SHORT).show()
//            }
//        }
    }

    private fun countData(){
        val startOfDay = LocalDate.now().atStartOfDay(ZoneId.of("Asia/Jakarta")).toInstant().toEpochMilli()
        val dateTime4 = Timestamp(startOfDay)
        Log.d("date4", dateTime4.toString())

        val db = FirebaseFirestore.getInstance()
        val collection = db.collection("presensi")
        val query = collection
            .whereGreaterThanOrEqualTo("datetime", dateTime4)
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