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
import com.example.notification_trial02.databinding.FragmentDonePatientListBinding
import com.example.notification_trial02.databinding.FragmentShowPatientFormBinding
import com.example.notification_trial02.databinding.FragmentShowPatientPrescriptionBinding
import com.example.notification_trial02.modals.PatientPreForm
import com.example.notification_trial02.modals.Period
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

        viewModel.patientPreFormResponse.observe(viewLifecycleOwner){ response ->
            when(response){
                is NetworkResponse.Loading -> {
                    binding.progressbar.visibility = View.VISIBLE
                    binding.formView.visibility = View.GONE
                }
                is NetworkResponse.Success -> {
                    binding.progressbar.visibility = View.GONE
                    binding.formView.visibility = View.VISIBLE
                }
                else -> {
                    binding.errMsg.visibility = View.VISIBLE
                    binding.errorImg.visibility = View.VISIBLE
                }
            }
        }
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
            binding.flowRateRecommended.text = patientForm!!.flowRateRecommended
            binding.targetOxygenRec.text = patientForm!!.targetOxygenRec
            binding.oxygenStartedDate.text = patientForm!!.oxygenDate
            binding.oxygenStartedTime.text = patientForm!!.oxygenTime
            binding.targetAchiveTime.text = patientForm!!.TargetAchieveTime
            binding.mews.text = patientForm!!.mews
            binding.weaningDate.text = patientForm!!.weaningDate
            binding.weaningTime.text = patientForm!!.weaningTime
            binding.oxygenOffDate.text = patientForm!!.oxygenOffDate
            binding.oxygenOffTime.text = patientForm!!.oxygenOffTime
            binding.complication.text = patientForm!!.complication

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

                if(patientForm!!.monitoring == Period.Min15)
                    monitoring.text = "15 Min"
                else if(patientForm!!.monitoring == Period.Min30)
                    monitoring.text = "30 Min"
                else if(patientForm!!.monitoring == Period.HOURLY)
                    monitoring.text = "Hourly"
                else if(patientForm!!.monitoring == Period.HOURLY2)
                    monitoring.text = "2 Hourly"
                else if (patientForm!!.monitoring == Period.OTHER)
                    monitoring.text = patientForm!!.otherPeriod

                if(patientForm!!.mewsRec)
                    mewsYesNo.text = "Yes"
                else
                    mewsYesNo.text = "No"
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