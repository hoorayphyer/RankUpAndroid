package com.example.rankupandroid.playerslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.rankupandroid.Player
import com.example.rankupandroid.databinding.PlayersListItemBinding

class PlayersListAdapter(private val clickListener: PlayerItemClickListener) :
    ListAdapter<Player, PlayersListAdapter.ViewHolder>(DiffCallback()) {

    class ViewHolder private constructor(private val binding: PlayersListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Player, clickListener: PlayerItemClickListener) {
            // this binding is a must regardless of participating
            binding.player = item
            if (item.participated) {
                itemView.visibility = View.GONE
            } else {
                binding.apply {
                    playerListItemName.text = item.name
                    this.clickListener = clickListener
                    executePendingBindings()
                }
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = PlayersListItemBinding.inflate(inflater, parent, false)
                return ViewHolder(binding)
            }
        }

    }

    class DiffCallback : DiffUtil.ItemCallback<Player>() {
        override fun areItemsTheSame(oldItem: Player, newItem: Player): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Player, newItem: Player): Boolean {
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

class PlayerItemClickListener(val callback: (player: Player) -> Unit) {
    fun onClick(player: Player) = callback(player)
}