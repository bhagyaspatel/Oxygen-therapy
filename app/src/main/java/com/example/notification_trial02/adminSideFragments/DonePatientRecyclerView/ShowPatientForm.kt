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
import com.example.notification_trial02.ViewModal.PateintViewModel
import com.example.notification_trial02.databinding.FragmentDonePatientListBinding
import com.example.notification_trial02.databinding.FragmentShowPatientFormBinding
import com.example.notification_trial02.databinding.FragmentShowPatientPrescriptionBinding
import com.example.notification_trial02.modals.PatientPreForm
import com.example.notification_trial02.modals.Prescription

class ShowPatientForm : Fragment() {

    private lateinit var binding : FragmentShowPatientFormBinding
    private var patientId : String ? = null
    private var patientForm : PatientPreForm? = null

    private val TAG = "FormShow"

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
        binding = FragmentShowPatientFormBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        patientId = ShowPatientPrescriptionArgs.fromBundle(requireArguments()).id

        viewModel.getPatientPreForm(patientId.toString())
        viewModel.patientForm.observe(viewLifecycleOwner){
            Log.d(TAG, "onViewCreated: $it")
            patientForm = it
            setView()
        }

        binding.closeBtn.setOnClickListener {
            findNavController().navigate(
                ShowPatientFormDirections.actionShowPatientFormToAdminHomeFragment()
            )
        }
    }

    private fun setView() {

        if(patientForm != null){
            binding.hr.text = patientForm!!.hr
            binding.bp.text = patientForm!!.bp
            binding.spo2.text = patientForm!!.spo2
            binding.rr.text = patientForm!!.rr
            binding.ph.text = patientForm!!.ph
            binding.pco2.text = patientForm!!.pco2
            binding.po2.text = patientForm!!.po2
            binding.hco3.text = patientForm!!.hco3

            if(patientForm!!.lactate.isNullOrBlank())
                binding.lactate.text = "not available"
            else
                binding.lactate.text = patientForm!!.lactate

            binding.rsn.text = patientForm!!.rsn.toString()

            with(binding){
                if(patientForm!!.prescription)
                    ifPrescription.text = "Yes"
                else
                    ifPrescription.text = "No"

                if(patientForm!!.risk)
                    isRisk.text = "Yes"
                else
                    isRisk.text = "No"

                if(! patientForm!!.saturation)
                    saturation.text = "88-92%"
                else
                    saturation.text = "94-98%"
            }

            var items = String()
            patientForm!!.comorbidities.forEach{
                Log.d(TAG, "setView: items area : $items")
                items += (it + "\n")
            }
            binding.allComorbidities.text = items

            if(patientForm!!.system.isNullOrBlank())
                binding.isSystem.text = "No system used"
            else
                binding.isSystem.text = patientForm!!.system.toString()
        }
        else{
            Log.d(TAG, "setView: patientForm is null")
        }
    }

}