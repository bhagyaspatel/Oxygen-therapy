//package com.example.notification_trial02.AdminSideActivity
//
//import android.os.Bundle
//import android.view.Menu
//import com.google.android.material.snackbar.Snackbar
//import com.google.android.material.navigation.NavigationView
//import androidx.navigation.findNavController
//import androidx.navigation.ui.AppBarConfiguration
//import androidx.navigation.ui.navigateUp
//import androidx.navigation.ui.setupActionBarWithNavController
//import androidx.navigation.ui.setupWithNavController
//import androidx.drawerlayout.widget.DrawerLayout
//import androidx.appcompat.app.AppCompatActivity
//import androidx.navigation.fragment.findNavController
//import com.example.notification_trial02.R
//import com.example.notification_trial02.adminSideFragments.AdminHomeFragmentDirections
//import com.example.notification_trial02.databinding.ActivityAdminNavigationBinding
//
//class AdminNavigationActivity : AppCompatActivity() {
//
//    private lateinit var appBarConfiguration: AppBarConfiguration
//    private lateinit var binding: ActivityAdminNavigationBinding
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        binding = ActivityAdminNavigationBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        setSupportActionBar(binding.appBarAdminNavigation.toolbar)
//
//        val drawerLayout: DrawerLayout = binding.drawerLayout
//        val navView: NavigationView = binding.navView
//        val navController = findNavController(R.id.nav_host_fragment_content_admin_navigation)
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.nav_home, R.id.nav_about, R.id.nav_contact
//            ), drawerLayout
//        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        navView.setupWithNavController(navController)
//
////        navView.setNavigationItemSelectedListener {
////            drawerLayout.closeDrawers()
////
////            when(it.itemId){
////                R.id.about -> {
////                    findNavController().navigate(
////                        AdminHomeFragmentDirections.actionAdminHomeFragmentToAboutFragment())
////                    true
////                }
////                }
////                R.id.contact -> {
////                    findNavController().navigate(EmployeeListFragmentDirections.actionEmployeeListFragmentToContactFragment())
////                    true
////                }
////
////                R.id.signoutBtn -> {
////            findNavController().navigate(
////                    EmployeeListFragmentDirections.actionEmployeeListFragmentToAboutFragment2())
////                    true
////                }
////                else
////                    return false
////        }
//    }
//
////    override fun onCreateOptionsMenu(menu: Menu): Boolean {
////        // Inflate the menu; this adds items to the action bar if it is present.
////        menuInflater.inflate(R.menu.admin_navigation, menu)
////        return true
////    }
//
//    override fun onSupportNavigateUp(): Boolean {
//        val navController = findNavController(R.id.nav_host_fragment_content_admin_navigation)
//        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
//    }
//}