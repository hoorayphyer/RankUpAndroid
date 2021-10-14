package com.example.rankupandroid.history

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [GameHistory::class], version = 1, exportSchema = false)
abstract class GameHistoryDatabase : RoomDatabase() {
    abstract val dao: GameHistoryDao

    companion object {
        // a singleton instance of the database
        @Volatile
        private var INSTANCE: GameHistoryDatabase? = null

        fun getInstance(context: Context): GameHistoryDatabase {
            synchronized(this) {
                var instance = INSTANCE
                // if there is not an existing instance of database, create one
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        GameHistoryDatabase::class.java,
                        "rankup_game_history_database"
                    ).fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}