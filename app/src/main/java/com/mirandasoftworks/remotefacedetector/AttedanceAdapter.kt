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
import com.mirandasoftworks.remotefacedetector.databinding.PersonListBinding
import com.mirandasoftworks.remotefacedetector.model.Alat
import com.mirandasoftworks.remotefacedetector.model.Person
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class AttedanceAdapter() : RecyclerView.Adapter<AttedanceAdapter.ListViewHolder>(), Filterable {

    private var listSearch = ArrayList<Person>()
    private var listSearchFull = ArrayList<Person>()
    private var listSearchFull2 = ArrayList<Person>()

    fun setData(list: ArrayList<Person>){

        this.listSearch = list
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

    inner class ListViewHolder(itemView: PersonListBinding) : RecyclerView.ViewHolder(itemView.root) {
        private val binding = itemView

        @SuppressLint("ResourceAsColor")
        fun bind(person: Person) {
            with(binding){
                val db = Firebase.firestore
                val collection = db.collection("alat")
                    .whereEqualTo("id", person.alat_id)
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

                tvUsername.text = person.nama

                val simpleDateFormat = SimpleDateFormat("EEEE, dd LLLL yyyy")
                val date = simpleDateFormat.format(person.datetime!!.toDate())
                Log.d("rvTime", date)
                tvDate.text = date

                val simpleTimeFormat = SimpleDateFormat("hh:mm:ss aaa")
                val time = simpleTimeFormat.format(person.datetime.toDate())
                Log.d("rvTime", time)
                tvTime.text = time

                when {
                    person.jenis_pekerjaan.equals("dosen") -> {
                        binding.llPersonList.setBackgroundColor(
                            ContextCompat.getColor(
                                root.context,
                                R.color.blue_sky
                            ))
                    }
                    person.jenis_pekerjaan.equals("tendik") -> {
                        binding.llPersonList.setBackgroundColor(
                            ContextCompat.getColor(
                                root.context,
                                R.color.light_gray
                            ))
                    }
                    else -> {
                        binding.llPersonList.setBackgroundColor(
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
        return ListViewHolder(PersonListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listSearch[position])
    }

    override fun getItemCount(): Int = listSearch.size

    override fun getFilter(): Filter {
        return  object : Filter(){
            override fun performFiltering(query: CharSequence?): FilterResults {

                val filteredList: ArrayList<Person> = ArrayList()

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

            }

            override fun publishResults(query: CharSequence?, filteredResult: FilterResults?) {
                //error
                listSearch.clear()

                listSearch.addAll(filteredResult!!.values as ArrayList<Person>)
                Log.d("searchTestPublish", listSearch.toString())
                notifyDataSetChanged()

            }

        }
    }
}