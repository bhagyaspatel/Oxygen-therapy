package com.example.notification_trial02.adminSideFragments.RecyclerView

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notification_trial02.R
import com.example.notification_trial02.databinding.FragmentPendingPatientListBinding
import com.example.notification_trial02.databinding.PatientViewBinding
import com.example.notification_trial02.modals.PatientAndHospital
import com.google.firebase.firestore.FirebaseFirestore

class PendingPatientList : Fragment() {

    private lateinit var binding : FragmentPendingPatientListBinding
    private val db = FirebaseFirestore.getInstance()
    private var patientList = ArrayList<PatientAndHospital>()

    private val TAG = "RecyclerView"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPendingPatientListBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db.collection("PendingPatient")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val patient: PatientAndHospital = document.toObject(PatientAndHospital::class.java)
                    patientList.add(patient)
                }
                setUpRecyclerView()
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }
    }

    private fun setUpRecyclerView() {
        with(binding.patientRecyclerView) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = patientAdapter(patientList){ flag, date ->
                //pass the function to delete the patient
                if (flag){
                    Log.d(TAG, "navigate function called")
                    findNavController().navigate(
                        PendingPatientListDirections.actionPendingPatientListToPretherapyFormDetails(date)
                    )
                }
                else{
                    Log.d(TAG, "delete from database function called")

                    db.collection("PendingPatient").document(date).delete()
                        .addOnSuccessListener {
                            Toast.makeText(requireContext(), "Patient Details Removed", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener{
                            Log.d(TAG, it.message!!)
                            Log.d(TAG, it.message!!)
                        }
                }
            }
        }
    }


}