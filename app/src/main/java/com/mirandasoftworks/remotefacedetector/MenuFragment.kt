package com.mirandasoftworks.remotefacedetector

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import com.mirandasoftworks.remotefacedetector.databinding.FragmentMenuBinding
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter


class MenuFragment : Fragment() {

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
    private var map1 = mutableMapOf<String, String>()



    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        val root: View = binding.root



        val sharedPreferences = this.requireActivity().getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE)
        if (sharedPreferences.getString("accountType", "") == "mahasiswa"){
            viewTypeOne()
        } else if (sharedPreferences.getString("accountType", "") == "dosen"){
            viewTypeOne()
        } else if (sharedPreferences.getString("accountType", "") == "pejabat"){
            viewTypeTwo()
        } else{
            viewTypeThree()
        }

        binding.btnRecapAllData.setOnClickListener {
            getMonthlyRecapData()
        }

        binding.btnRecapMe.setOnClickListener {
            getSummaryDataOfCurrentUser()
        }

        binding.btnAddCameraModule.setOnClickListener {
            val intent = Intent(this.requireActivity(), AddCameraModuleActivity::class.java)
            startActivity(intent)
        }

        binding.btnCreateAccount.setOnClickListener {
            val intent = Intent(this.requireActivity(), CreateAccountActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogout.setOnClickListener {
            sharedPreferences.edit().putBoolean("loginState", false).apply()
            val intent = Intent(this.requireActivity(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        return root
    }

    private fun viewTypeThree() {
        with(binding){
            cvRecapAll.visibility = View.VISIBLE
            btnAddCameraModule.visibility = View.VISIBLE
            btnCreateAccount.visibility = View.VISIBLE
        }
    }

    private fun viewTypeTwo() {
        with(binding){
            cvRecapAll.visibility = View.VISIBLE
            btnAddCameraModule.visibility = View.GONE
            btnCreateAccount.visibility = View.GONE
        }
    }

    private fun viewTypeOne() {
        with(binding){
            cvRecapAll.visibility = View.GONE
            btnAddCameraModule.visibility = View.GONE
            btnCreateAccount.visibility = View.GONE
        }
    }

    private fun getMonthlyRecapData() {
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

//                val query = collection
//                    .whereGreaterThanOrEqualTo("datetime", selectedMonthStartTimestamp)
//                    .whereLessThan("datetime", selectedMonthEndTimestamp)
//
//                query.addSnapshotListener { snapshot, e ->
//                    try {
//                        if (e != null) {
//                            Log.w("firebaseFirestoreProfile", "Listen failed snapshot all.", e)
//                            return@addSnapshotListener
//                        }
//
//                        if (snapshot != null) {
//                            list.clear()
//                            set.clear()
//                            Log.d("firebaseFirestoreProfile", "Current data full snapshot1 all: ${snapshot.documents}")
//                            Log.d("firebaseFirestoreProfile", "Current data full snapshot1[0] all: ${snapshot.documents[0]}")
//                            for (i in snapshot.documents.indices){
//                                val dd = snapshot.documents[i]
//                                val aa = snapshot.documents[i].get("datetime")
//                                val bb = aa.toString().subSequence(18, 28).toString()
//                                val cc = aa.toString().subSequence(42, 45).toString()
//
//
//                                fordate = Timestamp(bb.toLong())
//                                val fortime = (bb+cc).toLong()
//
//                                val simpleTimeFormat = SimpleDateFormat("KK:mm:ss aaa")
//                                val time = simpleTimeFormat.format(fortime)
//
//                                val simpleDateFormat = SimpleDateFormat("dd MM yyyy")
//                                val date = simpleDateFormat.format(fortime)
//
//                                val ee = dd.get("nama")
//                                Log.d("firebaseFirestoreProfile", "Current data ee all: $ee")
//
//
//                                Log.d("firebaseFirestoreProfile", "Current data id: ${i+1}")
//                                Log.d("firebaseFirestoreProfile", "Current data nama: ${snapshot.documents[i].get("nama")}")
//                                Log.d("firebaseFirestoreProfile", "Current data aa: $aa")
//                                Log.d("firebaseFirestoreProfile", "Current data dd: $dd")
//                                Log.d("firebaseFirestoreProfile", "Current data time: $time")
//                                Log.d("firebaseFirestoreProfile", "Current data date: $date")
//
//
//                                list.add(date)
//                                Log.d("firebaseFirestoreProfile", "Current data date list: $list")
//                                Log.d("firebaseFirestoreProfile", "Current data date list size: ${list.size}")
//                                Log.d("firebaseFirestoreProfile", "Current data date list count: ${list.count()}")
//
//
//                                set = list.toMutableSet()
//                                Log.d("firebaseFirestoreProfile", "Current data date set: $set")
//                                Log.d("firebaseFirestoreProfile", "Current data date set size: ${set.size}")
//                                Log.d("firebaseFirestoreProfileAll", "Current data date set count: ${set.count()}")
//
//                                binding.tvAllAttendance.text = "Total Kehadiran Bulan ${selectedMonthStart.month.toString().lowercase().split(' ')
//                                    .joinToString(separator = " ") { word -> word.replaceFirstChar { it.uppercase() } }} $year ="
//                                binding.tvSumAll.text = "${set.count()}"
//
//                            }
//
//
//
//                        } else {
//                            Log.d("firebaseFirestoreProfileAll", "Current data: null ")
//                        }
//                    } catch (e: Exception){
//                        Log.d("firebaseFirestoreProfileAll", "system error $e")
//                        // null not attached to a context.
////                        Toast.makeText(requireContext(), "system error", Toast.LENGTH_SHORT).show()
//                        binding.tvAllAttendance.text = "Total Kehadiran Bulan ${selectedMonthStart.month.toString().lowercase().split(' ')
//                            .joinToString(separator = " ") { word -> word.replaceFirstChar { it.uppercase() } }} $year ="
//                        binding.tvSumAll.text = "${set.count()}"
//                    }
//
//                }
                //mungkin bisa ditimpa per hari
                val collection2 = db.collection("presensitest")
                val query1 = collection2
                    .whereEqualTo("jenis_pekerjaan", "mahasiswa")
                    .whereGreaterThanOrEqualTo("datetime", selectedMonthStartTimestamp)
                    .whereLessThan("datetime", selectedMonthEndTimestamp)

                query1.addSnapshotListener { snapshot, e ->
                    try {
                        if (e != null) {
                            Log.w("firebaseFirestoreProfile", "Listen failed snapshot dosen.", e)
                            return@addSnapshotListener
                        }

                        if (snapshot != null) {
                            list2.clear()
                            set2.clear()
                            Log.d("firebaseFirestoreProfile", "Current data full snapshot dosen: ${snapshot.documents}")
                            Log.d("firebaseFirestoreProfile", "Current data full snapshot[1] dosen: ${snapshot.documents[0]}")

                            for (i in snapshot.documents.indices){



                                val aa = snapshot.documents[i].get("datetime")
                                val nama = snapshot.documents[i].get("nama")



                                val bb = aa.toString().subSequence(18, 28).toString()
                                val cc = aa.toString().subSequence(42, 45).toString()


                                fordate = Timestamp(bb.toLong())
                                val fortime = (bb+cc).toLong()

                                val simpleTimeFormat = SimpleDateFormat("KK:mm:ss aaa")
                                val time = simpleTimeFormat.format(fortime)

                                val simpleDateFormat = SimpleDateFormat("dd MM yyyy")
                                val date = simpleDateFormat.format(fortime)

//                                if (nama.toString() == nama.toString()){
//
//                                }

//                                map1[nama.toString()+"$i"] = date
//
//                                Log.d("firebaseFirestoreProfile", "Current data date map size : ${map1.size}")
//
//                                map1.forEach { s, i ->
//                                    Log.d("firebaseFirestoreProfile", "Current data date map s: $s")
//                                    Log.d("firebaseFirestoreProfile", "Current data date map i: $i")
//                                }

                                Log.d("firebaseFirestoreProfile", "Current data id dosen: ${i+1}")
                                Log.d("firebaseFirestoreProfile", "Current data nama dosen: ${snapshot.documents[i].get("nama")}")
                                Log.d("firebaseFirestoreProfile", "Current data aa dosen: $aa")
                                Log.d("firebaseFirestoreProfile", "Current data time: $time")
                                Log.d("firebaseFirestoreProfile", "Current data date: $date")


                                list2.add(date)
                                Log.d("firebaseFirestoreProfile", "Current data date list2: $list2")
                                Log.d("firebaseFirestoreProfile", "Current data date list2 size: ${list2.size}")
                                Log.d("firebaseFirestoreProfile", "Current data date list2 count: ${list2.count()}")


                                set2 = list2.toMutableSet()
                                Log.d("firebaseFirestoreProfile", "Current data date set2: $set2")
                                Log.d("firebaseFirestoreProfile", "Current data date set2 size: ${set2.size}")
                                Log.d("firebaseFirestoreProfileDosen", "Current data date set2 count: ${set2.count()}")

//                                binding.textview8.text = "Total Kehadiran Dosen Bulan ${selectedMonthStart.month} $year = ${set2.count()}"
                                binding.tvLecturerAttendance.text = "Kehadiran Dosen Bulan ${selectedMonthStart.month.toString().lowercase().split(' ')
                                    .joinToString(separator = " ") { word -> word.replaceFirstChar { it.uppercase() } }} $year ="
                                binding.tvSumLecturer.text = "${list2.count()}"
                            }



                        } else {
                            Log.d("firebaseFirestoreProfileDosen", "Current data: null")
                        }
                    } catch (e: Exception){
                        Log.d("firebaseFirestoreProfileDosen", "system error $e")
                        binding.tvLecturerAttendance.text = "Kehadiran Dosen Bulan ${selectedMonthStart.month.toString().lowercase().split(' ')
                            .joinToString(separator = " ") { word -> word.replaceFirstChar { it.uppercase() } }} $year ="
                        binding.tvSumLecturer.text = "${set2.count()}"
                        // null not attached to a context.
//                        Toast.makeText(requireContext(), "system error", Toast.LENGTH_SHORT).show()
                    }

                }
//
//                val query2 = collection
//                    .whereEqualTo("jenis_pekerjaan", "mahasiswa")
//                    .whereGreaterThanOrEqualTo("datetime", selectedMonthStartTimestamp)
//                    .whereLessThan("datetime", selectedMonthEndTimestamp)
//
//                query2.addSnapshotListener { snapshot, e ->
//                    try {
//                        if (e != null) {
//                            Log.w("firebaseFirestoreProfileMahasiswa", "Listen failed snapshot mahasiswa.", e)
//                            return@addSnapshotListener
//                        }
//
//                        if (snapshot != null) {
//                            list3.clear()
//                            set3.clear()
//
//                            for (i in snapshot.documents.indices){
//                                val aa = snapshot.documents[i].get("datetime")
//                                val bb = aa.toString().subSequence(18, 28).toString()
//                                val cc = aa.toString().subSequence(42, 45).toString()
//
//
//                                fordate = Timestamp(bb.toLong())
//                                val fortime = (bb+cc).toLong()
//
//                                val simpleTimeFormat = SimpleDateFormat("KK:mm:ss aaa")
//                                val time = simpleTimeFormat.format(fortime)
//
//                                val simpleDateFormat = SimpleDateFormat("dd MM yyyy")
//                                val date = simpleDateFormat.format(fortime)
//
//                                Log.d("firebaseFirestoreProfileMahasiswa", "Current data id mahasiswa: ${i+1}")
//                                Log.d("firebaseFirestoreProfileMahasiswa", "Current data nama mahasiswa: ${snapshot.documents[i].get("nama")}")
//                                Log.d("firebaseFirestoreProfileMahasiswa", "Current data aa mahasiswa: $aa")
//                                Log.d("firebaseFirestoreProfileMahasiswa", "Current data time: $time")
//                                Log.d("firebaseFirestoreProfileMahasiswa", "Current data date: $date")
//
//
//                                list3.add(date)
//                                Log.d("firebaseFirestoreProfileMahasiswa", "Current data date list3: $list3")
//                                Log.d("firebaseFirestoreProfileMahasiswa", "Current data date list3 size: ${list3.size}")
//                                Log.d("firebaseFirestoreProfileMahasiswa", "Current data date list3 count: ${list3.count()}")
//
//
//                                set3 = list3.toMutableSet()
//                                Log.d("firebaseFirestoreProfileMahasiswa", "Current data date set3: $set3")
//                                Log.d("firebaseFirestoreProfileMahasiswa", "Current data date set3 size: ${set3.size}")
//                                Log.d("firebaseFirestoreProfileMahasiswa", "Current data date set3 count: ${set3.count()}")
//
//                                binding.tvStudentAttendance.text = "Kehadiran Mahasiswa Bulan ${selectedMonthStart.month.toString().lowercase().split(' ')
//                                    .joinToString(separator = " ") { word -> word.replaceFirstChar { it.uppercase() } }} $year ="
//                                binding.tvSumStudent.text = "${set3.count()}"
//                            }
//
//
//
//                        } else {
//                            Log.d("firebaseFirestoreProfileMahasiswa", "Current data: null")
////                            binding.tvStudentAttendance.text = "Kehadiran Mahasiswa Bulan ${selectedMonthStart.month.toString().lowercase().split(' ')
////                                .joinToString(separator = " ") { word -> word.replaceFirstChar { it.uppercase() } }} $year ="
////                            binding.tvSumStudent.text = "${set3.count()}"
//                        }
//                    } catch (e: Exception){
//                        Log.d("firebaseFirestoreProfileMahasiswa", "system error $e")
//                        // null not attached to a context.
////                        Toast.makeText(requireContext(), "system error", Toast.LENGTH_SHORT).show()
//                        binding.tvStudentAttendance.text = "Kehadiran Mahasiswa Bulan ${selectedMonthStart.month.toString().lowercase().split(' ')
//                            .joinToString(separator = " ") { word -> word.replaceFirstChar { it.uppercase() } }} $year ="
//                        binding.tvSumStudent.text = "${set3.count()}"
//                    }
//
//                }

            }
            show(this@MenuFragment.parentFragmentManager, "MonthYearPickerDialog")
        }
    }


    private fun getSummaryDataOfCurrentUser() {

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
                                binding.tvSumCurrentUser.text = "kehadiran anda bulan ${selectedMonthStart.month} $year = 0"
                            }

                            Log.d("firebaseFirestoreProfile2", "Current data index snapshot1[0]: ${snapshot.documents[0]}")

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



                                ///bisa langsung dimasukkan ke set tanpa listt

//                                list4.add(date)
//                                Log.d("firebaseFirestoreProfile2", "Current data date list: $list4")
//                                Log.d("firebaseFirestoreProfile2", "Current data date list size: ${list4.size}")
//                                Log.d("firebaseFirestoreProfile2", "Current data date list count: ${list4.count()}")
//
//
//                                set4 = list4.toMutableSet()
//                                Log.d("firebaseFirestoreProfile2", "Current data date set: $set4")
//                                Log.d("firebaseFirestoreProfile2", "Current data date set size: ${set4.size}")
//                                Log.d("firebaseFirestoreProfile2", "Current data date set count: ${set4.count()}")



                                //langsung masuk set

                                set4.add(date)
                                Log.d("firebaseFirestoreProfile2", "Current data date set: $set4")
                                Log.d("firebaseFirestoreProfile2", "Current data date set size: ${set4.size}")
                                Log.d("firebaseFirestoreProfile2", "Current data date set count: ${set4.count()}")

                                binding.tvCurrentUserAttendance.text = "Kehadiran Saya Bulan ${selectedMonthStart.month.toString().lowercase().split(' ')
                                    .joinToString(separator = " ") { word -> word.replaceFirstChar { it.uppercase() } }} $year ="
                                binding.tvSumCurrentUser.text = "${set4.count()}"
                            }
                        } else {
                            Log.d("firebaseFirestoreProfile2", "Current data: null ")
                        }
                    }  catch (e: Exception){
                        Log.d("firebaseFirestoreProfile2", "system error $e")
                        // null not attached to a context.
//                        Toast.makeText(requireContext(), "system error", Toast.LENGTH_SHORT).show()
                        binding.tvCurrentUserAttendance.text = "Kehadiran Saya Bulan ${selectedMonthStart.month.toString().lowercase().split(' ')
                            .joinToString(separator = " ") { word -> word.replaceFirstChar { it.uppercase() } }} $year ="
                        binding.tvSumCurrentUser.text = "${set4.count()}"
                    }
                }
            }
            show(this@MenuFragment.parentFragmentManager, "MonthYearPickerDialog")
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}