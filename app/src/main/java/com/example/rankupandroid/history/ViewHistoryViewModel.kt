package com.example.rankupandroid.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewHistoryViewModel(val repo: GameHistoryDataRepository) : ViewModel() {
}

class ViewHistoryViewModelFactory(private val repo: GameHistoryDataRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return ViewHistoryViewModel(repo) as T
    }
}