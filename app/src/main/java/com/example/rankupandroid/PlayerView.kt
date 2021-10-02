package com.example.rankupandroid

import android.content.Context
import android.content.res.ColorStateList
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


    private fun setVisibility(specified: Boolean, deletable: Boolean) {
        binding.apply {
            if (specified) {
                if (deletable) {
                    addPlayerButton.apply {
                        setImageResource(android.R.drawable.ic_delete)
                        backgroundTintList = ColorStateList.valueOf(context.getColor(R.color.white))
                        elevation = 0.0f // this removes the surrounding shadow
                        // TODO modify click listener
                    }
                } else {
                    addPlayerButton.visibility = View.INVISIBLE
                }
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
        player: Player?,
        deletable: Boolean
    ) {
        binding.apply {
            setVisibility(player != null, deletable)
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