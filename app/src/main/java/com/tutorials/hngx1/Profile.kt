package com.tutorials.hngx1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.tutorials.hngx1.databinding.FragmentGithubWebViewBinding
import com.tutorials.hngx1.databinding.FragmentProfileBinding


class Profile : Fragment() {
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val route = R.id.action_profile_to_githubWebView //ProfileDirections.actionProfileToGithubWebView()
        binding.seeMoreBtn.setOnClickListener {
            findNavController().navigate(route)
        }
    }

}