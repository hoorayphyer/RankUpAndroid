package com.example.rankupandroid

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.rankupandroid.databinding.FragmentHostGameBinding

class HostGameFragment : Fragment() {

    private lateinit var binding: FragmentHostGameBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHostGameBinding.inflate(layoutInflater, container, false)

        binding.hostStartGameButton.setOnClickListener {
            findNavController().navigate(R.id.action_hostGameFragment_to_rankUpFragment)
        }

        // TODO automatically add you as an existing player
        // TODO consider using data binding for each player view, which may require implementing the Player class first
        return binding.root
    }
}