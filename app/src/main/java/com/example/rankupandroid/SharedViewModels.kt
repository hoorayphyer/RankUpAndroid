package com.example.rankupandroid

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModelSelectedPlayers : ViewModel() {
    var callback: (player: Player) -> Unit = {} // default is an no-op

    private val _myself: MutableLiveData<Player> by lazy {
        MutableLiveData<Player>(Player(0, "Me", R.drawable.my_avatar))
    }

    val myself: LiveData<Player> = _myself

    val teammate: MutableLiveData<Player?> by lazy {
        MutableLiveData<Player?>(null)
    }
    val opponent1: MutableLiveData<Player?> by lazy {
        MutableLiveData<Player?>(null)
    }
    val opponent2: MutableLiveData<Player?> by lazy {
        MutableLiveData<Player?>(null)
    }
}
