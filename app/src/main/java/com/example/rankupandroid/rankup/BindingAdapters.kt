package com.example.rankupandroid.rankup

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

// this sets the method to retrieve attribute "avatarUrl", used in players_list_item.xml
@BindingAdapter("cardImage")
fun bindCardImage(imgView: ImageView, card: Card) {
    // TODO cards_2_club is hard coded for test
    val cardImg = imgView.resources.getIdentifier(
        "cards_2_club", "drawable",
        imgView.context.packageName
    )
    Glide.with(imgView.context).load(cardImg).into(imgView)
}
