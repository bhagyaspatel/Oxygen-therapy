package com.example.notification_trial02.ViewModal

import android.app.Application
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.notification_trial02.*
import com.example.notification_trial02.ClientSideActivities.HomeActivity
import com.example.notification_trial02.Utils.NetworkResponse
import com.example.notification_trial02.modals.PatientAndHospital
import com.example.notification_trial02.modals.PatientPreForm
import com.example.notification_trial02.modals.Prescription
import com.example.notification_trial02.modals.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlin.math.log

class PateintViewModel(applicaation: Application) : AndroidViewModel(applicaation) {

    val TAG = "ViewModel"
    val db = FirebaseFirestore.getInstance()

    val patientList = MutableLiveData<List<PatientAndHospital>>()
    private val _patientList = ArrayList<PatientAndHospital>()

    val patient = MutableLiveData<PatientAndHospital>()
    private var _patient = PatientAndHospital()

    var patientForm = MutableLiveData<PatientPreForm>()

    var patientPrescription = MutableLiveData<Prescription>()

    var flag = MutableLiveData<Boolean>()

    var patientListResponse: MutableLiveData<NetworkResponse<PatientAndHospital>> = MutableLiveData()
    var patientPrescriptionResponse: MutableLiveData<NetworkResponse<Prescription>> = MutableLiveData()
    var patientPreFormResponse: MutableLiveData<NetworkResponse<PatientPreForm>> = MutableLiveData()

    fun getPatientPreForm(patientId: String) {
        patientPreFormResponse.value = NetworkResponse.Loading()

        viewModelScope.launch {
            db.collection(DONE).document(patientId)
                .collection(FORM).document(patientId)
                .get()
                .addOnSuccessListener { document ->
                    val form = document.toObject(PatientPreForm::class.java)
                    patientForm.postValue(form)
                    patientPreFormResponse.value = NetworkResponse.Success()
                    Log.d(TAG, "saved patient form is ${form.toString()}")
                    Log.d(TAG, "saved patient live form is ${patientForm}")
                }
                .addOnFailureListener { exception ->
                    patientPreFormResponse.value = NetworkResponse.Failure(exception.toString())
                    Log.d(TAG, "Error getting documents: ", exception)
                }
        }
    }

    //TODO : made new function for getting prescription
    fun getPatientPrescription(patientId: String) {
        patientPrescriptionResponse.value = NetworkResponse.Loading()

        viewModelScope.launch {
            db.collection(DONE).document(patientId)
                .collection(PRESCRIPTION).document(patientId)
                .get()
                .addOnSuccessListener { document ->
                    val prescription = document.toObject(Prescription::class.java)
                    patientPrescription.postValue(prescription)
                    patientPrescriptionResponse.value = NetworkResponse.Success()
                    Log.d(TAG, "saved patient form is ${prescription.toString()}")
                    Log.d(TAG, "saved patient live form is ${patientForm}")
                }
                .addOnFailureListener { exception ->
                    patientPrescriptionResponse.value = NetworkResponse.Failure(exception.toString())
                    Log.d(TAG, "Error getting documents: ", exception)
                }
        }
    }

    //TODO : have changed the path
    fun setPatientPrescription (prescription: Prescription, patientId: String){
        viewModelScope.launch {
            db.collection(DONE).document(patientId)
                .collection(PRESCRIPTION).document(patientId)
                .set (prescription)
                .addOnSuccessListener {
                    Toast.makeText(getApplication() , "Prescription saved", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(getApplication(), "Some error occurred on saving the prescription", Toast.LENGTH_SHORT).show()
                }
        }
    }

    fun getAllPendingPateint(){
        patientListResponse.value = NetworkResponse.Loading()

        viewModelScope.launch {
            db.collection(PENDING)
                .get()
                .addOnSuccessListener { documents ->
                    _patientList.clear()
                    for (document in documents) {
                        val patient: PatientAndHospital = document.toObject(PatientAndHospital::class.java)
                        Log.d(TAG, "getAllPendingPateint: " + patient)
                        _patientList.add(patient)
                    }
//                    setValue() method must be called from the main thread. But if you need set a
//                    value from a background thread, postValue() should be used.
                    Log.d(TAG, "getAllPendingPateint: after all the final list from db is $_patientList"
                    )
//                    patientList.value = listOf()
//                    Log.d(TAG, "getAllPendingPateint: after making empty ${patientList.value}")

                    patientList.postValue(_patientList)
                    patientListResponse.value = NetworkResponse.Success()
                    Log.d(TAG, "view Modal : after adding ${patientList.value}")
                }
                .addOnFailureListener { exception ->
                    patientListResponse.value = NetworkResponse.Failure(exception.toString())
                    Log.d(TAG, "Error getting documents: ", exception)
                }
        }
    }

    //TODO : chack the output
    fun getAllDonePatient() {
        patientListResponse.value = NetworkResponse.Loading()

        Log.d(TAG, "getAllDonePatient: getting all the done patients")
        viewModelScope.launch {
            db.collection(DONE)
                .get()
                .addOnSuccessListener { documents ->
                    _patientList.clear()
                    for (document in documents) {
                        Log.d(TAG, "Error getting documents: $document")
                        val patient: PatientAndHospital = document.toObject(PatientAndHospital::class.java)
                        _patientList.add(patient)
                    }
                    patientList.postValue(_patientList)
                    patientListResponse.value = NetworkResponse.Success()
                }
                .addOnFailureListener { exception ->
                    patientListResponse.value = NetworkResponse.Failure(exception.toString())
                    Log.d(TAG, "Error getting documents: ", exception)
                }
        }
    }

    //TODO : change this; delete from all collections
    fun deleltePatientFromDoneList(id : String){
        viewModelScope.launch {
            db.collection(DONE).document(id).delete()
                .addOnSuccessListener {
                    Toast.makeText(getApplication(), "Patient Details Removed", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener{
                    Log.d(TAG, it.message!!)
                    Log.d(TAG, it.message!!)
                }
        }
    }

    fun savePatientInDoneList (patientId : String, patient : PatientAndHospital) {
        Log.d(TAG, "savePatientInDoneList: called")
        viewModelScope.launch {
            db.collection(DONE).document(patientId)
                .set(patient)
                .addOnSuccessListener {
                    Toast.makeText(getApplication(), "Patient details saved in done list", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener{
                    Log.d(TAG, it.message!!)
                }
        }
    }

    fun getPatientFromPendingList(patientId : String) {
        viewModelScope.launch {
            db.collection(PENDING).document(patientId)
                .get()
                .addOnSuccessListener {
                    Log.d(TAG, "getPatientFromPendingList: patient in db is " + it)
                    if(it != null){
                        Log.d(TAG, "getPatientFromPendingList: " + it.toObject(PatientAndHospital::class.java))
                        _patient  = it.toObject(PatientAndHospital::class.java)!!
                        Log.d(TAG, "Live Patient is $_patient")
                        patient.postValue(_patient)
                        //TODO this is not null but the observer of this is getting null value
                        Log.d(TAG, "Live Patient 2 is $patient")
                    }
                    Log.d(TAG, "Patient is fetched from pending list")
                }
                .addOnFailureListener {
                    Log.d(TAG, "Patient's pre-form not saved")
                    Toast.makeText(getApplication(), "Some error occured in saving the details", Toast.LENGTH_SHORT).show()
                }
        }
    }

    //TODO : DONE ; have changed the path
    fun savePatientPreform(patientId : String, preForm: PatientPreForm) {
        viewModelScope.launch {
            db.collection(DONE).document(patientId)
                .collection(FORM).document(patientId)
                .set(preForm)
                .addOnSuccessListener {
                    Log.d(TAG, "Patient's pre-form saved successfully")
                    Toast.makeText(getApplication(), "Detail saved successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Log.d(TAG, "Patient's pre-form not saved")
                    Toast.makeText(getApplication(), "Some error occured in saving the details", Toast.LENGTH_SHORT).show()
                }
        }
    }

    fun deletePatientFromPendingList (id : String){
        viewModelScope.launch {
            db.collection(PENDING).document(id).delete()
                .addOnSuccessListener {
                    Toast.makeText(getApplication(), "Patient Details Removed", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener{
                    Log.d(TAG, it.message!!)
                    Log.d(TAG, it.message!!)
                }
        }
    }

    fun saveUser (newUser : User, id : String){
        viewModelScope.launch {
            db.collection(USER).document(id)
                .set(newUser)
                .addOnSuccessListener {
                    Toast.makeText(getApplication(), "Details saved successfully", Toast.LENGTH_SHORT).show()
                    flag.postValue(true)
                }
                .addOnFailureListener{
                    flag.postValue(false)
                    Toast.makeText(getApplication(), "Error occurred while saving the details", Toast.LENGTH_SHORT).show()
                }
        }
    }

}