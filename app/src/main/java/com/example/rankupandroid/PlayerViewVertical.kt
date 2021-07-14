package com.example.rankupandroid

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.rankupandroid.databinding.PlayerViewVerticalBinding

class PlayerViewVertical @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : ConstraintLayout(context, attrs) {

    private lateinit var binding : PlayerViewVerticalBinding

    init {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        binding = PlayerViewVerticalBinding.bind(View.inflate(context, R.layout.player_view, this))

        val ta = context.obtainStyledAttributes(attrs, R.styleable.PlayerViewVertical)
        try {
            val playerName = ta.getString(R.styleable.PlayerViewVertical_player_name_vertical)?: "unknown name"
            val playerIsFriend = ta.getBoolean(R.styleable.PlayerViewVertical_player_is_friend_vertical, false)
            var playerImageId : Int? = ta.getResourceId(R.styleable.PlayerViewVertical_player_image_vertical, 0)
            if ( playerImageId == 0) {
                playerImageId = null
            }
            setUpPlayerView(playerName, playerImageId, playerIsFriend)
        } finally {
            ta.recycle()
        }
    }

    private fun setUpPlayerView(playerName: String, playerImageId: Int?, playerIsFriend: Boolean) {
        binding.playerNameVertical.text = playerName
        if (playerImageId != null) {
            val drawable = AppCompatResources.getDrawable(context, playerImageId)
            binding.playerImageVertical.setImageDrawable(drawable)
        }
        binding.addFriendButtonVertical.visibility = if(playerIsFriend) View.INVISIBLE else View.VISIBLE
    }
}
