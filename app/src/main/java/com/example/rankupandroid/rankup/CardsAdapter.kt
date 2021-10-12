package com.example.rankupandroid.rankup

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.rankupandroid.databinding.CardsListItemBinding

class CardsAdapter(private val clickListener: CardItemClickListener) :
    ListAdapter<Card, CardsAdapter.ViewHolder>(DiffCallback()) {

    class ViewHolder private constructor(val binding: CardsListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Card, clickListener: CardItemClickListener) {
            binding.apply {
                card = item
                this.clickListener = clickListener
                executePendingBindings()
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = CardsListItemBinding.inflate(inflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Card>() {
        override fun areItemsTheSame(oldItem: Card, newItem: Card): Boolean {
            return oldItem.value == newItem.value
        }

        override fun areContentsTheSame(oldItem: Card, newItem: Card): Boolean {
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

class CardItemClickListener(val callback: (view: View, card: Card) -> Unit) {
    fun onClick(view: View, card: Card) = callback(view, card)
}

fun isSelectedMode(view: View): Boolean {
    val params = view.layoutParams as ConstraintLayout.LayoutParams
    // NOTE this relies on that params.topToTop has a named 'id' in xml
    return params.topToTop != -1
}

fun setToSelected(view: View) {
    val params = view.layoutParams as ConstraintLayout.LayoutParams
    params.topToTop = (view.parent as View).id
    params.bottomToBottom = -1
}

fun setToUnselected(view: View) {
    val params = view.layoutParams as ConstraintLayout.LayoutParams
    params.topToTop = -1
    params.bottomToBottom = (view.parent as View).id
}
