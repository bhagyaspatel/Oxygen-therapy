package com.example.notification_trial02.adminSideFragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.notification_trial02.PRESCRIPTION
import com.example.notification_trial02.ViewModal.PateintViewModel
import com.example.notification_trial02.databinding.FragmentPostTherapyFormBinding
import com.example.notification_trial02.modals.PatientPreForm
import com.example.notification_trial02.modals.Prescription
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch


class PostTherapyFormFragment : Fragment() {

    private lateinit var binding : FragmentPostTherapyFormBinding
    private var patientId : String ? = null
    private var patientForm : PatientPreForm? = null

    private val TAG = "PostTherapyFragment"

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
        binding = FragmentPostTherapyFormBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        patientId = PostTherapyFormFragmentArgs.fromBundle(requireArguments()).id

//        viewModel.getPatientPreForm(patientId.toString())
//        viewModel.patientForm.observe(viewLifecycleOwner){
//            Log.d(TAG, "onViewCreated: $it")
//            patientForm = it
//            setView()
//        }

        binding.savePrescriptionBtn.setOnClickListener {
            if (checkInputs()){
                val rsn = binding.etRsn.text.toString()
                val system = binding.etDeliverySystem.text.toString()
                val flow = binding.etFlowRate.text.toString()
                val humidification = binding.etHumidification.text.toString()

                var saturation = true
                if (!binding.saturationHigh.isChecked) //true for high, false for low
                    saturation = false

                val weaning = binding.etWeaningInst.text.toString()
                val doctorName = binding.doctorName.text.toString()
                val designation = binding.designation.text.toString()
                val kmc = binding.kmc.text.toString()

                val prescription = Prescription (rsn , system, flow, humidification,saturation,weaning,doctorName,designation,kmc)

                //TODO : DONE ; save this prescription in DONE list with collection "prescription"
                 viewModel.setPatientPrescription(prescription, patientId.toString())

                findNavController().navigate(
                    PostTherapyFormFragmentDirections.actionPostTherapyFormFragmentToPretherapyFormDetails(patientId!!)
                )
            }
            else{
                Toast.makeText(requireContext(), "Please fill the require details first", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun checkInputs(): Boolean {
        var flag = true

        if (binding.etFlowRate.text.isNullOrEmpty() || binding.etHumidification.text.isNullOrEmpty() ||
            binding.doctorName.text.isNullOrEmpty() || binding.kmc.text.isNullOrEmpty() ||
            binding.designation.text.isNullOrEmpty() || binding.etRsn.text.isNullOrEmpty() ||
            binding.etDeliverySystem.text.isNullOrEmpty() || binding.etWeaningInst.text.isNullOrEmpty())
                flag = false

        return flag
    }

//    private fun setView() {
//        if (patientForm != null){
//            Log.d(TAG, "setView: rsn is ${patientForm!!.rsn.toString()}")
//            binding.etRsn.text = patientForm!!.rsn.text
//
//            val rsn : String = patientForm!!.rsn.toString()
//
//            binding.etRsn.setText(rsn)
//
//            if ( patientForm!!.saturation )
//                binding.etTargetSaturation.text = "88-92%"
//            else
//                binding.etTargetSaturation.text = "94-98%"
//
//            if (! patientForm!!.system.isNullOrEmpty())
//                binding.etDeliverySystem.text = patientForm!!.system
//            else
//                binding.etDeliverySystem.text = "No Delivery System Used"
//        }
//        else{
//            Log.d(TAG, "setView: preform is null")
//        }
//    }


}