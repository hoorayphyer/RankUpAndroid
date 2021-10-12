package com.example.rankupandroid.rankup

import android.widget.ImageView
import androidx.databinding.BindingAdapter

// this sets the method to retrieve attribute "avatarUrl", used in players_list_item.xml
@BindingAdapter("cardImage")
fun bindCardImage(imgView: ImageView, card: Card) {
    val cardImg = imgView.resources.getIdentifier(
        CardImages[card], "drawable",
        imgView.context.packageName
    )
    imgView.setImageResource(cardImg)
    // NOTE using the following Glide call caused border to be drawn first, which looked weird.
    // Glide.with(imgView.context).load(cardImg).into(imgView)
}
