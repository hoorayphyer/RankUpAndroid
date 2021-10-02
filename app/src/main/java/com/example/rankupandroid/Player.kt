package com.example.rankupandroid

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder

data class Player(
    val id: Long,
    val name: String,
    val avatarIdInt: Int? = null,
    val avatarIdStr: String? = null,

    var participated: Boolean = false
)

fun glideLoad(context: Context, player: Player): RequestBuilder<Drawable> {
    val reqMan = Glide.with(context)
    return when {
        player.avatarIdInt != null -> {
            reqMan.load(player.avatarIdInt)
        }
        player.avatarIdStr != null -> {
            val imgUri = player.avatarIdStr.toUri().buildUpon().scheme("https").build()
            reqMan.load(imgUri)
        }
        else -> {
            reqMan.load(R.drawable.unknown_player_avatar)
        }
    }
}