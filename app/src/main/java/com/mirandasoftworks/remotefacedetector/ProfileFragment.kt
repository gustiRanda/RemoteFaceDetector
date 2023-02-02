package com.mirandasoftworks.remotefacedetector

import android.graphics.Color
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
import java.text.FieldPosition
import java.text.Format
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Year
import java.time.ZoneId
import java.util.*
import kotlin.time.Duration.Companion.milliseconds


class ProfileFragment : Fragment() {

    private lateinit var startDateTimestamp: Timestamp
    //2023-01-01 00:00:00.0
    private lateinit var endDateTimestamp: Timestamp
    private lateinit var fordate: Timestamp
    private var fordate1: Long? = null


    private var startDate: Long? = null
    private var endDate: Long? = null

    private var set = mutableSetOf<String>()
    private var set2 = mutableSetOf<Int>()
    private var set3 = mutableSetOf<Int>()

    private var list = mutableListOf<String>()
    private var list2 = mutableListOf<String>()
    private var list3 = mutableListOf<Int>()

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

//        val datePickerDialog = DatePickerDialog.OnDateSetListener { _, year, month, day  ->
//            calendar.set(Calendar.YEAR, year)
//            calendar.set(Calendar.MONTH, month)
//            calendar.set(Calendar.DAY_OF_MONTH, day)
//            calendar.set(Calendar.HOUR_OF_DAY, 0)
//            calendar.set(Calendar.MINUTE, 0)
//            calendar.set(Calendar.SECOND, 0)
//            calendar.set(Calendar.MILLISECOND, 0)
//            updateLabel(calendar)
////            updateQuery()
//
//        }
//
//        val datePickerDialog1 = DatePickerDialog.OnDateSetListener { _, year, month, day  ->
//            calendar.set(Calendar.YEAR, year)
//            calendar.set(Calendar.MONTH, month)
//            calendar.set(Calendar.DAY_OF_MONTH, day)
//            calendar.set(Calendar.HOUR_OF_DAY, 23)
//            calendar.set(Calendar.MINUTE, 59)
//            calendar.set(Calendar.SECOND, 59)
//            calendar.set(Calendar.MILLISECOND, 999)
//            updateLabel1(calendar)
////            updateQuery()
//
//        }
//
//        binding.button.setOnClickListener {
//            DatePickerDialog(requireContext(), datePickerDialog, calendar.get(Calendar.YEAR),
//            calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
//        }
//
//        binding.button1.setOnClickListener {
//            DatePickerDialog(requireContext(), datePickerDialog1, calendar.get(Calendar.YEAR),
//                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
//        }

        binding.button2.setOnClickListener {
            try {
                list.clear()
//                updateQueryForGraph()
//                updateGraph()
            } catch (e: Exception){
                Log.d("firebase firestore count", "system error $e")
                Toast.makeText(requireContext(), "Pilih Tanggal Terlebih Dahulu", Toast.LENGTH_SHORT).show()
            }

        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            updateData()
            binding.swipeRefreshLayout.isRefreshing = false
        }
        updateData()

//        countData()

//        getLastTimeStampOfCurrentMonthCalendar()
//        getLastTimeStampOfCurrentMonth()
//        getLastTimeStampOfCurrentMonth1()

        return root
    }

    private fun getLastTimeStampOfCurrentMonthCalendar() {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Jakarta"))
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1, 0,0,0)
        val aaa = calendar.time
//        val caaaaa = calendar.fi
//        Log.d("dateMonthStartCalendar", caaa.toString())
        Log.d("dateMonthStartCalendar", aaa.toString())
//        Log.d("dateMonthStartCalendar", caaaaa.toString())
        val caaaa = Timestamp(aaa.toInstant().toEpochMilli())
        Log.d("dateMonthStartCalendar", caaaa.toString())
    }

    fun getLastTimeStampOfCurrentMonth1(): Long {
        val calendar = Calendar.getInstance()
        // passing month-1 because 0-->jan, 1-->feb... 11-->dec
        calendar.set(Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH)
        calendar[Calendar.DATE] = calendar.getActualMaximum(Calendar.DATE)
        val date = calendar.time
        Log.d("dateMonthStartFun", date.time.toString())
        val aaaaaa = Timestamp(date.time)
        Log.d("dateMonthStartFun", aaaaaa.toString())
        return date.time
    }

    private fun getLastTimeStampOfCurrentMonth(): Long {
        val calendar = Calendar.getInstance()
        // passing month-1 because 0-->jan, 1-->feb... 11-->dec
        calendar[Calendar.getInstance()[Calendar.YEAR], Calendar.getInstance()[Calendar.DAY_OF_MONTH]] = 1
        calendar[Calendar.DATE] = calendar.getActualMaximum(Calendar.DATE)
        val date = calendar.time
        Log.d("dateMonthStartFun", date.time.toString())
        val aaaaaa = Timestamp(date.time)
        Log.d("dateMonthStartFun", aaaaaa.toString())
        return date.time
    }


    private fun updateGraph() {
        binding.xyPlot.clear()
        binding.xyPlot.setUserDomainOrigin(0)
        binding.xyPlot.setUserRangeOrigin(0)


        val label = arrayOf(
            "2022-12-22",
            "#4343",
            "#434",
            "443r",
            "3r3r",
            "2022-12-22",
            "2022-12-22",
            "2022-12-22",
            "2022-12-22",
            "2022-12-22",
            "#4343",
            "#434",
            "443r",
            "3r3r",
            "2022-12-22",
            "2022-12-22",
            "2022-12-22",
            "2022-12-22"
        )
        val domainLabel = arrayOf<Number>(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13)
        val series1Number =
            arrayOf<Number>(1, 2, 3, 4, 10, 5, 15, 12, 15, 1, 2, 3, 4, 10, 5, 15, 12, 15)


        //simple
        val series1: XYSeries = SimpleXYSeries(
            list3,
            SimpleXYSeries.ArrayFormat.Y_VALS_ONLY,
            "Jumlah Kehadiran"
        )

        Log.d("firebaseFirestoreProfilePlot", "Current data : ${Arrays.asList(*series1Number)}")
        Log.d("firebaseFirestoreProfilePlot", "Current data map values to mutabel sit: $list3")
        Log.d("firebaseFirestoreProfilePlot", "Current data set2 to mutabel sit: ${ set2.toMutableList()}")
        // [1, 2, 3, 4, 10, 5, 15, 12, 15, 1, 2, 3, 4, 10, 5, 15, 12, 15]

//        val series1: XYSeries = SimpleXYSeries(
//            (set2.toMutableList().toList()),
//            SimpleXYSeries.ArrayFormat.Y_VALS_ONLY,
//            "Jumlah Hadir"
//        )





        val series1Format = LineAndPointFormatter(
            Color.BLUE, Color.BLACK,
            null, null
        )
        val series2Format = LineAndPointFormatter(
            Color.DKGRAY, Color.BLACK,
            null, null
        )

        series1Format.interpolationParams = CatmullRomInterpolator.Params(
            10,
            CatmullRomInterpolator.Type.Centripetal
        )
        series2Format.interpolationParams = CatmullRomInterpolator.Params(
            10,
            CatmullRomInterpolator.Type.Centripetal
        )

        binding.xyPlot.addSeries(series1, series1Format)





        //set stepping
        binding.xyPlot.setDomainStep(StepMode.INCREMENT_BY_VAL, 1.0)
        binding.xyPlot.setRangeStep(StepMode.INCREMENT_BY_VAL, 1.0)



        //set label pos to use
        binding.xyPlot.getGraph().setLineLabelEdges(
            XYGraphWidget.Edge.BOTTOM,
            XYGraphWidget.Edge.LEFT
        )

        binding.xyPlot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setFormat(object : Format() {
            override fun format(
                o: Any,
                stringBuffer: StringBuffer,
                fieldPosition: FieldPosition
            ): StringBuffer {
//                int i = Math.round(((Number) o).floatValue());
//                return stringBuffer.append(domainLabel[i]);
                val i = Math.round((o as Number).toFloat())


                return stringBuffer.append(list2[i])

//                int i = Math.round(((Number) o).floatValue());
//                return stringBuffer.append(arrayList.get(i).getDate());
            }

            override fun parseObject(s: String, parsePosition: ParsePosition): Any? {
                return null
            }
        })

//        xyPlot.setMarkupEnabled(true);
        ;
        PanZoom.attach(
            binding.xyPlot,
            PanZoom.Pan.BOTH,
            PanZoom.Zoom.STRETCH_BOTH,
            PanZoom.ZoomLimit.MIN_TICKS
        )

        //set graph limit
        //maxX must be max value of the label so that graph do not flash white
        binding.xyPlot.getOuterLimits().set(0, listSize, 0, list3.max())

    }

    private fun updateData() {

        //daily data count
        val startOfDay = LocalDate.now().atStartOfDay(ZoneId.of("Asia/Jakarta")).toInstant().toEpochMilli()
        val dateTime4 = Timestamp(startOfDay)
        Log.d("date", dateTime4.toString())
        Log.d("date", startOfDay.toString())

        val endOfDay = LocalDate.now().atStartOfDay(ZoneId.of("Asia/Jakarta")).plusDays(1).toInstant().toEpochMilli()
        val aa = Timestamp(endOfDay)
//        val startOfDay2 = ZonedDateTime.of(2023, 1, 11, 0, 0, 0, 0, zoneId2).toInstant().toEpochMilli()
        Log.d("dateaa", aa.toString())

        startDateTimestamp = dateTime4
        endDateTimestamp = aa


        val db = FirebaseFirestore.getInstance()
        val collection = db.collection("presensi")
        val query = collection
            .whereGreaterThanOrEqualTo("datetime", dateTime4)
            .whereLessThanOrEqualTo("datetime", aa)

        val countQuery = query.count()
        countQuery.get(AggregateSource.SERVER).addOnCompleteListener {
            if (it.isSuccessful) {
                val snapshot = it.result
                Toast.makeText(activity, "Count hari ini: ${snapshot.count}", Toast.LENGTH_SHORT).show()
                Log.d("firebasFirestoreCount", "Count hari ini: ${snapshot.count}")
                binding.textview3.text = "Kehadiran hari ini = ${snapshot.count}"
            } else {
                Log.d("firebasFirestoreCount", "Count failed: ", it.exception)
                Toast.makeText(activity, "Gagal Mengambil Data Hari Ini, Silakan Periksa Koneksi Internet Anda", Toast.LENGTH_SHORT).show()
            }
        }



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

        startDateTimestamp = dateTime4
        endDateTimestamp = aa


        val db1 = FirebaseFirestore.getInstance()
        val collection1 = db1.collection("presensi")
        val query1 = collection1
            .whereGreaterThanOrEqualTo("datetime", startOfMonthTimestamp)
            .whereLessThan("datetime", endOfMonthTimestamp)

        val countQuery1 = query1.count()
        countQuery1.get(AggregateSource.SERVER).addOnCompleteListener {
            if (it.isSuccessful) {
                val snapshot = it.result
                Toast.makeText(activity, "Count Bulan ini: ${snapshot.count}", Toast.LENGTH_SHORT).show()
                Log.d("firebasFirestoreCount", "Count Bulan ini: ${snapshot.count}")
                binding.textview4.text = "Kehadiran bulan ini = ${snapshot.count}"
            } else {
                Log.d("firebasFirestoreCount", "Count failed: ", it.exception)
                Toast.makeText(activity, "Gagal Mengambil Data Bulan Ini, Silakan Periksa Koneksi Internet Anda", Toast.LENGTH_SHORT).show()
            }
        }

    }


    private fun updateQueryForGraph() {
        val db = FirebaseFirestore.getInstance()
        val collection = db.collection("rekap")
        val query = collection
            .whereGreaterThanOrEqualTo("datetime", startDateTimestamp)
            .whereLessThanOrEqualTo("datetime", endDateTimestamp)

//
//        val countQuery = query.count()
//
//        countQuery.get(AggregateSource.SERVER).addOnCompleteListener {
//            if (it.isSuccessful) {
//                val snapshot = it.result
//                Toast.makeText(requireContext(), "Count: ${snapshot.count}", Toast.LENGTH_SHORT).show()
//                Log.d("firebaseFirestoreCount", "Count: ${snapshot.count}")
//                binding.textview2.text = snapshot.count.toString()
//            } else {
//                Log.d("firebaseFirestoreCount", "Count failed: ", it.exception)
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
                    for (i in snapshot.documents.indices){
                        val aa = snapshot.documents[i].get("datetime")
                        val bb = aa.toString().subSequence(18, 28).toString()
                        val cc = aa.toString().subSequence(42, 45).toString()


                        //fordate = timestamp
                        fordate = Timestamp(bb.toLong())
                        val fortime = (bb+cc).toLong()
//                        val aaa = Timestamp(aa as Long)

                        val simpleTimeFormat = SimpleDateFormat("KK:mm:ss aaa")
                        val time = simpleTimeFormat.format(fortime)

                        val simpleDateFormat = SimpleDateFormat("dd yyyy")
                        val date = simpleDateFormat.format(fortime)
                        //fordate1 = long
//                        fordate1 = Timestamp(bb) as Long
                        Log.d("firebaseFirestoreProfile", "Current data id: ${i+1}")
                        Log.d("firebaseFirestoreProfile", "Current data nama: ${snapshot.documents[i].get("nama")}")
                        Log.d("firebaseFirestoreProfile", "Current data aa: $aa")
//                        Log.d("firebaseFirestoreProfile", "Current data aa: $aaa")
                        Log.d("firebaseFirestoreProfile", "Current data time: $time")
                        Log.d("firebaseFirestoreProfile", "Current data date: $date")
//                        Log.d("firebaseFirestoreProfile", "Current data bb: $bb")
//                        Log.d("firebaseFirestoreProfile", "Current data fortime: $fortime")
//                        Log.d("firebaseFirestoreProfile", "Current data cc: $cc")
//                        Log.d("firebaseFirestoreProfile", "Current data fordate: $fordate")
//                        Log.d("firebaseFirestoreProfile", "Current data fordate1: $fordate1")
                        Log.d("firebaseFirestoreProfile", "Current data startDateTimestamp: $startDateTimestamp")
                        Log.d("firebaseFirestoreProfile", "Current data endDateTimestamp: ${endDateTimestamp.toInstant().toEpochMilli()}")


                        list.add(date)
                        Log.d("firebaseFirestoreProfile", "Current data date list: $list")
                        Log.d("firebaseFirestoreProfile", "Current data date list size: ${list.size}")
                        Log.d("firebaseFirestoreProfile", "Current data date list count: ${list.count()}")

                        list.forEach { value ->
                            val dateValue: (String) -> Boolean = {it.contentEquals(value)}
                            val countEachElementList = list.count(dateValue)
                            Log.d("firebaseFirestoreProfile", "Current data date list: $value = $countEachElementList")
//                            map = mutableMapOf(ii to countEachElemetList)
                            map.put(value, countEachElementList)
                            Log.d("firebaseFirestoreProfile", "Current data date map: $map")
                            Log.d("firebaseFirestoreProfile", "Current data date map keys: ${map.keys}")
                            Log.d("firebaseFirestoreProfile", "Current data date map values: ${map.values}")

                        }
//                        map.forEach { s, i ->
//                            Log.d("firebaseFirestoreProfile", "Current data date map s: $s")
//                            Log.d("firebaseFirestoreProfile", "Current data date map i: $i")
//                        }

                        set = list.toMutableSet()
                        Log.d("firebaseFirestoreProfile", "Current data date set: $set")
                        Log.d("firebaseFirestoreProfile", "Current data date set size: ${set.size}")
                        Log.d("firebaseFirestoreProfile", "Current data date set count: ${set.count()}")
//
                        list2 = set.toMutableList()
                        Log.d("firebaseFirestoreProfile", "Current data date list2 : $list2")
                        Log.d("firebaseFirestoreProfile", "Current data date list2 size count: ${list2.count()}")
                        listSize = list2.count()-1
                        Log.d("firebaseFirestoreProfile", "Current data date list2 size count: $listSize")
//
                        list3 = map.values.toMutableList()
                        Log.d("firebaseFirestoreProfile", "Current data date list3: ${list3.max()}")
                        Log.d("firebaseFirestoreProfile", "Current data date list3 count: ${list3.count()}")


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