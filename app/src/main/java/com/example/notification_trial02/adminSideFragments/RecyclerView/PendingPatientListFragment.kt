package com.example.notification_trial02.adminSideFragments.RecyclerView

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notification_trial02.PENDING
import com.example.notification_trial02.R
import com.example.notification_trial02.ViewModal.PateintViewModel
import com.example.notification_trial02.databinding.FragmentPendingPatientListBinding
import com.example.notification_trial02.databinding.PatientViewBinding
import com.example.notification_trial02.modals.PatientAndHospital
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//TODO : delete all unrequired created instances

class PendingPatientListFragment : Fragment() {

    private lateinit var binding: FragmentPendingPatientListBinding
//    private val db = FirebaseFirestore.getInstance()
    private lateinit var patients : ArrayList<PatientAndHospital>

    private val TAG = "RecyclerView"

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

        //TODO recycler view is adding same items over and over again

        viewModel.getAllPendingPateint()
        viewModel.patientList.observe(viewLifecycleOwner){
            patients.clear()
            Log.d(TAG, "onViewCreated: LIST SHLD BE CLEAR BUT/AND..." + patients)
            patients.addAll(it)
            setUpRecyclerView()
        }
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
                            id
                        )

                    )
                } else {
                    Log.d(TAG, "delete from database function called")
                    viewModel.deletePatientFromPendingList(id)
                }
            }
        }
    }
}