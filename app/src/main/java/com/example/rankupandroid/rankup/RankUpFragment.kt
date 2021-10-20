package com.example.rankupandroid.rankup

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.rankupandroid.R
import com.example.rankupandroid.SharedViewModelSelectedPlayers
import com.example.rankupandroid.databinding.FragmentRankUpBinding
import com.example.rankupandroid.history.GameHistoryDataRepository
import com.example.rankupandroid.history.GameHistoryDatabase
import com.example.rankupandroid.history.ViewHistoryViewModel
import com.example.rankupandroid.history.ViewHistoryViewModelFactory

class RankUpFragment : Fragment() {
    private lateinit var binding: FragmentRankUpBinding
    private lateinit var motionLayout: MotionLayout
    private val viewModel: RankUpViewModel by viewModels()
    private val sharedModel: SharedViewModelSelectedPlayers by activityViewModels()
    private val historyViewModel: ViewHistoryViewModel by activityViewModels {
        val dao = GameHistoryDatabase.getInstance(requireActivity().application).dao
        val repo = GameHistoryDataRepository(dao)
        ViewHistoryViewModelFactory(repo)
    }

    private val dealingDirection =
        arrayOf(R.id.dealtToBottom, R.id.dealtToRight, R.id.dealtToTop, R.id.dealtToLeft)
    private var dealingIter: Int = 0

    private val dealingCardDurationInMilliseconds: Int = 500

    private lateinit var playedCardImage: Array<ImageView>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRankUpBinding.inflate(layoutInflater, container, false)

        motionLayout = binding.root

        playedCardImage = arrayOf(
            binding.playedCardsMyself, binding
                .playedCardsOpponent1, binding.playedCardsTeammate, binding.playedCardsOpponent2
        )

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

        val itemClickListener = CardItemClickListener { view, card ->
            if (isSelectedMode(view)) {
                setToUnselected(view, true)
                viewModel.deselectCardInHand(card.value)
            } else {
                setToSelected(view, true)
                viewModel.selectCardInHand(card.value)
            }
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

        viewModel.cardsToPlay[0].observe(viewLifecycleOwner, {
            setCardImage(binding.playedCardsMyself, it)
            // force redraw
            binding.cardsRecyclerView.adapter?.notifyDataSetChanged()
        })

        viewModel.cardsToPlay[1].observe(viewLifecycleOwner, {
            setCardImage(binding.playedCardsOpponent1, it)
        })

        viewModel.cardsToPlay[2].observe(viewLifecycleOwner, {
            setCardImage(binding.playedCardsTeammate, it)
        })

        viewModel.cardsToPlay[3].observe(viewLifecycleOwner, {
            setCardImage(binding.playedCardsOpponent2, it)
        })

        viewModel.isGameFinished.observe(viewLifecycleOwner, { isGameFinished ->
            if (isGameFinished) {
                Toast.makeText(
                    context, "Game finished! Click DEAL to start another one", Toast
                        .LENGTH_LONG
                ).show()
                historyViewModel.saveHistory(viewModel.history)
            }
        })

        viewModel.clearPlayedCards.observe(viewLifecycleOwner, { clearPlayedCards ->
            if (clearPlayedCards) {
                resetAllPlayedCardImage()
            }
        })

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

    private fun setActionButton(gamePhase: GamePhase) {
        binding.rankupActionButton.apply {
            when (gamePhase) {
                GamePhase.END -> {
                    visibility = View.VISIBLE
                    text = getString(R.string.rankup_action_button_deal_str)
                    setOnClickListener {
                        // TODO neither of following two lines is working to have the button
                        //  disappear
                        visibility = View.INVISIBLE
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
                        if (viewModel.whoseTurn == 0) {
                            val errorMsg = viewModel.HumanPlay()
                            if (errorMsg != null) {
                                Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun resetAllPlayedCardImage() {
        for (i in 0..3) {
            resetCardImage(playedCardImage[i])
        }
    }

    private fun dealCards() {
        resetAllPlayedCardImage()
        viewModel.initializeDeck()
        dealingIter = viewModel.dealingBegin
        val playerInt = dealingIter % 4
        // look at CardDealingTransitionListener.onTransitionCompleted for recursive calls
        motionLayout.transitionToState(
            dealingDirection[playerInt],
            dealingCardDurationInMilliseconds
        )
    }

    private inner class CardDealingTransitionListener : MotionLayout.TransitionListener {
        override fun onTransitionStarted(motionLayout: MotionLayout?, startId: Int, endId: Int) {
        }

        override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
            // setProgress doesn't invoke transition listener
            motionLayout?.progress = 0.0f
            // put this dealCardTo here so that the card is revealed after the card motion is done
            if (dealingIter < viewModel.dealingEnd) {
                // this apparently redundant 'if' branch is because of a weird bug that the
                // transition motion seems to always gets executed one more time after the
                // supposed end. Without this 'if', one player ends up with one more card
                // than others
                viewModel.dealCardTo(dealingIter % 4)
            }
            ++dealingIter
            if (dealingIter < viewModel.dealingEnd) {
                val playerInt = dealingIter % 4
                motionLayout?.transitionToState(
                    dealingDirection[playerInt],
                    dealingCardDurationInMilliseconds
                )
            } else if (dealingIter == viewModel.dealingEnd) {
                viewModel.startPlayPhase()
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