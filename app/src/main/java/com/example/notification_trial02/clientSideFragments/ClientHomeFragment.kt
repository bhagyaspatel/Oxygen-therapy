package com.example.notification_trial02.clientSideFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.notification_trial02.R
import com.example.notification_trial02.databinding.FragmentClientHomeBinding

class ClientHomeFragment : Fragment() {

    private lateinit var binding : FragmentClientHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentClientHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cardBtn.setOnClickListener {
            findNavController().navigate(
                ClientHomeFragmentDirections.actionClientHomeFragmentToSendNotification()
            )
        }
    }


}