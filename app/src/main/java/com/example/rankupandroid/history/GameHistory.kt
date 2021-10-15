package com.example.rankupandroid.history

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "game_history_table")
@Parcelize
data class GameHistory(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "game_start_time") val startTime: String,
    @ColumnInfo(name = "rounds_myself") val roundsMyself: MutableList<Int>, // Int for card integers
    @ColumnInfo(name = "rounds_opponent1") val roundsOpponent1: MutableList<Int>,
    @ColumnInfo(name = "rounds_teammate") val roundsTeammate: MutableList<Int>,
    @ColumnInfo(name = "rounds_opponent2") val roundsOpponent2: MutableList<Int>
) : Parcelable