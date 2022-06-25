package com.example.notification_trial02.adminSideFragments.RecyclerView

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.notification_trial02.R
import com.example.notification_trial02.modals.PatientAndHospital
import java.time.LocalDate

class patientAdapter(private var list : MutableList<PatientAndHospital>, private val listener : (Boolean, String) -> Unit)
    : RecyclerView.Adapter<patientAdapter.ViewHolder>() {

    val TAG = "Adapter"

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val patientName = itemView.findViewById<TextView>(R.id.name)
        val age = itemView.findViewById<TextView>(R.id.age)
        val sex = itemView.findViewById<TextView>(R.id.sex)
        val area = itemView.findViewById<TextView>(R.id.area)
        val hospitalNum = itemView.findViewById<TextView>(R.id.hospitalNum)
        val date = itemView.findViewById<TextView>(R.id.date)

        val deleteBtn = itemView.findViewById<ImageButton>(R.id.delteBtn)

        init {
            itemView.setOnClickListener {
                Log.d(TAG, "ItemView Clicked ${list[adapterPosition].date}")
                if (list[adapterPosition].date != null)
                    listener.invoke(true, list[adapterPosition].pk!!)
            }
            deleteBtn.setOnClickListener {
                Log.d(TAG, "delete btn Clicked ${list[adapterPosition].pk}")

                if (list[adapterPosition].date != null)
                    listener.invoke(false, list[adapterPosition].pk!!)

//                removeFromView(adapterPosition)
                list.removeAt(adapterPosition)
//                val newList = list
//                newList.removeAt(adapterPosition)
//                submitList(newList)
                notifyDataSetChanged()
            }
        }
    }

    private fun removeFromView(adapterPosition: Int) {
        list.removeAt(adapterPosition)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemLayout = LayoutInflater.from (parent.context)
            .inflate(R.layout.patient_view, parent, false)
        return ViewHolder(itemLayout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = list[position]
        holder.patientName.text = currentItem.name.toString()
        holder.age.text = currentItem.age.toString()
        holder.sex.text = currentItem.sex
        holder.area.text = currentItem.area.toString()
        holder.hospitalNum.text = currentItem.hospitalNumber.toString()
        holder.date.text = currentItem.date.toString()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun submitList (patientList : MutableList<PatientAndHospital>){
        Log.d(TAG, "submit list()")
        val oldList = list

        val diffResult : DiffUtil.DiffResult = DiffUtil.calculateDiff(
            PatientItemDiffCallBack(
                oldList,
                patientList
            )
        )
        list = patientList
        diffResult.dispatchUpdatesTo(this)
    }

    class PatientItemDiffCallBack(var oldPatientList : MutableList<PatientAndHospital>, var newPatientList : MutableList<PatientAndHospital>)
        :DiffUtil.Callback(){

        override fun getOldListSize(): Int {
            return oldPatientList.size
        }

        override fun getNewListSize(): Int {
            return newPatientList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return (oldPatientList[oldItemPosition].pk == newPatientList[newItemPosition].pk)
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldPatientList.get(oldItemPosition).equals(newPatientList.get(newItemPosition))
        }

    }
}