package com.example.rankupandroid.rankup

import android.widget.ImageView
import com.example.rankupandroid.R

// card images are from
// https://commons.wikimedia.org/wiki/Category:SVG_playing_cards_1
// Android has the following restrictions
// 1. subdirectories under /drawable are just ignored
// 2. resource names can only contain a-z, 0-9, _
// 3. the extension can only be .png or .xml

internal object CardImages {
    private val CardImageArray = arrayOf(
        "cards_2_club", "cards_2_diamond", "cards_2_heart", "cards_2_spade",
        "cards_3_club", "cards_3_diamond", "cards_3_heart", "cards_3_spade",
        "cards_4_club", "cards_4_diamond", "cards_4_heart", "cards_4_spade",
        "cards_5_club", "cards_5_diamond", "cards_5_heart", "cards_5_spade",
        "cards_6_club", "cards_6_diamond", "cards_6_heart", "cards_6_spade",
        "cards_7_club", "cards_7_diamond", "cards_7_heart", "cards_7_spade",
        "cards_8_club", "cards_8_diamond", "cards_8_heart", "cards_8_spade",
        "cards_9_club", "cards_9_diamond", "cards_9_heart", "cards_9_spade",
        "cards_10_club", "cards_10_diamond", "cards_10_heart", "cards_10_spade",
        "cards_j_club", "cards_j_diamond", "cards_j_heart", "cards_j_spade",
        "cards_q_club", "cards_q_diamond", "cards_q_heart", "cards_q_spade",
        "cards_k_club", "cards_k_diamond", "cards_k_heart", "cards_k_spade",
        "cards_ace_club", "cards_ace_diamond", "cards_ace_heart", "cards_ace_spade",
        "cards_joker_black", "cards_joker_red"
    )

    // `get` defines the operator[]
    operator fun get(cardInt: Int): String {
        return CardImageArray[cardInt % CardImageArray.size]
    }

    operator fun get(card: Card): String {
        return CardImageArray[card.value]
    }
}

fun setCardImage(view: ImageView, cardInt: Int) {
    view.apply {
        setImageResource(
            resources.getIdentifier(
                CardImages[cardInt],
                "drawable", context.packageName
            )
        )
        setBackgroundResource(R.drawable.card_border)
    }
}

fun resetCardImage(view: ImageView) {
    view.apply {
        setImageResource(
            resources.getIdentifier(
                "cards_blank",
                "drawable", context.packageName
            )
        )
        setBackgroundResource(android.R.color.transparent)
    }
}
