package com.example.rankupandroid.history

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface GameHistoryDao {
    @Query("SELECT * FROM game_history_table")
    fun getAllGameHistories(): LiveData<List<GameHistory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateDatabase(histories: List<GameHistory>)
}