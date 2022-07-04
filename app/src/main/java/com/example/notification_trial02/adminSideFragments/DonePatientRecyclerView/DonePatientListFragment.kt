package com.example.notification_trial02.adminSideFragments.DonePatientRecyclerView

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notification_trial02.Utils.NetworkResponse
import com.example.notification_trial02.ViewModal.PateintViewModel
import com.example.notification_trial02.databinding.FragmentDonePatientListBinding
import com.example.notification_trial02.modals.PatientAndHospital
import com.google.firebase.firestore.FirebaseFirestore

class DonePatientListFragment : Fragment() {

    private lateinit var binding : FragmentDonePatientListBinding
    private val TAG = "DonePatientFragment"
    private val db = FirebaseFirestore.getInstance()
    private var patients = ArrayList<PatientAndHospital>()

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
        // Inflate the layout for this fragment
        Log.d(TAG, "onCreateView")
        binding = FragmentDonePatientListBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated")

        viewModel.patientListResponse.observe(viewLifecycleOwner){ response ->
            when(response){
                is NetworkResponse.Loading -> {
                    binding.progressbar.visibility = View.VISIBLE
                    binding.donePatientRV.visibility = View.GONE
                }
                is NetworkResponse.Success -> {
                    binding.progressbar.visibility = View.GONE
                    binding.donePatientRV.visibility = View.VISIBLE
                }
                else -> {
                    binding.errMsg.visibility = View.VISIBLE
                    binding.errorImg.visibility = View.VISIBLE
                }
            }
        }


        viewModel.getAllDonePatient()
        viewModel.patientList.observe(viewLifecycleOwner){
            patients.clear()
            patients.addAll(it)
            setUpRecyclerView()
        }
    }

    private fun setUpRecyclerView() {
        with(binding.donePatientRV) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = DonePatientAdapter(patients){ flag, id ->
                if (flag){
                    Log.d(TAG, "navigate function called")
                    findNavController().navigate(
                        DonePatientListFragmentDirections.actionDonePatientListFragmentToShowPatientPrescription(id)
                    )
                }
                else{
                    Log.d(TAG, "delete from database function called")
                    viewModel.deleltePatientFromDoneList(id)
                }
            }
        }
    }
}