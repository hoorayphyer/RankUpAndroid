package com.example.rankupandroid.playerslist

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

// this sets the method to retrieve attribute "avatarUrl", used in players_list_item.xml
@BindingAdapter("avatarUrl")
fun bindImagePictureOfDay(imgView : ImageView, imgUrl : String?) {
    imgUrl?.let{
        val imgUri = it.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context).load(imgUri).into(imgView)
    }
}