package com.mirandasoftworks.remotefacedetector

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
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

    fun setData(list: ArrayList<Dosen>){
//        listSearch.clear()
//        listSearch.addAll(list)
//        notifyDataSetChanged()
        this.listSearch = list
//        this.listSearchFull = list
        listSearchFull = ArrayList(listSearch)

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

        fun bind(dosen: Dosen) {

            with(binding){
                val db = Firebase.firestore
                val collection = db.collection("alat")
                    .whereEqualTo("id", dosen.alat_id)
                collection.get()
                    .addOnSuccessListener { document ->
                        try {
                            val location = document.toObjects(Alat::class.java)[0].lokasi
                            tvLocation.text = location
                        } catch (e: Exception){
                            Log.d("rv", "system error $e")
                        }

                    }
                    .addOnFailureListener { exception ->
                        Log.d("rv", "get failed with ", exception)
                    }

                tvUsername.text = dosen.nama

                val simpleDateFormat = SimpleDateFormat("EEEE, dd LLLL yyyy")
                val date = simpleDateFormat.format(dosen.datetime!!.toDate())
                Log.d("rvTime", date)
                tvDate.text = date

                val simpleTimeFormat = SimpleDateFormat("KK:mm:ss aaa")
                val time = simpleTimeFormat.format(dosen.datetime.toDate())
                Log.d("rvTime", time)
                tvTime.text = time
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

                if (query == null || query.length == 0){
                    filteredList.addAll(listSearchFull)

                } else{
                    val searchQuery = query.toString().toLowerCase().trim()

                    for (item in listSearchFull) {
                        if (item.nama!!.lowercase(Locale.ROOT).contains(searchQuery)){
                            filteredList.add(item)
                        }
                    }
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