package com.example.rankupandroid

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.rankupandroid.databinding.PlayerViewBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PlayerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : ConstraintLayout(context, attrs) {

    private var binding: PlayerViewBinding =
        PlayerViewBinding.bind(View.inflate(context, R.layout.player_view, this))


    private fun setVisibility(specified: Boolean, deletable: Boolean) {
        binding.apply {
            if (specified) {
                actionButton.apply {
                    visibility = if (deletable) {
                        setActionButtonLook(this, false)
                        View.VISIBLE
                    } else {
                        View.INVISIBLE
                    }
                }
                playerName.visibility = View.VISIBLE
                playerImage.visibility = View.VISIBLE
            } else {
                actionButton.apply {
                    setActionButtonLook(actionButton, true)
                    visibility = View.VISIBLE
                }
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
            actionButton.setOnClickListener(null)

            if (player != null) {
                playerName.text = player.name
                glideLoad(context, player)
                    .into(binding.playerImage)
            }
        }
    }

    fun setButtonAction(f: OnClickListener?) {
        binding.actionButton.setOnClickListener(f)
    }

    private fun setActionButtonLook(actionButton: FloatingActionButton, isAdd: Boolean) {
        actionButton.apply {
            if (isAdd) {
                setImageResource(android.R.drawable.ic_input_add)
                val value = TypedValue()
                context.theme.resolveAttribute(R.attr.colorSecondary, value, true)
                backgroundTintList = ColorStateList.valueOf(value.data)
                elevation = 6.0f
            } else {
                setImageResource(android.R.drawable.ic_delete)
                backgroundTintList = ColorStateList.valueOf(context.getColor(R.color.white))
                elevation = 0.0f // this removes the surrounding shadow
            }
        }
    }
}