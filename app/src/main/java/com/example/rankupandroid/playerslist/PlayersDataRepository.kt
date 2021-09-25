package com.example.rankupandroid.playerslist

import com.example.rankupandroid.Player
import com.example.rankupandroid.network.RankUpRemote

// Right now it's always loading from remote
class PlayersDataRepository {
    private val remote = RankUpRemote.service

    suspend fun getPlayersFromRemote() : List<Player> {
        val results = remote.queryPlayers()
        return results.players.map{
            Player(it.account_id, it.nickname)
        }
    }
}