package com.example.rankupandroid.playerslist

import androidx.lifecycle.*
import com.example.rankupandroid.Player
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlayersListViewModel(private val repo: PlayersDataRepository) : ViewModel() {
    private val _activePlayers: MutableLiveData<List<Player>> = MutableLiveData<List<Player>>()
    val activePlayers: LiveData<List<Player>> = _activePlayers

    init {
        viewModelScope.launch(Dispatchers.IO) {
            // get list from remote then turn it into list of players
            // unlike `setValue`, `postValue` posts a task to a main thread to set the given value. So if you have a following code executed in the main thread:
            _activePlayers.postValue(repo.getPlayersFromRemote())
        }
    }

    fun updatePlayer(player: Player, participated: Boolean) {
        _activePlayers.apply {
            value?.find {
                it.id == player.id
            }?.let {
                it.participated = participated
                // force updating mutableLiveData
                value = value!!
            }
        }
    }
}

class PlayersListViewModelFactory(private val repo: PlayersDataRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return PlayersListViewModel(repo) as T
    }
}