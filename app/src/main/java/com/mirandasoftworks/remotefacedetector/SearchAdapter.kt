package com.mirandasoftworks.remotefacedetector

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mirandasoftworks.remotefacedetector.databinding.UserListBinding
import com.mirandasoftworks.remotefacedetector.model.Alat
import com.mirandasoftworks.remotefacedetector.model.Dosen
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class SearchAdapter() : RecyclerView.Adapter<SearchAdapter.ListViewHolder>(), Filterable {

    private var listSearch = ArrayList<Dosen>()
    private var listSearchFull = ArrayList<Dosen>()
    private var listSearchFull2 = ArrayList<Dosen>()

    fun setData(list: ArrayList<Dosen>){

//        if (list.contains(Dosen("","","","","","","pejabat"))){
//            Log.d("searchTestListSearchFullInitiation", list.toString())
//
//            this.listSearch = list
//            listSearchFull = ArrayList(listSearch)
//        }

//        if (list.any{ it.equals("pejabat")}){
//            Log.d("searchTestListSearchFullInitiation", list.toString())
//            list.filter { it.tipe_akun.equals("pejabat")}
//            this.listSearch = list
//            listSearchFull = ArrayList(listSearch)
//        }

//        if (list.filter { it.tipe_akun!!.contains("pejabat")}){
//            Log.d("searchTestListSearchFullInitiation", list.toString())
//            this.listSearch = list
//            listSearchFull = ArrayList(listSearch)
//        }

//        if (list.filter { it.tipe_akun.equals("pejabat")}){
//            Log.d("searchTestListSearchFullInitiation", list.toString())
//            this.listSearch = list
//            listSearchFull = ArrayList(listSearch)
//        }


        //work

//        val listFiltered = list.filter { it.tipe_akun.equals("pejabat")}
//        this.listSearch = listFiltered as ArrayList<Dosen>
//        listSearchFull = ArrayList(listSearch)



//        this.listSearch = list
//
//        listSearchFull2 = ArrayList(listSearch)
//        listSearchFull2.removeAt(0)
//        listSearchFull = ArrayList(listSearchFull2)

//
//          work

        this.listSearch = list
        listSearchFull = ArrayList(listSearch)



        Log.d("searchTestListSearchFullInitiationFilter", list.filter { it.tipe_akun.equals("pejabat")}.toString())
        Log.d("searchTestListSearchFullInitiationAny", list.any{ it.equals("pejabat")}.toString())
        Log.d("searchTestListSearchFullInitiation", listSearchFull.toString())

//        notifyDataSetChanged()

    }

//    private var exampleList: List<ExampleItem>? = null
//    private var exampleListFull: List<ExampleItem>? = null
//
//    internal class ExampleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        var imageView: ImageView
//        var textView1: TextView
//        var textView2: TextView
//
//        init {
//            imageView = itemView.findViewById(R.id.image_view)
//            textView1 = itemView.findViewById(R.id.text_view1)
//            textView2 = itemView.findViewById(R.id.text_view2)
//        }
//    }
//
//    fun ExampleAdapter(exampleList: List<ExampleItem>?) {
//        this.exampleList = exampleList
//        exampleListFull = ArrayList(exampleList)
//    }

    inner class ListViewHolder(itemView: UserListBinding) : RecyclerView.ViewHolder(itemView.root) {
        private val binding = itemView

        @SuppressLint("ResourceAsColor")
        fun bind(dosen: Dosen) {
            with(binding){
                val db = Firebase.firestore
                val collection = db.collection("alat")
                    .whereEqualTo("id", dosen.alat_id)
                collection.get()
                    .addOnSuccessListener { document ->
                        try {
                            val location = document.toObjects(Alat::class.java)[0].lokasi
                            Log.d("location","${document.toObjects(Alat::class.java)}")
                            Log.d("location","${document.toObjects(Alat::class.java)[0]}")
                            Log.d("location","${document.toObjects(Alat::class.java)[0].lokasi}")
                            tvLocation.text = location
                        } catch (e: Exception){
                            Log.d("rv", "system error $e")
                        }

                    }
                    .addOnFailureListener { exception ->
                        Log.d("rv", "get failed with ", exception)
                    }

//                val collection2 = db.collection("akun")
//                    .whereEqualTo("nama", dosen.nama)
//                collection2.get()
//                    .addOnSuccessListener { document ->
//                        try {
//                            val tipeAkun = document.toObjects(Dosen::class.java)[0].tipe_akun
//                            Log.d("locationAA","${document.toObjects(Dosen::class.java)}")
//                            Log.d("locationAA","${document.toObjects(Dosen::class.java)[0]}")
//                            Log.d("locationAA","${document.toObjects(Dosen::class.java)[0].tipe_akun}")
//                            if (tipeAkun == "mahasiswa"){
//                                binding.clUserList.setBackgroundColor(R.color.blue_sky)
////                                listSearch.remove(document.toObjects(Dosen::class.java)[0].nama)
//                            }
//                        } catch (e: Exception){
//                            Log.d("rv", "system error $e")
//                        }
//
//                    }
//                    .addOnFailureListener { exception ->
//                        Log.d("rv", "get failed with ", exception)
//                    }

                tvUsername.text = dosen.nama

                val simpleDateFormat = SimpleDateFormat("EEEE, dd LLLL yyyy")
                val date = simpleDateFormat.format(dosen.datetime!!.toDate())
                Log.d("rvTime", date)
                tvDate.text = date

                val simpleTimeFormat = SimpleDateFormat("hh:mm:ss aaa")
                val time = simpleTimeFormat.format(dosen.datetime.toDate())
                Log.d("rvTime", time)
                tvTime.text = time


//                listSearch.remove(dosen.takeIf {
//                    dosen.tipe_akun.equals("pejabat")
//                })
//                when {
//                    dosen.pekerjaan.equals("dosen") -> {
//                        binding.clUserList.setBackgroundColor(R.color.purple_700)
//                    }
//                    dosen.pekerjaan.equals("tendik") -> {
//                        binding.clUserList.setBackgroundColor(R.color.teal_200)
//                    }
//                    dosen.pekerjaan.equals("mahasiswa") -> {
//                        binding.clUserList.setBackgroundColor(R.color.blue_sky)
//                    }
//                }
//                when {
//                    dosen.pekerjaan.equals("dosen") -> {
//                        binding.llUserList.setBackgroundColor(
//                            ContextCompat.getColor(
//                                itemView.context,
//                                R.color.teal_700
//                            ))
//                    }
//                    dosen.pekerjaan.equals("mahasiswa") -> {
//                        binding.llUserList.setBackgroundColor(
//                            ContextCompat.getColor(
//                                root.context,
//                                R.color.blue_sky
//                            ))
//                    }
//                    else -> {
//                        binding.llUserList.setBackgroundColor(
//                            ContextCompat.getColor(
//                                itemView.context,
//                                R.color.white
//                            ))
//                    }
//                }

                when {
                    dosen.jenis_pekerjaan.equals("dosen") -> {
                        binding.llUserList.setBackgroundColor(
                            ContextCompat.getColor(
                                root.context,
                                R.color.teal_700
                            ))
                    }
                    dosen.jenis_pekerjaan.equals("mahasiswa") -> {
                        binding.llUserList.setBackgroundColor(
                            ContextCompat.getColor(
                                root.context,
                                R.color.blue_sky
                            ))
                    }
                    else -> {
                        binding.llUserList.setBackgroundColor(
                            ContextCompat.getColor(
                                root.context,
                                R.color.white
                            ))
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(UserListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listSearch[position])
    }

    override fun getItemCount(): Int = listSearch.size

    override fun getFilter(): Filter {
        return  object : Filter(){
            override fun performFiltering(query: CharSequence?): FilterResults {

                val filteredList: ArrayList<Dosen> = ArrayList()

                if (query == null || query.isEmpty()){
                    filteredList.addAll(listSearchFull)
                    Log.d("searchTestNull", listSearchFull.toString())

                } else{
                    val searchQuery = query.toString().toLowerCase().trim()

                    for (item in listSearchFull) {
                        if (item.nama!!.lowercase(Locale.ROOT).contains(searchQuery)){
                            filteredList.add(item)
                        }
                    }
                    Log.d("searchTest", filteredList.toString())
                }

                val filterResults = FilterResults()
                filterResults.values = filteredList

                return  filterResults

//                val filterResults = FilterResults()
//
//                if (query == null || query.length < 0){
//                    filterResults.count = listSearchFull.size
//                    filterResults.values = listSearchFull
//                } else{
//
//                    val searchQuery = query.toString().lowercase(Locale.ROOT).trim()
//
//                    val filterArrayList = ArrayList<Dosen>()
//
//                    for (search in listSearchFull){
//                        if (search.nama!!.contains(searchQuery)){
//                            filterArrayList.add(search)
//                        }
//                    }
//
//                    filterResults.count = filterArrayList.size
//                    filterResults.values = filterArrayList
//                }
//
//                return filterResults

            }

            override fun publishResults(query: CharSequence?, filteredResult: FilterResults?) {
                //error
                listSearch.clear()

                listSearch.addAll(filteredResult!!.values as ArrayList<Dosen>)
                Log.d("searchTestPublish", listSearch.toString())
                notifyDataSetChanged()

//                exampleList.clear()
//                exampleList.addAll(results.values as List<*>)
//                notifyDataSetChanged()

//                listSearch = filteredResult!!.values as ArrayList<Dosen>
//                notifyDataSetChanged()
            }

        }
    }
}