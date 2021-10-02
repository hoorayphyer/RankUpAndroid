package com.example.rankupandroid

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.rankupandroid.databinding.PlayerViewVerticalBinding

class PlayerViewVertical @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : ConstraintLayout(context, attrs) {

    private lateinit var binding: PlayerViewVerticalBinding

    private fun setUpPlayerView(playerName: String, playerImageId: Int?) {
        binding.playerNameVertical.text = playerName
        if (playerImageId != null) {
            val drawable = AppCompatResources.getDrawable(context, playerImageId)
            binding.playerImageVertical.setImageDrawable(drawable)
        }
    }
}
