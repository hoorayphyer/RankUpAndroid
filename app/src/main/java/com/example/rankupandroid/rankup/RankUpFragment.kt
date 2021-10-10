package com.example.rankupandroid.rankup

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.rankupandroid.SharedViewModelSelectedPlayers
import com.example.rankupandroid.databinding.FragmentRankUpBinding

class RankUpFragment : Fragment() {
    private lateinit var binding: FragmentRankUpBinding
    private val viewModel: RankUpViewModel by viewModels()
    private val sharedModel: SharedViewModelSelectedPlayers by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRankUpBinding.inflate(layoutInflater, container, false)

        binding.apply {
            sharedModel.myself.observe(viewLifecycleOwner, {
                myself.updateView(it, deletable = false, actionButtonGone = true)
            })

            sharedModel.teammate.observe(viewLifecycleOwner, {
                teammate.updateView(it, deletable = false, actionButtonGone = true)
            })

            sharedModel.opponent1.observe(viewLifecycleOwner, {
                opponent1.updateViewVertical(it)
            })

            sharedModel.opponent2.observe(viewLifecycleOwner, {
                opponent2.updateViewVertical(it)
            })
        }

        val itemClickListener = CardItemClickListener {
        }

        val cardsAdapter = CardsAdapter(itemClickListener)

        // the following supplies the recycler view with concrete contents
        viewModel.cardsInHand.observe(viewLifecycleOwner, {
            cardsAdapter.submitList(it)
        })

        binding.cardsRecyclerView.adapter = cardsAdapter

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