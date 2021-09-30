package com.example.rankupandroid.playerslist

import com.example.rankupandroid.Player
import com.example.rankupandroid.network.RankUpRemote

// Right now it's always loading from remote
class PlayersDataRepository {
    private val remote = RankUpRemote.service

    suspend fun getPlayersFromRemote(): List<Player> {
        // The fake server only provides player names, so we populate by hand their avatar urls.
        // The actual loading is done in playerslist.BindingAdapters.kt

        // avatars chosen from
        // https://www.flaticon.com/free-icons/avatar?word=avatar&type=icon&license=selection&order_by=4
        val avatarUrls = listOf(
            "https://cdn-icons-png.flaticon.com/512/921/921110.png",
            "https://cdn-icons-png.flaticon.com/512/921/921124.png",
            "https://cdn-icons-png.flaticon.com/512/949/949675.png",
            "https://cdn-icons-png.flaticon.com/512/949/949666.png",
            "https://cdn-icons-png.flaticon.com/512/921/921089.png",
            "https://cdn-icons-png.flaticon.com/512/921/921071.png",
            "https://cdn-icons-png.flaticon.com/512/921/921077.png",
            "https://cdn-icons-png.flaticon.com/512/921/921099.png",
            "https://cdn-icons-png.flaticon.com/512/921/921104.png",
            "https://cdn-icons-png.flaticon.com/512/921/921082.png"
        )
        return (remote.queryPlayers() zip avatarUrls)
            .map {
                Player(it.first.account_id, it.first.nickname, null, it.second)
            }
    }
}