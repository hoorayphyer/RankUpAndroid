package com.example.rankupandroid.history

import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GameHistoryDataRepository(private val cache: GameHistoryDatabaseDao) {
    private var _gameHistories = cache.getAllGameHistories()
    val gameHistories: LiveData<List<GameHistory>> = _gameHistories

    // call this function to save histories
    suspend fun saveHistories(histories: List<GameHistory>) {
        withContext(Dispatchers.IO) {
//            val dateFormat = SimpleDateFormat("YYYY-MM-dd", Locale.getDefault())
//            val cal = Calendar.getInstance()
//            val startDate = dateFormat.format(cal.time)
//            cal.add(Calendar.DAY_OF_YEAR, 7)
//            val endDate = dateFormat.format(cal.time)
            try {
//                cache.deletePriorTo(startDate)
                cache.updateDatabase(histories)
            } catch (e: Exception) {
            }
        }
    }
}