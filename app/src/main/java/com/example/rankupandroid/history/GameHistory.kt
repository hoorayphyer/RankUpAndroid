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
    @ColumnInfo(name = "rounds") val rounds: MutableList<Int>, // Int for card integers
) : Parcelable