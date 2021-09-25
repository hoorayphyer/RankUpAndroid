package com.example.rankupandroid.playerslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rankupandroid.Player
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlayersListViewModel(private val repo : PlayersDataRepository) : ViewModel() {
    private val _playersList : MutableLiveData<List<Player>> = MutableLiveData<List<Player>>()
    val playersList : LiveData<List<Player>> = _playersList

    init{
        viewModelScope.launch(Dispatchers.IO) {
            // get list from remote then turn it into list of players
            _playersList.value = repo.getPlayersFromRemote()
        }
    }
}