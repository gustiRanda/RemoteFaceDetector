package com.mirandasoftworks.remotefacedetector

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.androidplot.xy.*
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.FirebaseFirestore
import com.mirandasoftworks.remotefacedetector.databinding.FragmentProfileBinding
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*


class ProfileFragment : Fragment() {

    private lateinit var startDateTimestamp: Timestamp
    //2023-01-01 00:00:00.0
    private lateinit var endDateTimestamp: Timestamp
    private lateinit var fordate: Timestamp
    private var fordate1: Long? = null


    private var startDate: Long? = null
    private var endDate: Long? = null

    private var set = mutableSetOf<String>()
    private var set2 = mutableSetOf<String>()
    private var set3 = mutableSetOf<String>()
    private var set4 = mutableSetOf<String>()

    private var list = mutableListOf<String>()
    private var list2 = mutableListOf<String>()
    private var list3 = mutableListOf<String>()
    private var list4 = mutableListOf<String>()

    private var listSize: Int? = null

    private var map = mutableMapOf<String, Int>()



    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root



        val sharedPreferences = this.requireActivity().getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE)
        if (sharedPreferences.getString("accountType", "") == "non pejabat mahasiswa"){
            with(binding){
                button.visibility = View.GONE
            }
        } else{
            with(binding){
                button.visibility = View.VISIBLE
            }
        }

        binding.button.setOnClickListener {
            getMonthlySummaryData()
        }

        binding.button2.setOnClickListener {
            getSummaryDataOfCurrentUser2()
        }

        binding.btnLogout.setOnClickListener {
            sharedPreferences.edit().putBoolean("loginState", false).apply()
            val intent = Intent(this.requireActivity(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        binding.button3.setOnClickListener {
            val intent = Intent(this.requireActivity(), Camera::class.java)
            startActivity(intent)
        }

        binding.button4.setOnClickListener {
            val intent = Intent(this.requireActivity(), CreateAccountActivity::class.java)
            startActivity(intent)
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
//            getSummaryData()
            getSummaryDataOfCurrentUser()
            binding.swipeRefreshLayout.isRefreshing = false
        }



//        getSummaryData()
        getSummaryDataOfCurrentUser()


        return root
    }

    private fun getMonthlySummaryData() {
        MonthYearPickerDialog().apply {
            setListener { view, year, month, dayOfMonth ->

                val selectedMonthStart = LocalDate.parse("$dayOfMonth-${month+1}-$year", DateTimeFormatter.ofPattern("d-M-yyyy"))
                Log.d("profileFragmentSelectedMonth", "selectedMonthStart = $selectedMonthStart")

                val selectedMonthStartDate = selectedMonthStart.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
                Log.d("profileFragmentSelectedMonth", "selectedMonthStartDate = $selectedMonthStartDate")

                //2023-04-01 00:00:00.0
                val selectedMonthStartTimestamp = Timestamp(selectedMonthStartDate)
                Log.d("profileFragmentSelectedMonth", "selectedMonthStartTimestamp = $selectedMonthStartTimestamp")
                //2023-03-01 00:00:00.0

                val selectedMonthEnd = selectedMonthStart.plusMonths(1)
                Log.d("profileFragmentSelectedMonth", "selectedMonthEnd = $selectedMonthEnd")

                val selectedMonthEndDate = selectedMonthEnd.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
                Log.d("profileFragmentSelectedMonth", "selectedMonthEndDate = $selectedMonthEndDate")

                val selectedMonthEndTimestamp = Timestamp(selectedMonthEndDate)
                Log.d("profileFragmentSelectedMonth", "selectedMonthEndTimestamp = $selectedMonthEndTimestamp")

                val db = FirebaseFirestore.getInstance()
                val collection = db.collection("rekap")
                val query = collection
                    .whereGreaterThanOrEqualTo("datetime", selectedMonthStartTimestamp)
                    .whereLessThan("datetime", selectedMonthEndTimestamp)

                query.addSnapshotListener { snapshot, e ->
                    try {
                        if (e != null) {
                            Log.w("firebaseFirestoreProfile", "Listen failed snapshot1.", e)
                            return@addSnapshotListener
                        }

                        if (snapshot != null) {
                            list.clear()
                            set.clear()
                            Log.d("firebaseFirestoreProfile", "Current data full snapshot1: ${snapshot.documents}")
                            Log.d("firebaseFirestoreProfile", "Current data full snapshot1: ${snapshot.documents[0]}")
                            for (i in snapshot.documents.indices){
                                val dd = snapshot.documents[i]
                                val aa = snapshot.documents[i].get("datetime")
                                val bb = aa.toString().subSequence(18, 28).toString()
                                val cc = aa.toString().subSequence(42, 45).toString()


                                fordate = Timestamp(bb.toLong())
                                val fortime = (bb+cc).toLong()

                                val simpleTimeFormat = SimpleDateFormat("KK:mm:ss aaa")
                                val time = simpleTimeFormat.format(fortime)

                                val simpleDateFormat = SimpleDateFormat("dd MM yyyy")
                                val date = simpleDateFormat.format(fortime)

                                val ee = dd.get("nama")
                                for (c in listOf(ee)){
                                    Log.d("firebaseFirestoreProfile", "Current data ee: ${listOf(c)}")
                                    Log.d("firebaseFirestoreProfile", "Current data ee: ${listOf(c)[1]}")

                                }
                                Log.d("firebaseFirestoreProfile", "Current data ee: $ee")


                                Log.d("firebaseFirestoreProfile", "Current data id: ${i+1}")
                                Log.d("firebaseFirestoreProfile", "Current data nama: ${snapshot.documents[i].get("nama")}")
                                Log.d("firebaseFirestoreProfile", "Current data aa: $aa")
                                Log.d("firebaseFirestoreProfile", "Current data dd: $dd")
                                Log.d("firebaseFirestoreProfile", "Current data time: $time")
                                Log.d("firebaseFirestoreProfile", "Current data date: $date")


                                list.add(date)
                                Log.d("firebaseFirestoreProfile", "Current data date list: $list")
                                Log.d("firebaseFirestoreProfile", "Current data date list size: ${list.size}")
                                Log.d("firebaseFirestoreProfile", "Current data date list count: ${list.count()}")


                                set = list.toMutableSet()
                                Log.d("firebaseFirestoreProfile", "Current data date set: $set")
                                Log.d("firebaseFirestoreProfile", "Current data date set size: ${set.size}")
                                Log.d("firebaseFirestoreProfile", "Current data date set count: ${set.count()}")

                                binding.textview7.text = "Total Kehadiran Bulan ${selectedMonthStart.month} $year = ${set.count()}"
                            }



                        } else {
                            Log.d("firebaseFirestoreProfile", "Current data: null ")
                        }
                    } catch (e: Exception){
                        Log.d("firebaseFirestoreProfile", "system error $e")
                        // null not attached to a context.
//                        Toast.makeText(requireContext(), "system error", Toast.LENGTH_SHORT).show()
                    }

                }

                val query1 = collection
                    .whereEqualTo("pekerjaan", "Dosen")
                    .whereGreaterThanOrEqualTo("datetime", selectedMonthStartTimestamp)
                    .whereLessThan("datetime", selectedMonthEndTimestamp)

                query1.addSnapshotListener { snapshot, e ->
                    try {
                        if (e != null) {
                            Log.w("firebaseFirestoreProfile", "Listen failed snapshot2.", e)
                            return@addSnapshotListener
                        }

                        if (snapshot != null) {
                            list2.clear()
                            set2.clear()
                            for (i in snapshot.documents.indices){
                                val aa = snapshot.documents[i].get("datetime")
                                val bb = aa.toString().subSequence(18, 28).toString()
                                val cc = aa.toString().subSequence(42, 45).toString()


                                fordate = Timestamp(bb.toLong())
                                val fortime = (bb+cc).toLong()

                                val simpleTimeFormat = SimpleDateFormat("KK:mm:ss aaa")
                                val time = simpleTimeFormat.format(fortime)

                                val simpleDateFormat = SimpleDateFormat("dd MM yyyy")
                                val date = simpleDateFormat.format(fortime)

                                Log.d("firebaseFirestoreProfile", "Current data id: ${i+1}")
                                Log.d("firebaseFirestoreProfile", "Current data nama: ${snapshot.documents[i].get("nama")}")
                                Log.d("firebaseFirestoreProfile", "Current data aa: $aa")
                                Log.d("firebaseFirestoreProfile", "Current data time: $time")
                                Log.d("firebaseFirestoreProfile", "Current data date: $date")


                                list2.add(date)
                                Log.d("firebaseFirestoreProfile", "Current data date list2: $list2")
                                Log.d("firebaseFirestoreProfile", "Current data date list2 size: ${list2.size}")
                                Log.d("firebaseFirestoreProfile", "Current data date list2 count: ${list2.count()}")


                                set2 = list2.toMutableSet()
                                Log.d("firebaseFirestoreProfile", "Current data date set2: $set2")
                                Log.d("firebaseFirestoreProfile", "Current data date set2 size: ${set2.size}")
                                Log.d("firebaseFirestoreProfile", "Current data date set2 count: ${set2.count()}")

                                binding.textview8.text = "Total Kehadiran Dosen Bulan ${selectedMonthStart.month} $year = ${set2.count()}"
                            }



                        } else {
                            Log.d("firebaseFirestoreProfile", "Current data: null")
                        }
                    } catch (e: Exception){
                        Log.d("firebaseFirestoreProfile", "system error $e")
                        // null not attached to a context.
//                        Toast.makeText(requireContext(), "system error", Toast.LENGTH_SHORT).show()
                    }

                }

                val query2 = collection
                    .whereEqualTo("pekerjaan", "Mahasiswa")
                    .whereGreaterThanOrEqualTo("datetime", selectedMonthStartTimestamp)
                    .whereLessThan("datetime", selectedMonthEndTimestamp)

                query2.addSnapshotListener { snapshot, e ->
                    try {
                        if (e != null) {
                            Log.w("firebaseFirestoreProfile", "Listen failed snapshot2.", e)
                            return@addSnapshotListener
                        }

                        if (snapshot != null) {
                            list3.clear()
                            set3.clear()
                            for (i in snapshot.documents.indices){
                                val aa = snapshot.documents[i].get("datetime")
                                val bb = aa.toString().subSequence(18, 28).toString()
                                val cc = aa.toString().subSequence(42, 45).toString()


                                fordate = Timestamp(bb.toLong())
                                val fortime = (bb+cc).toLong()

                                val simpleTimeFormat = SimpleDateFormat("KK:mm:ss aaa")
                                val time = simpleTimeFormat.format(fortime)

                                val simpleDateFormat = SimpleDateFormat("dd MM yyyy")
                                val date = simpleDateFormat.format(fortime)

                                Log.d("firebaseFirestoreProfile", "Current data id: ${i+1}")
                                Log.d("firebaseFirestoreProfile", "Current data nama: ${snapshot.documents[i].get("nama")}")
                                Log.d("firebaseFirestoreProfile", "Current data aa: $aa")
                                Log.d("firebaseFirestoreProfile", "Current data time: $time")
                                Log.d("firebaseFirestoreProfile", "Current data date: $date")


                                list3.add(date)
                                Log.d("firebaseFirestoreProfile", "Current data date list3: $list3")
                                Log.d("firebaseFirestoreProfile", "Current data date list3 size: ${list3.size}")
                                Log.d("firebaseFirestoreProfile", "Current data date list3 count: ${list3.count()}")


                                set3 = list3.toMutableSet()
                                Log.d("firebaseFirestoreProfile", "Current data date set3: $set3")
                                Log.d("firebaseFirestoreProfile", "Current data date set3 size: ${set3.size}")
                                Log.d("firebaseFirestoreProfile", "Current data date set3 count: ${set3.count()}")

                                binding.textview9.text = "Total Kehadiran Mahasiswa Bulan ${selectedMonthStart.month} $year = ${set3.count()}"
                            }



                        } else {
                            Log.d("firebaseFirestoreProfile", "Current data: null")
                        }
                    } catch (e: Exception){
                        Log.d("firebaseFirestoreProfile", "system error $e")
                        // null not attached to a context.
//                        Toast.makeText(requireContext(), "system error", Toast.LENGTH_SHORT).show()
                    }

                }

            }
            show(this@ProfileFragment.parentFragmentManager, "MonthYearPickerDialog")
        }
    }


    private fun getSummaryDataOfCurrentUser() {

        //monthly data count
        val dayOfMonth = LocalDate.now().atStartOfDay(ZoneId.of("Asia/Jakarta")).dayOfMonth -1
        val startOfMonth = LocalDate.now().atStartOfDay(ZoneId.of("Asia/Jakarta")).minusDays(
            dayOfMonth.toLong()
        ).toInstant().toEpochMilli()
        val startOfMonthTimestamp = Timestamp(startOfMonth)
        Log.d("dateMonthStart", dayOfMonth.toString())
        Log.d("dateMonthStart", startOfMonth.toString())
        Log.d("dateMonthStart", startOfMonthTimestamp.toString())


        val currentMonth = LocalDate.now().atStartOfDay(ZoneId.of("Asia/Jakarta")).month.value
        val endOfMonth = LocalDate.now().atStartOfDay(ZoneId.of("Asia/Jakarta")).plusMonths(1).minusDays(
            dayOfMonth.toLong()
        ).toInstant().toEpochMilli()
        val endOfMonthTimestamp = Timestamp(endOfMonth)
//        val endOfMonthTimestamp = Timestamp(endOfMonth)
//        val startOfDay2 = ZonedDateTime.of(2023, 1, 11, 0, 0, 0, 0, zoneId2).toInstant().toEpochMilli()
        Log.d("dateMonthEnd", currentMonth.toString())
        Log.d("dateMonthEnd", endOfMonth.toString())
        Log.d("dateMonthEnd", endOfMonthTimestamp.toString())



        val sharedPreferences = this.requireActivity().getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE)


        val db1 = FirebaseFirestore.getInstance()
        val collection1 = db1.collection("rekap")
        val query1 = collection1
            .whereGreaterThanOrEqualTo("datetime", startOfMonthTimestamp)
            .whereLessThan("datetime", endOfMonthTimestamp)
            .whereEqualTo("nama", "${sharedPreferences.getString("name", "")}")

        val countQuery1 = query1.count()
        countQuery1.get(AggregateSource.SERVER).addOnCompleteListener {
            if (it.isSuccessful) {
                val snapshot = it.result
                Log.d("firebasFirestoreCount", "Count Bulan ini: ${snapshot.count}")
                binding.textview5.text = "Kehadiran anda bulan ini = ${snapshot.count}"
            } else {
                Log.d("firebasFirestoreCount", "Count failed: ", it.exception)
                Toast.makeText(activity, "Gagal Mengambil Data Bulan Ini, Silakan Periksa Koneksi Internet Anda", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun getSummaryDataOfCurrentUser2() {

        val sharedPreferences = this.requireActivity().getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE)

        MonthYearPickerDialog().apply {
            setListener { view, year, month, dayOfMonth ->

                val selectedMonthStart = LocalDate.parse("$dayOfMonth-${month+1}-$year", DateTimeFormatter.ofPattern("d-M-yyyy"))
                Log.d("profileFragmentSelectedMonth", "selectedMonthStart = $selectedMonthStart")

                val selectedMonthStartDate = selectedMonthStart.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
                Log.d("profileFragmentSelectedMonth", "selectedMonthStartDate = $selectedMonthStartDate")

                //2023-04-01 00:00:00.0
                val selectedMonthStartTimestamp = Timestamp(selectedMonthStartDate)
                Log.d("profileFragmentSelectedMonth", "selectedMonthStartTimestamp = $selectedMonthStartTimestamp")
                //2023-03-01 00:00:00.0

                val selectedMonthEnd = selectedMonthStart.plusMonths(1)
                Log.d("profileFragmentSelectedMonth", "selectedMonthEnd = $selectedMonthEnd")

                val selectedMonthEndDate = selectedMonthEnd.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
                Log.d("profileFragmentSelectedMonth", "selectedMonthEndDate = $selectedMonthEndDate")

                val selectedMonthEndTimestamp = Timestamp(selectedMonthEndDate)
                Log.d("profileFragmentSelectedMonth", "selectedMonthEndTimestamp = $selectedMonthEndTimestamp")

                val db = FirebaseFirestore.getInstance()
                val collection = db.collection("rekap")
                val query = collection
                    .whereGreaterThanOrEqualTo("datetime", selectedMonthStartTimestamp)
                    .whereLessThan("datetime", selectedMonthEndTimestamp)
                    .whereEqualTo("nama", "${sharedPreferences.getString("name", "")}")

                query.addSnapshotListener { snapshot, e ->
                    try {
                        if (e != null) {
                            Log.w("firebaseFirestoreProfile2", "Listen failed snapshot1.", e)
                            return@addSnapshotListener
                        }
                        if (snapshot != null){
                            list4.clear()
                            set4.clear()
                            Log.d("firebaseFirestoreProfile2", "Current data full snapshot1: ${snapshot.documents}")

                            if (snapshot.documents.size == 0){
                                binding.textview6.text = "kehadiran anda bulan ${selectedMonthStart.month} $year = 0"
                            }

                            Log.d("firebaseFirestoreProfile2", "Current data index snapshot1: ${snapshot.documents[0]}")

                            for (i in snapshot.documents.indices){
                                val dd = snapshot.documents[i]
                                val aa = snapshot.documents[i].get("datetime")
                                val bb = aa.toString().subSequence(18, 28).toString()
                                val cc = aa.toString().subSequence(42, 45).toString()


                                fordate = Timestamp(bb.toLong())
                                val fortime = (bb+cc).toLong()

                                val simpleTimeFormat = SimpleDateFormat("KK:mm:ss aaa")
                                val time = simpleTimeFormat.format(fortime)

                                val simpleDateFormat = SimpleDateFormat("dd MM yyyy")
                                val date = simpleDateFormat.format(fortime)

                                val ee = dd.get("nama")
//                                for (c in listOf(ee)){
//                                    Log.d("firebaseFirestoreProfile2", "Current data ee: ${listOf(c)}")
//                                    Log.d("firebaseFirestoreProfile2", "Current data ee: ${listOf(c)[1]}")
//
//                                }
                                Log.d("firebaseFirestoreProfile2", "Current data ee: $ee")


                                Log.d("firebaseFirestoreProfile2", "Current data id: ${i+1}")
                                Log.d("firebaseFirestoreProfile2", "Current data nama: ${snapshot.documents[i].get("nama")}")
                                Log.d("firebaseFirestoreProfile2", "Current data aa: $aa")
                                Log.d("firebaseFirestoreProfile2", "Current data dd: $dd")
                                Log.d("firebaseFirestoreProfile2", "Current data time: $time")
                                Log.d("firebaseFirestoreProfile2", "Current data date: $date")


                                list4.add(date)
                                Log.d("firebaseFirestoreProfile2", "Current data date list: $list4")
                                Log.d("firebaseFirestoreProfile2", "Current data date list size: ${list4.size}")
                                Log.d("firebaseFirestoreProfile2", "Current data date list count: ${list4.count()}")


                                set4 = list4.toMutableSet()
                                Log.d("firebaseFirestoreProfile2", "Current data date set: $set4")
                                Log.d("firebaseFirestoreProfile2", "Current data date set size: ${set4.size}")
                                Log.d("firebaseFirestoreProfile2", "Current data date set count: ${set4.count()}")

                                binding.textview6.text = "kehadiran anda bulan ${selectedMonthStart.month} $year = ${set4.count()}"
                            }
                        } else {
                            Log.d("firebaseFirestoreProfile2", "Current data: null ")
                        }
                    }  catch (e: Exception){
                        Log.d("firebaseFirestoreProfile2", "system error $e")
                        // null not attached to a context.
//                        Toast.makeText(requireContext(), "system error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            show(this@ProfileFragment.parentFragmentManager, "MonthYearPickerDialog")
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}