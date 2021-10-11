package com.example.rankupandroid.rankup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

enum class GamePhase {
    END, DEAL, PLAY
}

class RankUpViewModel : ViewModel() {
    private val _cardsInHand: MutableLiveData<List<Card>> = MutableLiveData<List<Card>>()
    val cardsInHand: LiveData<List<Card>> = _cardsInHand

    private val _gamePhase = MutableLiveData<GamePhase>(GamePhase.END)
    val gamePhase : LiveData<GamePhase> = _gamePhase

    init {
        // TODO hard coded for tests
        _cardsInHand.value = (0..53).map {
            Card(it)
        }
    }

    fun toNextPhase() {
        _gamePhase.value = when(_gamePhase.value!!) {
            GamePhase.END -> GamePhase.DEAL
            GamePhase.DEAL -> GamePhase.PLAY
            GamePhase.PLAY -> GamePhase.END
        }
    }
}