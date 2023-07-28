package com.mirandasoftworks.remotefacedetector

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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

    private var listStudent = mutableListOf<Any>()
    private var listStaff = mutableListOf<Any>()
    private var listLecturer = mutableListOf<Any>()
    private var list4 = mutableListOf<String>()
    private var listAllData = mutableListOf<Any>()



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
        } else if (sharedPreferences.getString("accountType", "") == "dosen/karyawan"){
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

        binding.btnCameraModuleList.setOnClickListener {
            val intent = Intent(this.requireActivity(), CameraModuleListActivity::class.java)
            startActivity(intent)
        }

        binding.btnAccountList.setOnClickListener {
            val intent = Intent(this.requireActivity(), AccountListActivity::class.java)
            startActivity(intent)
        }

        binding.btnUploadPhoto.setOnClickListener {
            val intent = Intent(this.requireActivity(), UploadPhotoActivity::class.java)
            startActivity(intent)
        }

        binding.btnChangePassword.setOnClickListener {
            val intent = Intent(this.requireActivity(), ChangePasswordActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogout.setOnClickListener {
            val dialog = Dialog(binding.root.context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.dialog_logout_alert)
            val btnYes: Button = dialog.findViewById(R.id.btn_yes)
            val btnNo: Button = dialog.findViewById(R.id.btn_no)

            btnYes.setOnClickListener {
                sharedPreferences.edit().putBoolean("loginState", false).apply()
                dialog.dismiss()
                val intent = Intent(this.requireActivity(), LoginActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }

            btnNo.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }

        return root
    }

    private fun viewTypeThree() {
        with(binding){
            cvRecapAll.visibility = View.VISIBLE
            btnCameraModuleList.visibility = View.VISIBLE
            btnAccountList.visibility = View.VISIBLE
            btnUploadPhoto.visibility = View.VISIBLE
        }
    }

    private fun viewTypeTwo() {
        with(binding){
            cvRecapAll.visibility = View.VISIBLE
            btnCameraModuleList.visibility = View.GONE
            btnAccountList.visibility = View.GONE
            btnUploadPhoto.visibility = View.GONE
        }
    }

    private fun viewTypeOne() {
        with(binding){
            cvRecapAll.visibility = View.GONE
            btnCameraModuleList.visibility = View.GONE
            btnAccountList.visibility = View.GONE
            btnUploadPhoto.visibility = View.GONE
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

                val query = collection
                    .whereGreaterThanOrEqualTo("datetime", selectedMonthStartTimestamp)
                    .whereLessThan("datetime", selectedMonthEndTimestamp)

                query.addSnapshotListener { snapshot, e ->
                    try {
                        if (e != null) {
                            Log.w("firebaseFirestoreProfile", "Listen failed snapshot all.", e)
                            return@addSnapshotListener
                        }

                        if (snapshot != null) {
                            listStudent.clear()
                            listStaff.clear()
                            listLecturer.clear()
                            listAllData.clear()
                            Log.d("firebaseFirestoreProfile", "Current data full snapshot1 all: ${snapshot.documents}")
                            Log.d("firebaseFirestoreProfile", "Current data full snapshot1[0] all: ${snapshot.documents[0]}")
                            listAllData.addAll(snapshot.documents)
                            listStudent = listAllData.filter { it.toString().lowercase().contains("mahasiswa") } as MutableList<Any>
                            listLecturer = listAllData.filter { it.toString().lowercase().contains("dosen") } as MutableList<Any>
                            listStaff = listAllData.filter { it.toString().lowercase().contains("karyawan") } as MutableList<Any>

                            binding.tvStudentAttendance.text = "Kehadiran Mahasiswa Bulan ${selectedMonthStart.month.toString().lowercase().split(' ')
                                .joinToString(separator = " ") { word -> word.replaceFirstChar { it.uppercase() } }} $year ="
                            binding.tvSumStudent.text = "${listStudent.count()}"

                            binding.tvStaffAttendance.text = "Kehadiran Karyawan Bulan ${selectedMonthStart.month.toString().lowercase().split(' ')
                                .joinToString(separator = " ") { word -> word.replaceFirstChar { it.uppercase() } }} $year ="
                            binding.tvSumStaff.text = "${listStaff.count()}"

                            binding.tvLecturerAttendance.text = "Kehadiran Dosen Bulan ${selectedMonthStart.month.toString().lowercase().split(' ')
                                .joinToString(separator = " ") { word -> word.replaceFirstChar { it.uppercase() } }} $year ="
                            binding.tvSumLecturer.text = "${listLecturer.count()}"

                            binding.tvAllAttendance.text = "Total Kehadiran Bulan ${selectedMonthStart.month.toString().lowercase().split(' ')
                                .joinToString(separator = " ") { word -> word.replaceFirstChar { it.uppercase() } }} $year ="
                            binding.tvSumAll.text = "${listAllData.count()}"

                        } else {
                            Log.d("firebaseFirestoreProfileAll", "Current data: null ")
                        }
                    } catch (e: Exception){
                        Log.d("firebaseFirestoreProfileAll", "system error $e")
                        // null not attached to a context.
//                        Toast.makeText(requireContext(), "system error", Toast.LENGTH_SHORT).show()
                        binding.tvStudentAttendance.text = "Kehadiran Mahasiswa Bulan ${selectedMonthStart.month.toString().lowercase().split(' ')
                            .joinToString(separator = " ") { word -> word.replaceFirstChar { it.uppercase() } }} $year ="
                        binding.tvSumStudent.text = "${listStudent.count()}"

                        binding.tvStaffAttendance.text = "Kehadiran Mahasiswa Bulan ${selectedMonthStart.month.toString().lowercase().split(' ')
                            .joinToString(separator = " ") { word -> word.replaceFirstChar { it.uppercase() } }} $year ="
                        binding.tvSumStaff.text = "${listStaff.count()}"

                        binding.tvLecturerAttendance.text = "Kehadiran Dosen Bulan ${selectedMonthStart.month.toString().lowercase().split(' ')
                            .joinToString(separator = " ") { word -> word.replaceFirstChar { it.uppercase() } }} $year ="
                        binding.tvSumLecturer.text = "${listLecturer.count()}"

                        binding.tvAllAttendance.text = "Total Kehadiran Bulan ${selectedMonthStart.month.toString().lowercase().split(' ')
                            .joinToString(separator = " ") { word -> word.replaceFirstChar { it.uppercase() } }} $year ="
                        binding.tvSumAll.text = "${listAllData.count()}"
                    }

                }

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