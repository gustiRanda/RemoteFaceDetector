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
    private var listMyData = mutableListOf<Any>()
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
                val selectedMonthStartDate = selectedMonthStart.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
                val selectedMonthStartTimestamp = Timestamp(selectedMonthStartDate)
                val selectedMonthEnd = selectedMonthStart.plusMonths(1)
                val selectedMonthEndDate = selectedMonthEnd.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
                val selectedMonthEndTimestamp = Timestamp(selectedMonthEndDate)

                val db = FirebaseFirestore.getInstance()
                val collection = db.collection("rekap")

                val query = collection
                    .whereGreaterThanOrEqualTo("datetime", selectedMonthStartTimestamp)
                    .whereLessThan("datetime", selectedMonthEndTimestamp)

                query.addSnapshotListener { snapshot, e ->
                    try {
                        if (e != null) {
                            Log.w("getDataAll", "Listen failed snapshot all.", e)
                            return@addSnapshotListener
                        }

                        if (snapshot != null) {
                            Log.w("getDataAll", "get data")
                            listStudent.clear()
                            listStaff.clear()
                            listLecturer.clear()
                            listAllData.clear()
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
                            Log.d("getDataAll", "Current data: null ")
                        }
                    } catch (e: Exception){
                        Log.d("getDataAll", "system error $e")
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
                val selectedMonthStartDate = selectedMonthStart.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
                val selectedMonthStartTimestamp = Timestamp(selectedMonthStartDate)
                val selectedMonthEnd = selectedMonthStart.plusMonths(1)
                val selectedMonthEndDate = selectedMonthEnd.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
                val selectedMonthEndTimestamp = Timestamp(selectedMonthEndDate)

                val db = FirebaseFirestore.getInstance()
                val collection = db.collection("rekap")
                val query = collection
                    .whereGreaterThanOrEqualTo("datetime", selectedMonthStartTimestamp)
                    .whereLessThan("datetime", selectedMonthEndTimestamp)
                    .whereEqualTo("nama", "${sharedPreferences.getString("name", "")}")

                query.addSnapshotListener { snapshot, e ->
                    try {
                        if (e != null) {
                            Log.w("getDataMy", "Listen failed snapshot1.", e)
                            return@addSnapshotListener
                        }
                        if (snapshot != null){
                            listMyData.clear()

                            listMyData.addAll(snapshot.documents)

                            binding.tvCurrentUserAttendance.text = "Kehadiran Saya Bulan ${selectedMonthStart.month.toString().lowercase().split(' ')
                                .joinToString(separator = " ") { word -> word.replaceFirstChar { it.uppercase() } }} $year ="
                            binding.tvSumCurrentUser.text = "${listMyData.count()}"
                        } else {
                            Log.d("getDataMy", "Current data: null ")
                        }
                    }  catch (e: Exception){
                        Log.d("getDataMy", "system error $e")
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