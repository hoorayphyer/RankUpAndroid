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
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.rankupandroid.R
import com.example.rankupandroid.SharedViewModelSelectedPlayers
import com.example.rankupandroid.databinding.FragmentRankUpBinding

class RankUpFragment : Fragment() {
    private lateinit var binding: FragmentRankUpBinding
    private lateinit var motionLayout: MotionLayout
    private val viewModel: RankUpViewModel by viewModels()
    private val sharedModel: SharedViewModelSelectedPlayers by activityViewModels()

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
            val params = view.layoutParams as ConstraintLayout.LayoutParams
            val isUnselected = (params.topToTop == -1)
            if (isUnselected) {
                params.topToTop = (view.parent as View).id
                params.bottomToBottom = -1
                viewModel.selectCardInHand(card.value)
            } else {
                params.topToTop = -1
                params.bottomToBottom = (view.parent as View).id
                viewModel.deselectCardInHand(card.value)
            }

            // see below for requestLayout(), which is necessary to make the view update
            // https://stackoverflow.com/questions/13856180/usage-of-forcelayout-requestlayout-and-invalidate
            view.requestLayout()
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

    private fun setActionButton(gamePhase: GamePhase) {
        binding.rankupActionButton.apply {
            when (gamePhase) {
                GamePhase.END -> {
                    visibility = View.VISIBLE
                    text = getString(R.string.rankup_action_button_deal_str)
                    setOnClickListener {
                        // TODO neither of following two lines is working to have the button
                        //  disappear
                        viewModel.toNextPhase()
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
                        playCards()
                    }
                }
            }
        }

    }

    private fun setPlayedCardImage(view: ImageView, cardInt: Int) {
        view.apply {
            setImageResource(
                resources.getIdentifier(
                    CardImages[cardInt],
                    "drawable", context.packageName
                )
            )
            setBackgroundResource(R.drawable.card_border)
        }
    }

    private fun resetPlayedCardImage(view: ImageView) {
        view.apply {
            setImageResource(
                resources.getIdentifier(
                    "cards_blank",
                    "drawable", context.packageName
                )
            )
            setBackgroundResource(android.R.color.transparent)
        }
    }

    private fun resetAllPlayedCardImage() {
        for (i in 0..3) {
            resetPlayedCardImage(playedCardImage[i])
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

    private fun playCards() {
        val playerStart = 0
        var clearedPlayedCards = false
        for (i in 0..3) {
            val player = (playerStart + i) % 4
            val (errorMsg, cardPlayed) = viewModel.playCards(player)
            if (errorMsg == null) {
                // only clear played cards when new cards are to be played
                if (!clearedPlayedCards) {
                    resetAllPlayedCardImage()
                    clearedPlayedCards = true
                }

                setPlayedCardImage(playedCardImage[player], cardPlayed)
                if (player == 0) {
                    // force redraw
                    binding.cardsRecyclerView.adapter!!.notifyDataSetChanged()
                }
            } else {
                Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
                // this return exits the entire function when the first player plays something
                // illegal. Note this only works for now when the first player is the user
                return
            }
            // TODO set delay between different players
        }

        viewModel.advanceRound()
        if (viewModel.isGameFinished()) {
            Toast.makeText(
                context, "Game finished! Click DEAL to start another one", Toast
                    .LENGTH_LONG
            ).show()
            viewModel.toNextPhase()
        }
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
                viewModel.toNextPhase()
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