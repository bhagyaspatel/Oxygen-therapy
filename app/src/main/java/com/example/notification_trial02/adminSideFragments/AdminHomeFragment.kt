package com.example.notification_trial02.adminSideFragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.notification_trial02.R
import com.example.notification_trial02.authentication.SignupActivity
import com.example.notification_trial02.databinding.FragmentAdminHomeBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class AdminHomeFragment : Fragment() {

    private lateinit var binding : FragmentAdminHomeBinding
    private val TAG = "AdminHomeFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAdminHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupNavigationDrawer()

        binding.showListBtn.setOnClickListener {
            findNavController().navigate(
                AdminHomeFragmentDirections.actionAdminHomeFragmentToPendingPatientList()
            )
        }
        binding.showDonePatientBtn.setOnClickListener {
            findNavController().navigate(
                AdminHomeFragmentDirections.actionAdminHomeFragmentToDonePatientListFragment()
            )
        }
    }

    private fun setupNavigationDrawer() {
        val navController = Navigation.findNavController(requireActivity(), R.id.fragmentContainerView)
        val drawerLayout = requireActivity().findViewById<DrawerLayout>(R.id.drawerLayout) //from activity_xml
        val navigationView = requireActivity().findViewById<NavigationView>(R.id.navigation_view)

//        NavigationUI.setupWithNavController(toolbar,navController, drawerLayout)
        //we dont want back button in the toolbar of ListFrag bcz once loggd in we dont want to go back to that page so..

        NavigationUI.setupWithNavController(binding.toolbar,navController,
            AppBarConfiguration.Builder(R.id.admin_navigation, R.id.adminHomeFragment)
                .setDrawerLayout(drawerLayout)
                .build())

        navigationView.setupWithNavController(navController)

        navigationView.setNavigationItemSelectedListener {
            drawerLayout.closeDrawers()

            when(it.itemId){
                R.id.about -> { findNavController().navigate(
                    AdminHomeFragmentDirections.actionAdminHomeFragmentToAboutFragment())
                    true
                }
                R.id.contact -> {findNavController().navigate(
                    AdminHomeFragmentDirections.actionAdminHomeFragmentToContactFragment())
                    true
                }
                R.id.signoutBtn ->{
                    val auth = FirebaseAuth.getInstance()
                    auth.signOut()
                    auth.addAuthStateListener {
                        if (auth.currentUser == null){ //i.e. user have signed out
                            //this listener is called multiple times so check if we are in right fragment or not
                            Log.d(TAG, "setupNavigationDrawer: user signed out")
                            val currFragID = findNavController().currentDestination!!.id
                            if (currFragID == R.id.adminHomeFragment){
                                val intent = Intent(requireContext(), SignupActivity::class.java)
                                startActivity(intent)
                            }
                        }
                    }
                    true
                }
                else -> false
            }
        }
    }
}