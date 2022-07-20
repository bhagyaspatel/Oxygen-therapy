package com.example.notification_trial02.adminSideFragments.DonePatientRecyclerView

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.notification_trial02.R
import com.example.notification_trial02.Utils.NetworkResponse
import com.example.notification_trial02.ViewModal.PateintViewModel
import com.example.notification_trial02.adminSideFragments.PostTherapyFormFragmentArgs
import com.example.notification_trial02.databinding.FragmentDonePatientListBinding
import com.example.notification_trial02.databinding.FragmentShowPatientPrescriptionBinding
import com.example.notification_trial02.modals.PatientPreForm
import com.example.notification_trial02.modals.Prescription

class ShowPatientPrescription : Fragment() {

    private lateinit var binding : FragmentShowPatientPrescriptionBinding
    private var patientId : String ? = null
    private var patientPrescription : Prescription? = null

    private val TAG = "PrescriptionShow"

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
        binding = FragmentShowPatientPrescriptionBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        patientId = ShowPatientPrescriptionArgs.fromBundle(requireArguments()).id

        viewModel.patientPrescriptionResponse.observe(viewLifecycleOwner){ response ->
            when(response){
                is NetworkResponse.Loading -> {
                    binding.progressbar.visibility = View.VISIBLE
                    binding.prescriptionVeiw.visibility = View.GONE
                }
                is NetworkResponse.Success -> {
                    binding.progressbar.visibility = View.GONE
                    binding.prescriptionVeiw.visibility = View.VISIBLE
                }
                else -> {
                    binding.errMsg.visibility = View.VISIBLE
                    binding.errorImg.visibility = View.VISIBLE
                }
            }
        }

        viewModel.getPatientPrescription(patientId.toString())
        viewModel.patientPrescription.observe(viewLifecycleOwner){
            Log.d(TAG, "onViewCreated: $it")
            patientPrescription = it
            setView()
        }

        binding.showFormBtn.setOnClickListener {
            findNavController().navigate(
                ShowPatientPrescriptionDirections.actionShowPatientPrescriptionToShowPatientForm(patientId.toString())
            )
        }
    }

    private fun setView() {
        if (patientPrescription != null){
            Log.d(TAG, "setView: rsn is ${patientPrescription!!.rsn.toString()}")
            binding.etRsn.text = patientPrescription!!.rsn
            binding.etFlowRate.text = patientPrescription!!.flowRate
            binding.etHumidification.text = patientPrescription!!.humidification
            binding.etWeaningInst.text = patientPrescription!!.weaning
            binding.doctorName.text = patientPrescription!!.doctor
            binding.designation.text = patientPrescription!!.designation
            binding.kmc.text = patientPrescription!!.kmc

            //TODO : Check whether the correct value is coming or not for the saturation and delivery system
            if (patientPrescription!!.saturation == false)
                binding.etSaturation.text = "88-92%"
            else
                binding.etSaturation.text = "94-98%"


            if (! patientPrescription!!.system.isNullOrEmpty())
                binding.etDeliverySystem.text = patientPrescription!!.system
            else
                binding.etDeliverySystem.text = "No Delivery System Used"
        }
        else{
            Log.d(TAG, "setView: preform is null")
        }
    }

}