package com.example.notification_trial02.adminSideFragments.RecyclerView

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notification_trial02.PENDING_PATIENT_LIST
import com.example.notification_trial02.R
import com.example.notification_trial02.Utils.NetworkResponse
import com.example.notification_trial02.ViewModal.PateintViewModel
import com.example.notification_trial02.databinding.FragmentPendingPatientListBinding
import com.example.notification_trial02.modals.PatientAndHospital
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import kotlinx.android.extensions.LayoutContainer

//TODO : delete all unrequired created instances
val TAG = "RecyclerView"
private var patients = ArrayList<PatientAndHospital>()

class PendingPatientListFragment : Fragment() {

    private lateinit var binding: FragmentPendingPatientListBinding
//    private val db = FirebaseFirestore.getInstance()
//    private val dbRef = FirebaseDatabase.getInstance()
//    private lateinit var adapter : FirebaseRecyclerAdapter<PatientAndHospital, MyViewHolder?>


    private val viewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        ).get(PateintViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Log.d(TAG, "onCreateView")
        binding = FragmentPendingPatientListBinding.inflate(inflater, container, false)
        patients = ArrayList<PatientAndHospital>()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated")

        viewModel.patientListResponse.observe(viewLifecycleOwner){ response ->
            when(response){
                is NetworkResponse.Loading -> {
                    binding.progressbar.visibility = View.VISIBLE
                    binding.patientRecyclerView.visibility = View.GONE
                }
                is NetworkResponse.Success -> {
                    binding.progressbar.visibility = View.GONE
                    binding.patientRecyclerView.visibility = View.VISIBLE
                }
                else -> {
                    binding.errMsg.visibility = View.VISIBLE
                    binding.errorImg.visibility = View.VISIBLE
                }
            }
        }

        viewModel.getAllPendingPateint()
        viewModel.patientList.observe(viewLifecycleOwner){
            patients.clear()
            Log.d(TAG, "onViewCreated: LIST SHLD BE CLEAR BUT/AND..." + patients)
            patients.addAll(it)
            setUpRecyclerView()
        }
//        binding.patientRecyclerView.layoutManager = LinearLayoutManager(requireContext())
//        setupRealtimeRecyclerView()
    }

    private fun setUpRecyclerView() {
        Log.d(TAG, "the pending patient list is " + patients)
        with(binding.patientRecyclerView) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = patientAdapter(patients) { flag, id ->
                if (flag) {
                    Log.d(TAG, "navigate function called")
                    findNavController().navigate(
                        PendingPatientListFragmentDirections.actionPendingPatientListToPostTherapyFormFragment(
                            id)
                    )
                } else {
                    Log.d(TAG, "delete from database function called")

                    AlertDialog.Builder(requireContext())
                        .setTitle("Are you sure?")
                        .setMessage("Click OK to send the alert for oxygen therapy")
                        .setPositiveButton("OK"){ dialog, _ ->
                            viewModel.deletePatientFromPendingList(id)
                        }
                        .setNegativeButton("Cancel"){dialog, _ ->
                            dialog.cancel()
                        }
                        .show()
                }
            }
        }
    }

//    private fun setupRealtimeRecyclerView() {
//        val query : Query = dbRef //Query object
//            .reference
//            .child(PENDING_PATIENT_LIST) //we are querying all the children in this path
//
//        val options : FirebaseRecyclerOptions<PatientAndHospital> =
//            FirebaseRecyclerOptions.Builder<PatientAndHospital>()
//                .setQuery(query) {
//                    Log.d(TAG, "setupRealtimeRecyclerView: $it")
//                    val patient = PatientAndHospital(
//                            it.child("name").value.toString(),
//                            it.child("age").value.toString(),
//                            it.child("sex").value.toString(),
//                            it.child("date").value.toString(),
//                            it.child("area").value.toString(),
//                            it.child("hospitalNumber").value.toString(),
//                            it.child("pk").value.toString()
//                        )
//                    patients.add(patient)
//                    PatientAndHospital(
//                        it.child("name").value.toString(),
//                        it.child("age").value.toString(),
//                        it.child("sex").value.toString(),
//                        it.child("date").value.toString(),
//                        it.child("area").value.toString(),
//                        it.child("hospitalNumber").value.toString(),
//                        it.child("pk").value.toString()
//                    )
//                }
//                .build()
//        adapter = object : FirebaseRecyclerAdapter <PatientAndHospital, MyViewHolder?>(options){
//
//            override fun onCreateViewHolder(
//                parent: ViewGroup,
//                viewType: Int
//            ): MyViewHolder {
//                val view : View =  LayoutInflater.from(parent.context)
//                    .inflate(R.layout.patient_view, parent, false)
//                return MyViewHolder(view)
//            }
//
//            override fun onBindViewHolder(
//                holder: MyViewHolder,
//                position: Int,
//                model: PatientAndHospital
//            ) {
//                Log.d(TAG, "onBindViewHolder: ${model.name}, ${model.hospitalNumber}")
//                holder.patientName.text = model.name
//                holder.age.text = model.age
//                holder.sex.text = model.sex
//                holder.area.text = model.area
//                holder.hospitalNum.text = model.hospitalNumber
//                holder.date.text = model.date
//            }
//        }
//        binding.patientRecyclerView.adapter = adapter
//
//    }

//    class MyViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
//        val patientName = itemView.findViewById<TextView>(R.id.name)
//        val age = itemView.findViewById<TextView>(R.id.age)
//        val sex = itemView.findViewById<TextView>(R.id.sex)
//        val area = itemView.findViewById<TextView>(R.id.area)
//        val hospitalNum = itemView.findViewById<TextView>(R.id.hospitalNum)
//        val date = itemView.findViewById<TextView>(R.id.date)
//        val deleteBtn = itemView.findViewById<ImageButton>(R.id.delteBtn)
//
//        init {
//            itemView.setOnClickListener {
//                Log.d(TAG, "itemview clicked: ${adapterPosition} ")
//                val id = patients[adapterPosition].pk.toString()
//                Log.d(TAG, "and its id is: $id")
//                findNavController(it).navigate(
//                        PendingPatientListFragmentDirections.actionPendingPatientListToPostTherapyFormFragment(
//                            id))
//            }
//        }
//    }

//    override fun onStart() {
//        super.onStart()
//        adapter.startListening()
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        adapter.stopListening()
//    }
}