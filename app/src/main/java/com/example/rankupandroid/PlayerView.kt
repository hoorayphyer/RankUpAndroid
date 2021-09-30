package com.example.rankupandroid

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.rankupandroid.databinding.PlayerViewBinding

class PlayerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : ConstraintLayout(context, attrs) {

    private var binding: PlayerViewBinding =
        PlayerViewBinding.bind(View.inflate(context, R.layout.player_view, this))

//    init {
    //        init(attrs)
//    }

//    private fun init(attrs: AttributeSet?) {
//
//        val ta = context.obtainStyledAttributes(attrs, R.styleable.PlayerView)
//        try {
//            val playerName = ta.getString(R.styleable.PlayerView_player_name)
//            val playerSpecified = ta.getBoolean(R.styleable.PlayerView_player_specified, false)
//            var playerImageId: Int? = ta.getResourceId(R.styleable.PlayerView_player_image, 0)
//            if (playerImageId == 0) {
//                playerImageId = null
//            }
//            setUpPlayerView(playerName, playerImageId, playerSpecified)
//        } finally {
//            ta.recycle()
//        }
//    }

    private fun setVisibility(specified: Boolean) {
        binding.apply {
            if (specified) {
                addPlayerButton.visibility = View.INVISIBLE
                playerName.visibility = View.VISIBLE
                playerImage.visibility = View.VISIBLE
            } else {
                addPlayerButton.visibility = View.VISIBLE
                playerName.visibility = View.INVISIBLE
                playerImage.visibility = View.INVISIBLE
            }
        }
    }

    fun updateView(
        player: Player?
    ) {
        binding.apply {
            setVisibility(player != null)
            // initialize clickListener to null. Allow this to be later specified
            addPlayerButton.setOnClickListener(null)

            if (player != null) {
                playerName.text = player.name
                glideLoad(context, player)
                    .into(binding.playerImage)
            }
        }
    }

    fun setAddButtonAction(f: OnClickListener?) {
        binding.addPlayerButton.setOnClickListener(f)
    }
}