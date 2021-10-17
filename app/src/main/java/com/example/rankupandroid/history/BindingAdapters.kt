package com.example.rankupandroid.history

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.rankupandroid.R
import com.example.rankupandroid.rankup.setCardImage

@BindingAdapter("game_time_text")
fun bindGameTimeText(view: TextView, history: GameHistory) {
    view.text = view.context.getString(
        R.string.game_history_list_view_game_time, history
            .startTime
    )
}

@BindingAdapter("round_text")
fun bindRoundText(view: TextView, i: Int) {
    view.text = view.context.getString(R.string.game_history_list_view_round, i)
}

@BindingAdapter("rounds_player", "round_i")
fun bindCardImage(view: ImageView, rounds: MutableList<Int>, round_i_1_based: Int) {
    setCardImage(view, rounds[round_i_1_based - 1])
}
