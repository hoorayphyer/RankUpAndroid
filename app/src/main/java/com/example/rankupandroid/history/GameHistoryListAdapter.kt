package com.example.rankupandroid.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.rankupandroid.databinding.GameHistoryListItemBinding

class GameHistoryListAdapter(private val clickListener: GameHistoryItemClickListener) :
    ListAdapter<GameHistory, GameHistoryListAdapter.ViewHolder>(DiffCallback()) {

    class ViewHolder private constructor(private val binding: GameHistoryListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: GameHistory, clickListener: GameHistoryItemClickListener) {
            binding.apply {
                history = item
                this.clickListener = clickListener
                executePendingBindings()
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = GameHistoryListItemBinding.inflate(inflater, parent, false)
                return ViewHolder(binding)
            }
        }

    }

    class DiffCallback : DiffUtil.ItemCallback<GameHistory>() {
        override fun areItemsTheSame(oldItem: GameHistory, newItem: GameHistory): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: GameHistory, newItem: GameHistory): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }
}

class GameHistoryItemClickListener(val callback: (history: GameHistory) -> Unit) {
    fun onClick(history: GameHistory) = callback(history)
}