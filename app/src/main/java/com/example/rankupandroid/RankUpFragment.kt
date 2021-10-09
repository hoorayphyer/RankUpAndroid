package com.example.rankupandroid

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import com.example.rankupandroid.databinding.FragmentRankUpBinding

class RankUpFragment : Fragment() {
    private lateinit var binding: FragmentRankUpBinding
    private val sharedModel: SharedViewModelSelectedPlayers by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRankUpBinding.inflate(layoutInflater, container, false)

        binding.apply {
            sharedModel.myself.observe(viewLifecycleOwner, {
                myself.updateView(it, false)
            })

            sharedModel.teammate.observe(viewLifecycleOwner, {
                teammate.updateView(it, false)
            })

            sharedModel.opponent1.observe(viewLifecycleOwner, {
                opponent1.updateViewVertical(it)
            })

            sharedModel.opponent2.observe(viewLifecycleOwner, {
                opponent2.updateViewVertical(it)
            })
        }

        return binding.root
    }

    // toggle actionbar
    @SuppressLint("RestrictedApi") // due to setShowHideAnimationEnabled(false) below
    override fun onResume() {
        super.onResume()
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            setShowHideAnimationEnabled(false)
            hide()
        }
    }

    override fun onStop() {
        super.onStop()
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
    }
}