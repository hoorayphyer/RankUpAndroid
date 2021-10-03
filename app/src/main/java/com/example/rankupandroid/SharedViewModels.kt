package com.example.rankupandroid

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.properties.Delegates

class SharedViewModelSelectedPlayers : ViewModel() {
    var callback: (player: Player) -> Unit = {} // default is an no-op

    private val _myself: MutableLiveData<Player> by lazy {
        MutableLiveData<Player>(Player(0, "Me", R.drawable.my_avatar, null, true))
    }

    val myself: LiveData<Player> = _myself

    var playerChangeCallback: (Player?, Player?) -> Unit =
        { oldValue: Player?, newValue: Player? -> }

    val teammate: MutableLiveData<Player?> by Delegates.observable(MutableLiveData<Player?>(null)) { _, oldValue, newValue ->
        playerChangeCallback(oldValue.value, newValue.value)
    }

    val opponent1: MutableLiveData<Player?> by lazy {
        MutableLiveData<Player?>(null)
    }
    val opponent2: MutableLiveData<Player?> by lazy {
        MutableLiveData<Player?>(null)
    }
}
