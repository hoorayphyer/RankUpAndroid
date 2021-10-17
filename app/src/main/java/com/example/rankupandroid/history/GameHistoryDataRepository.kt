package com.example.rankupandroid.history

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GameHistoryDataRepository(private val cache: GameHistoryDatabaseDao) {
    suspend fun saveHistory(history: GameHistory) {
        withContext(Dispatchers.IO) {
            try {
                cache.updateDatabase(history)
            } catch (e: Exception) {
            }
        }
    }

    suspend fun readHistories(): List<GameHistory> {
        return withContext(Dispatchers.IO) {
            cache.getAllGameHistories()
        }
    }

    suspend fun deleteHistories() {
        withContext(Dispatchers.IO) {
            try {
                cache.deleteAllGameHistories()
            } catch (e: Exception) {
            }
        }
    }
}