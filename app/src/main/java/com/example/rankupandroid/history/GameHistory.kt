package com.example.rankupandroid.history

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.lang.Exception

@Entity(tableName = "game_history_table")
@Parcelize
data class GameHistory(
    @PrimaryKey(autoGenerate = true) val id: Long = -1,
    @ColumnInfo(name = "game_start_time") var startTime: String = "unknown start time",
    @ColumnInfo(name = "rounds_myself") var roundsMyself: MutableList<Int> = mutableListOf(),
    @ColumnInfo(name = "rounds_opponent1") var roundsOpponent1: MutableList<Int> = mutableListOf(),
    @ColumnInfo(name = "rounds_teammate") var roundsTeammate: MutableList<Int> = mutableListOf(),
    @ColumnInfo(name = "rounds_opponent2") var roundsOpponent2: MutableList<Int> = mutableListOf()
) : Parcelable {
    fun rounds(i: Int): MutableList<Int> {
        return when (i) {
            0 -> roundsMyself
            1 -> roundsOpponent1
            2 -> roundsTeammate
            3 -> roundsOpponent2
            else -> throw Exception("Invalid rounds index $i")
        }
    }
}