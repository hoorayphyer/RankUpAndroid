package com.example.rankupandroid.playerslist

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.example.rankupandroid.Player
import com.example.rankupandroid.glideLoad

// this sets the method to retrieve attribute "avatarUrl", used in players_list_item.xml
@BindingAdapter("avatarId")
fun bindPlayerAvatar(imgView: ImageView, player: Player) {
    glideLoad(imgView.context, player)
//            .placeholder(R.drawable.loading_spinner) // TODO
        .into(imgView)
}