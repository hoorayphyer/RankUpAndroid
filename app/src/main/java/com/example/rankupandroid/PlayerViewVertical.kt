package com.example.rankupandroid

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.rankupandroid.databinding.PlayerViewVerticalBinding

class PlayerViewVertical @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : ConstraintLayout(context, attrs) {

    private var binding: PlayerViewVerticalBinding =
        PlayerViewVerticalBinding.bind(View.inflate(context, R.layout.player_view_vertical, this))

    fun updateViewVertical(
        player: Player?
    ) {
        requireNotNull(player)
        binding.apply {
            playerNameVertical.text = player.name
            glideLoad(context, player).into(playerImageVertical)
        }
    }
}
