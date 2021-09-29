package com.example.rankupandroid

import androidx.lifecycle.ViewModel

class SharedViewModelHostGameFragPlayersListFrag : ViewModel() {
    var callback: (player: Player) -> Unit = {} // default is an no-op

    var yourTeammate: Player? = null
    var opponent1: Player? = null
    var opponent2: Player? = null
}
