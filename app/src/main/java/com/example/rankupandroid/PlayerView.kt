package com.example.rankupandroid

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.rankupandroid.databinding.PlayerViewBinding

class PlayerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : ConstraintLayout(context, attrs) {

    private lateinit var binding : PlayerViewBinding

    init {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        binding = PlayerViewBinding.bind(View.inflate(context, R.layout.player_view, this))

        val ta = context.obtainStyledAttributes(attrs, R.styleable.PlayerView)
        try {
            val playerName = ta.getString(R.styleable.PlayerView_player_name)?: "unknown name"
            val playerSpecified = ta.getBoolean(R.styleable.PlayerView_player_specified, false)
            val playerIsFriend = ta.getBoolean(R.styleable.PlayerView_player_is_friend, false)
            var playerImageId : Int? = ta.getResourceId(R.styleable.PlayerView_player_image, 0)
            if ( playerImageId == 0) {
                playerImageId = null
            }
            setUpPlayerView(playerName, playerImageId, playerSpecified, playerIsFriend)
        } finally {
            ta.recycle()
        }
    }

    private fun setUpPlayerView(playerName: String, playerImageId: Int?, playerSpecified: Boolean, playerIsFriend: Boolean) {
        if (!playerSpecified) {
            binding.addPlayerButton.visibility = View.VISIBLE
            binding.playerName.visibility =View.INVISIBLE
            binding.addFriendButton.visibility =View.INVISIBLE
            binding.playerImage.visibility = View.INVISIBLE
            // TODO set the image to a plus sign(maybe a FAB is good), then add click listener
            return
        }
        binding.addPlayerButton.visibility = View.GONE

        binding.playerName.text = playerName
        if (playerImageId != null) {
            val drawable = AppCompatResources.getDrawable(context, playerImageId)
            binding.playerImage.setImageDrawable(drawable)
        }
        binding.addFriendButton.visibility = if(playerIsFriend) View.INVISIBLE else View.VISIBLE
    }
}