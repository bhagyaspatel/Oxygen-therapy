package com.example.notification_trial02.generalFragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.notification_trial02.databinding.FragmentContactBinding

class ContactFragment : Fragment() {

    private lateinit var binding : FragmentContactBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentContactBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.linkdinImg.setOnClickListener{
            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            intent.addCategory(Intent.CATEGORY_BROWSABLE)
            intent.data = Uri.parse("https://www.linkedin.com/in/bhagya-patel-9836081b7/")
            startActivity(intent)
        }

        binding.githubImg.setOnClickListener{
            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            intent.addCategory(Intent.CATEGORY_BROWSABLE)
            intent.data = Uri.parse("https://github.com/bhagyaspatel")
            startActivity(intent)
        }

        binding.emailImg.setOnClickListener{
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "plain/text"
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("bhagyaspatel0911@gmail.com"))
            intent.putExtra(Intent.EXTRA_SUBJECT, "subject")
            intent.putExtra(Intent.EXTRA_TEXT, "mail body")
            startActivity(Intent.createChooser(intent, ""))
        }
    }

}