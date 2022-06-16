package com.example.notification_trial02.adminSideFragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.notification_trial02.R
import com.example.notification_trial02.databinding.FragmentPendingPatientListBinding
import com.example.notification_trial02.databinding.FragmentPretherapyFormDetailsBinding
import com.example.notification_trial02.modals.PatientAndHospital
import com.google.firebase.firestore.FirebaseFirestore

class PretherapyFormDetails : Fragment() {

    private lateinit var binding : FragmentPretherapyFormDetailsBinding
    private val db = FirebaseFirestore.getInstance()

    var patient : PatientAndHospital? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPretherapyFormDetailsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val time = PretherapyFormDetailsArgs.fromBundle(requireArguments()).date
//        getPatientByTime(time)
    }

//    private fun getPatientByTime(time: String) {
//        db.collection("PendingPatient").document(time)
//            .get()
//            .addOnSuccessListener { result ->
//                if(result != null){
//                    patient = result.toObject(PatientAndHospital::class.java)
//                    binding.progressBar.visibility = View.GONE
//                    Log.d(TAG, "Success: $patient" )
//                    setPatientData()
//                }
//            }
//            .addOnFailureListener { exception ->
//                binding.progressBar.visibility = View.GONE
//                Toast.makeText(requireContext(), "Error occured $exception", Toast.LENGTH_SHORT).show()
//                Log.d(TAG, "Error getting documents: ", exception)
//            }
//    }


}