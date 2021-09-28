package com.example.rankupandroid

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.example.rankupandroid.databinding.PlayerViewBinding

class PlayerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : ConstraintLayout(context, attrs) {

    private lateinit var binding: PlayerViewBinding

    init {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        binding = PlayerViewBinding.bind(View.inflate(context, R.layout.player_view, this))

        val ta = context.obtainStyledAttributes(attrs, R.styleable.PlayerView)
        try {
            val playerName = ta.getString(R.styleable.PlayerView_player_name)
            val playerSpecified = ta.getBoolean(R.styleable.PlayerView_player_specified, false)
            var playerImageId: Int? = ta.getResourceId(R.styleable.PlayerView_player_image, 0)
            if (playerImageId == 0) {
                playerImageId = null
            }
            setUpPlayerView(playerName, playerImageId, playerSpecified)
        } finally {
            ta.recycle()
        }
    }

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

    fun setUpPlayerView(
        name: String?,
        imageId: Int?,
        specified: Boolean
    ) {
        binding.apply {
            setVisibility(specified)
            // initialize clicklistener to null. Allow this to be later specified
            addPlayerButton.setOnClickListener(null)

            if (specified) {
                playerName.text = name ?: "no_name_specified"
                Glide.with(context).load(imageId ?: R.drawable.unknown_player_avatar)
                    .into(binding.playerImage)
            }
        }
    }

    fun setAddButtonAction(f: OnClickListener?) {
        binding.addPlayerButton.setOnClickListener(f)
    }
}