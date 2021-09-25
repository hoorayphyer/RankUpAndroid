package com.example.rankupandroid.localdatabase

// TODO I don't want to save player into database

//import androidx.lifecycle.LiveData
//import androidx.room.Dao
//import androidx.room.Insert
//import androidx.room.OnConflictStrategy
//import androidx.room.Query
//import com.example.rankupandroid.Player
//
//@Dao
//interface PlayerDao {
//    @Query("SELECT * FROM players_data_table")
//    fun getAllPlayers() : LiveData<List<Player>>
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun updateDatabase( asteroids : List<Player> )
//}