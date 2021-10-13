package com.example.rankupandroid.history

//import android.content.Context
//import androidx.room.Database
//import androidx.room.Room
//import androidx.room.RoomDatabase
//import com.example.rankupandroid.Player
//
//@Database(entities = [Player::class], version = 1, exportSchema = false)
//abstract class PlayerDatabase : RoomDatabase() {
//    abstract val dao : PlayerDao
//
//    companion object {
//        // a singleton instance of the database
//        @Volatile
//        private var INSTANCE : PlayerDatabase? = null
//
//        fun getInstance(context: Context) : PlayerDatabase {
//            synchronized(this) {
//                var instance = INSTANCE
//                // if there is not an existing instance of database, create one
//                if (instance == null) {
//                    instance = Room.databaseBuilder(
//                        context.applicationContext,
//                        PlayerDatabase::class.java,
//                        "rankup_player_database"
//                    ).fallbackToDestructiveMigration()
//                        .build()
//                    INSTANCE = instance
//                }
//
//                return instance
//            }
//        }
//    }
//
//
//}