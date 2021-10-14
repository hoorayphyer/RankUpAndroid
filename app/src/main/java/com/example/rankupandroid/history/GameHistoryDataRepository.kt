package com.example.rankupandroid.history

import androidx.lifecycle.LiveData

class GameHistoryDataRepository(private val cache: GameHistoryDatabaseDao) {
    private var _gameHistories = cache.getAllGameHistories()
    val gameHistories: LiveData<List<GameHistory>> = _gameHistories
}