package com.example.rankupandroid.rankup

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.rankupandroid.R
import com.example.rankupandroid.SharedViewModelSelectedPlayers
import com.example.rankupandroid.databinding.FragmentRankUpBinding

class RankUpFragment : Fragment() {
    private lateinit var binding: FragmentRankUpBinding
    private lateinit var motionLayout : MotionLayout
    private val viewModel: RankUpViewModel by viewModels()
    private val sharedModel: SharedViewModelSelectedPlayers by activityViewModels()

    private val dealingDirection = arrayOf(R.id.dealtToBottom, R.id.dealtToRight, R.id.dealtToTop, R.id.dealtToLeft)
    private var dealingIter : Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRankUpBinding.inflate(layoutInflater, container, false)

        motionLayout = binding.root

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


        viewModel.gamePhase.observe(viewLifecycleOwner, {
            setActionButton(it)
        })

        // setState sets the initial state of the motionLayout. Otherwise the first
        // constraintSetStart of a <Transition> in the motion_scene.xml will serve the purpose
        motionLayout.setState(R.id.atCenter, motionLayout.width, motionLayout.height)
        motionLayout.addTransitionListener(CardDealingTransitionListener())

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

    private fun setActionButton( gamePhase: GamePhase ) {
        binding.rankupActionButton.apply{
            when(gamePhase) {
                GamePhase.END -> {
                    visibility = View.VISIBLE
                    text = getString(R.string.rankup_action_button_deal_str)
                    setOnClickListener {
                        // TODO neither of following two lines is working
                        viewModel.toNextPhase()
                        visibility=View.INVISIBLE
                        dealCards()
                    }
                }
                GamePhase.DEAL -> {
                    visibility = View.INVISIBLE
                    setOnClickListener { }
                }
                GamePhase.PLAY -> {
                    visibility = View.VISIBLE
                    text = getString(R.string.rankup_action_button_play_str)
                    setOnClickListener {
                        // TODO implement this
                    }
                }
            }
        }

    }

    private fun dealCards() {
        viewModel.initializeDeck()
        dealingIter = viewModel.dealingBegin
        val playerInt = dealingIter % 4
        motionLayout.transitionToState(dealingDirection[playerInt], 500)
        viewModel.dealCardTo(playerInt)
    }

    private inner class CardDealingTransitionListener: MotionLayout.TransitionListener {
        override fun onTransitionStarted(motionLayout: MotionLayout?, startId: Int, endId: Int) {
        }

        override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
            // setProgress doesn't invoke transition listener
            motionLayout?.progress = 0.0f
            ++dealingIter
            if (dealingIter < viewModel.dealingEnd) {
                val playerInt = dealingIter % 4
                motionLayout?.transitionToState(dealingDirection[playerInt], 1000)
                viewModel.dealCardTo(playerInt)
            }
        }

        override fun onTransitionChange(
            motionLayout: MotionLayout?,
            startId: Int,
            endId: Int,
            progress: Float
        ) {
        }

        override fun onTransitionTrigger(
            motionLayout: MotionLayout?,
            triggerId: Int,
            positive: Boolean,
            progress: Float
        ) {
        }
    }
}