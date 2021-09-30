package com.example.rankupandroid

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData

class SharedViewModelSelectedPlayers : ViewModel() {
    var callback: (player: Player) -> Unit = {} // default is an no-op

    val myself: LiveData<Player?> by lazy {
        liveData { Player(0, "Me", R.drawable.my_avatar) }
    }

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
