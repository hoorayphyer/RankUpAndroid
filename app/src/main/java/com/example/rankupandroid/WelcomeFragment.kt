package com.example.rankupandroid

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.rankupandroid.databinding.FragmentWelcomeBinding

class WelcomeFragment : Fragment() {

    private lateinit var binding : FragmentWelcomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWelcomeBinding.inflate(layoutInflater, container, false)
        // Inflate the layout for this fragment

        binding.hostGameButton.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_hostGameFragment)
        }
        binding.joinGameButton.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_joinGameFragment)
        }

        return binding.root
    }
}