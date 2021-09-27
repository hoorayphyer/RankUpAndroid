package com.example.rankupandroid.playerslist

import com.example.rankupandroid.Player
import com.example.rankupandroid.network.RankUpRemote

// Right now it's always loading from remote
class PlayersDataRepository {
    private val remote = RankUpRemote.service

    suspend fun getPlayersFromRemote(): List<Player> {
        // The fake server only provides player names, so we populate by hand their avatar urls.
        // The actual loading is done in playerslist.BindingAdapters.kt
        val players = remote.queryPlayers()
            .map {
                Player(it.account_id, it.nickname, null)
            }

        // avatars chosen from
        // https://www.flaticon.com/free-icons/avatar?word=avatar&type=icon&license=selection&order_by=4

        players[0].avatarUrl = "https://cdn-icons-png.flaticon.com/512/921/921110.png"
        players[1].avatarUrl = "https://cdn-icons-png.flaticon.com/512/921/921124.png"
        players[2].avatarUrl = "https://cdn-icons-png.flaticon.com/512/949/949675.png"
        players[3].avatarUrl = "https://cdn-icons-png.flaticon.com/512/949/949666.png"
        players[4].avatarUrl = "https://cdn-icons-png.flaticon.com/512/921/921089.png"
        players[5].avatarUrl = "https://cdn-icons-png.flaticon.com/512/921/921071.png"
        players[6].avatarUrl = "https://cdn-icons-png.flaticon.com/512/921/921077.png"
        players[7].avatarUrl = "https://cdn-icons-png.flaticon.com/512/921/921099.png"
        players[8].avatarUrl = "https://cdn-icons-png.flaticon.com/512/921/921104.png"
        players[9].avatarUrl = "https://cdn-icons-png.flaticon.com/512/921/921082.png"

        return players
    }
}