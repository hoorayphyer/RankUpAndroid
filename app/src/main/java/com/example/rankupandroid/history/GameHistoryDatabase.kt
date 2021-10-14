package com.example.rankupandroid.history

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

class GameHistoryConverters {
    private val separator: String = "-"

    @TypeConverter
    fun roundsFromString(rounds: MutableList<Int>?): String? {
        return rounds?.joinToString(separator)
    }

    @TypeConverter
    fun roundsToString(str: String?): MutableList<Int>? {
        return str?.run {
            split(separator).map { it.toInt() }.toMutableList()
        }
    }
}

@Dao
interface GameHistoryDatabaseDao {
    @Query("SELECT * FROM game_history_table")
    fun getAllGameHistories(): LiveData<List<GameHistory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateDatabase(histories: List<GameHistory>)

    @Query("DELETE FROM game_history_table WHERE game_time < :date")
    fun deletePriorTo(date: String)
}

@Database(entities = [GameHistory::class], version = 1, exportSchema = false)
@TypeConverters(GameHistoryConverters::class)
abstract class GameHistoryDatabase : RoomDatabase() {
    abstract val dao: GameHistoryDatabaseDao

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