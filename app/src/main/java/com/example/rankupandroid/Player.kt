package com.example.rankupandroid

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "players_data_table")
@Parcelize
data class Player (
    @PrimaryKey val id: Long,
    @ColumnInfo(name="name") val name: String,
    @ColumnInfo(name="image_id") val imageId: Int,
    @ColumnInfo(name="is_friend") val isFriend: Boolean) : Parcelable