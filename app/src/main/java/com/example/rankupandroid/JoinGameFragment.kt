package com.example.rankupandroid

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.rankupandroid.databinding.FragmentJoinGameBinding

class JoinGameFragment : Fragment() {
    private lateinit var binding: FragmentJoinGameBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentJoinGameBinding.inflate(layoutInflater, container, false)

        binding.joinStartGameButton.setOnClickListener {
            findNavController().navigate(R.id.action_joinGameFragment_to_rankUpFragment)
        }

        return binding.root
    }
}