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
import com.example.notification_trial02.ViewModal.PateintViewModel
import com.example.notification_trial02.databinding.FragmentPretherapyFormDetailsBinding
import com.example.notification_trial02.modals.PatientAndHospital
import com.example.notification_trial02.modals.PatientPreForm
import com.example.notification_trial02.modals.Period
import com.google.firebase.database.FirebaseDatabase

class PretherapyFormDetails : Fragment() {

    private lateinit var binding: FragmentPretherapyFormDetailsBinding
    private var patientId: String? = null
    private val TAG = "Form"
//    private val dbRef = FirebaseDatabase.getInstance()

    var patient: PatientAndHospital? = null
    private lateinit var editTexts: ArrayList<String>

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
        binding = FragmentPretherapyFormDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        patientId = PretherapyFormDetailsArgs.fromBundle(requireArguments()).id

        binding.deliveryYes.setOnClickListener {
            Log.d(TAG, "delivery checked yes")
            binding.deliverySystem.visibility = View.VISIBLE
        }
        binding.deliveryNo.setOnClickListener {
            Log.d(TAG, "delivery checked no")
            binding.deliverySystem.visibility = View.GONE
        }

        binding.others.setOnClickListener {
            Log.d(TAG, "other comorbidities clicked")
            if (binding.others.isChecked)
                binding.otherComorbidities.visibility = View.VISIBLE
            else
                binding.otherComorbidities.visibility = View.GONE
        }

        binding.savePatientBtn.setOnClickListener {
            Log.d(TAG, "onViewCreated: save btn clicked -> current patient is " + patient)


            viewModel.getPatientFromPendingList(patientId.toString())
            viewModel.patient.observe(viewLifecycleOwner) {
                patient = it
                Log.d(TAG, "Live Patient in observer is $patient")
                if (checkFields()) {
                    extractData()
                }
                else {
                    Toast.makeText(
                        requireContext(),
                        "Please fill all the fields first",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun extractData() {
        val rsn = binding.rsn.text.toString()

        var prescreption = true
        if (!binding.presYes.isChecked)
            prescreption = false


        val comorbidities = arrayListOf<String>()
        val ids = arrayOf(
            binding.dm,
            binding.htn,
            binding.copd,
            binding.asthama,
            binding.ihd,
            binding.others
        )

        ids.forEach { item ->
            if (item.isChecked == true) {
                comorbidities.add(item.text.toString())
                Log.d(TAG, item.text.toString())
            }
        }

        var risk = true
        if (!binding.riskYes.isChecked)
            risk = false

        var saturation = true
        if (!binding.saturationHigh.isChecked)
            saturation = false

        val hr = binding.hr.text.toString()
        val bp = binding.bp.text.toString()
        val spo2 = binding.spo2.text.toString()
        val rr = binding.rr.text.toString()
        val ph = binding.ph.text.toString()
        val pco2 = binding.pco2.text.toString()
        val po2 = binding.po2.text.toString()
        val hco3 = binding.hco3.text.toString()

        var lactate : String? = null
        if (!binding.lactate.text.isNullOrBlank())
            lactate = binding.lactate.text.toString()

        var delivery = true
        if (!binding.deliveryYes.isChecked)
            delivery = false

        var system: String? = null
        if (delivery) {
            system = binding.etDeliverySystem.text.toString()
        }

        val flowRateRecommended = binding.flowRateRecommended.text.toString()
        val flowRateGiven = binding.flowRateGiven.text.toString()

        var monitor : Period = Period.Min15
        if (binding.min30.isChecked)
            monitor = Period.Min30
        else if (binding.hourly.isChecked)
            monitor = Period.HOURLY
        else if (binding.hourly2.isChecked)
            monitor = Period.HOURLY2
        else if (binding.otherPeriod.isChecked)
            monitor = Period.OTHER

        val otherPeriod = binding.otherPeriod.text.toString()

        val targetOxygen = binding.targetOxygenRec.text.toString()
        val oxygenTime = binding.oxygenTime.text.toString()
        val oxygenDate = binding.oxygenDate.text.toString()

        val etTargetAchieveTime = binding.etTargetAchieveTime.text.toString()
        val mews = binding.etMews.text.toString()

        var mewsRec = true
        if (!binding.mewsYes.isChecked)
            mewsRec = false

        val oxygenOffTime = binding.oxygenOffTime.text.toString()
        val oxygenOffDate = binding.oxygenOffDate.text.toString()
        val weaningTime = binding.weaningTime.text.toString()
        val weaningDate = binding.weaningDate.text.toString()
        val complicatin = binding.etComplication.text.toString()

        val preForm = PatientPreForm(
            rsn,
            prescreption,
            comorbidities,
            risk,
            saturation,
            hr, bp, spo2, rr, ph, pco2, po2, hco3, lactate,
            system,
            flowRateRecommended, flowRateGiven,
            monitor, otherPeriod,
            targetOxygen, oxygenTime, oxygenDate,
            etTargetAchieveTime,
            mews, mewsRec,
            oxygenOffTime, oxygenOffDate,
            weaningTime, weaningDate,
            complicatin
        )
        savePatientPreForm(preForm)
    }

    private fun savePatientPreForm(preForm: PatientPreForm) {
        //TODO : save this patient preform in the done list with collection "preform"
        viewModel.savePatientPreform(patientId.toString(), preForm)
        removeAndSave()

        findNavController().navigate(
            PretherapyFormDetailsDirections.actionPretherapyFormDetailsToAdminHomeFragment()
        )
    }

    private fun removeAndSave() {
        Log.d(TAG, "remove and save function called")

        if (patient == null) {
            Log.d(TAG, "patient is null from pending list for storing into done list")
        } else {
            viewModel.savePatientInDoneList(patientId.toString(), patient!!)
            viewModel.deletePatientFromPendingList(patientId.toString())
        }
    }

    private fun checkFields(): Boolean { ////####
        editTexts = arrayListOf(
            binding.rsn.text.toString(),
            binding.hr.text.toString(),
            binding.bp.text.toString(),
            binding.spo2.text.toString(),
            binding.rr.text.toString(),
            binding.ph.text.toString(),
            binding.pco2.text.toString(),
            binding.po2.text.toString(),
            binding.hco3.text.toString(),
            binding.flowRateRecommended.text.toString(),
            binding.flowRateGiven.text.toString(),
            binding.targetOxygenRec.text.toString(),
            binding.oxygenTime.text.toString(),
            binding.oxygenDate.text.toString(),
            binding.etTargetAchieveTime.text.toString(),
            binding.etMews.text.toString(),
            binding.weaningTime.text.toString(),
            binding.weaningDate.text.toString(),
            binding.oxygenOffDate.text.toString(),
            binding.oxygenOffTime.text.toString(),
            binding.etComplication.text.toString(),
        )

        var flag = true

        editTexts.forEach {
            Log.d(TAG, it)
            if (it.isBlank()) {
                Log.d(TAG, it)
                flag = false
            }
            Log.d(TAG, "outside for each")
        }

        if (binding.otherPeriod.isChecked && binding.etMonitorOther.text.isNullOrBlank() )
            flag= false

        if (binding.deliveryYes.isChecked && binding.etDeliverySystem.text.isNullOrBlank())
            flag = false

        Log.d(TAG, "after cheking fields flag is $flag")
        return flag
    }
}