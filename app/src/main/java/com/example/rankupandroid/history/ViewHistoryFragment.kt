package com.example.rankupandroid.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.rankupandroid.databinding.FragmentViewHistoryBinding

class ViewHistoryFragment : Fragment() {
    private lateinit var binding: FragmentViewHistoryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentViewHistoryBinding.inflate(layoutInflater, container, false)

        return binding.root
    }
}