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