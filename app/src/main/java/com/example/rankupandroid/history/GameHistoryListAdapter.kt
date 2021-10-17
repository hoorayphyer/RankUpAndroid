package com.example.rankupandroid.history

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.rankupandroid.R
import com.example.rankupandroid.databinding.GameHistoryListItemCardsPlayedBinding
import com.example.rankupandroid.databinding.GameHistoryListItemGameHeaderBinding
import com.example.rankupandroid.databinding.GameHistoryListItemRoundHeaderBinding
import com.example.rankupandroid.rankup.setCardImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// for each game history, we will use different view holders for game header, round header, and
// cards that are played
sealed class GameHistoryItem {
    companion object {
        protected fun uniqueIdBuilder(game_id: Long, round: Int, showCards: Boolean): Long {
            // for GameHeader, round is zero and showCards is false
            // for RoundHeader, showCards is false but round must be nonzero
            // for CardsItem, showCards is true and round is nonzero
            return game_id * 100 + 10 * round + (if (showCards) 1 else 0)
        }

        const val VIEW_TYPE_GAME_HEADER = 0
        const val VIEW_TYPE_ROUND_HEADER = 1
        const val VIEW_TYPE_CARDS_PLAYED = 2
    }

    abstract val id: Long

    // must inherit to register it in sealed classes
    data class GameHeader(override val id: Long, val startTime: String) : GameHistoryItem() {
        constructor(history: GameHistory) : this(
            uniqueIdBuilder(history.id, 0, false), history
                .startTime
        ) {
        }
    }

    data class RoundHeader(override val id: Long, val round_i_0_based: Int) : GameHistoryItem() {
        constructor(history: GameHistory, round_i_0_based: Int) : this(
            uniqueIdBuilder
                (history.id, round_i_0_based + 1, false), round_i_0_based
        ) {
        }
    }

    data class CardsPlayed(override val id: Long, val cards: List<Int>) : GameHistoryItem
        () {
        constructor(history: GameHistory, round_i_0_based: Int) : this(
            uniqueIdBuilder(
                history
                    .id, round_i_0_based + 1, true
            ), listOf(
                history.roundsMyself[round_i_0_based],
                history
                    .roundsOpponent1[round_i_0_based],
                history.roundsTeammate[round_i_0_based],
                history
                    .roundsOpponent2[round_i_0_based]
            )
        ) {
        }
    }
}

class ViewHolderGameHeader private constructor(private val binding: GameHistoryListItemGameHeaderBinding) :
    RecyclerView
    .ViewHolder(binding.root) {
    fun bind(item: GameHistoryItem.GameHeader, context: Context) {
        binding.gameTimeText.text = context.getString(
            R.string.game_history_list_view_game_time, item.startTime
        )
    }

    companion object {
        fun from(parent: ViewGroup): ViewHolderGameHeader {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding =
                GameHistoryListItemGameHeaderBinding.inflate(layoutInflater, parent, false)
            return ViewHolderGameHeader(binding)
        }
    }
}

class ViewHolderRoundHeader private constructor(
    private val binding:
    GameHistoryListItemRoundHeaderBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: GameHistoryItem.RoundHeader, context: Context) {
        binding.roundText.text = context.getString(
            R.string.game_history_list_view_round, item
                .round_i_0_based + 1
        )
    }

    companion object {
        fun from(parent: ViewGroup): ViewHolderRoundHeader {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = GameHistoryListItemRoundHeaderBinding.inflate(
                layoutInflater, parent,
                false
            )
            return ViewHolderRoundHeader(binding)
        }
    }
}

class ViewHolderCardsPlayed private constructor(private val binding: GameHistoryListItemCardsPlayedBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: GameHistoryItem.CardsPlayed) {
        binding.apply {
            setCardImage(cards1, item.cards[0])
            setCardImage(cards2, item.cards[1])
            setCardImage(cards3, item.cards[2])
            setCardImage(cards4, item.cards[3])
        }
    }

    companion object {
        fun from(parent: ViewGroup): ViewHolderCardsPlayed {
            val inflater = LayoutInflater.from(parent.context)
            val binding = GameHistoryListItemCardsPlayedBinding.inflate(inflater, parent, false)
            return ViewHolderCardsPlayed(binding)
        }
    }
}


class GameHistoryListAdapter :
    ListAdapter<GameHistoryItem, RecyclerView.ViewHolder>(DiffCallback()) {

    class DiffCallback : DiffUtil.ItemCallback<GameHistoryItem>() {
        override fun areItemsTheSame(oldItem: GameHistoryItem, newItem: GameHistoryItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: GameHistoryItem,
            newItem: GameHistoryItem
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is GameHistoryItem.GameHeader -> GameHistoryItem.VIEW_TYPE_GAME_HEADER
            is GameHistoryItem.RoundHeader -> GameHistoryItem.VIEW_TYPE_ROUND_HEADER
            is GameHistoryItem.CardsPlayed -> GameHistoryItem.VIEW_TYPE_CARDS_PLAYED
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            GameHistoryItem.VIEW_TYPE_GAME_HEADER -> ViewHolderGameHeader.from(parent)
            GameHistoryItem.VIEW_TYPE_ROUND_HEADER -> ViewHolderRoundHeader.from(parent)
            GameHistoryItem.VIEW_TYPE_CARDS_PLAYED -> ViewHolderCardsPlayed.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolderGameHeader -> {
                val item = getItem(position) as GameHistoryItem.GameHeader
                holder.bind(item, holder.itemView.context)
            }
            is ViewHolderRoundHeader -> {
                val item = getItem(position) as GameHistoryItem.RoundHeader
                holder.bind(item, holder.itemView.context)
            }
            is ViewHolderCardsPlayed -> {
                val item = getItem(position) as GameHistoryItem.CardsPlayed
                holder.bind(item)
            }
        }
    }

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    fun addHeaderAndSubmitList(list: List<GameHistory>) {
        adapterScope.launch {
            val items = list.map {
                val res = mutableListOf<GameHistoryItem>(GameHistoryItem.GameHeader(it))
                for (round_i in 0 until it.roundsMyself.size) {
                    res.add(GameHistoryItem.RoundHeader(it, round_i))
                    res.add(GameHistoryItem.CardsPlayed(it, round_i))
                }
                res
            }.flatten()

            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }
}