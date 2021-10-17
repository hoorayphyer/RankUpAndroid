package com.example.rankupandroid.history

import androidx.lifecycle.*
import kotlinx.coroutines.launch

class ViewHistoryViewModel(private val repo: GameHistoryDataRepository) : ViewModel() {
    private var _gameHistories: MutableLiveData<List<GameHistory>> =
        MutableLiveData<List<GameHistory>>()
    val gameHistories: LiveData<List<GameHistory>> = _gameHistories

    fun saveHistory(history: GameHistory) {
        viewModelScope.launch {
            repo.saveHistory(history)
        }
    }

    fun loadHistories() {
        viewModelScope.launch {
            _gameHistories.value = repo.readHistories()
        }
    }

    fun deleteHistories() {
        viewModelScope.launch {
            repo.deleteHistories()
        }
        _gameHistories.value = listOf()
    }
}

class ViewHistoryViewModelFactory(private val repo: GameHistoryDataRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return ViewHistoryViewModel(repo) as T
    }
}