package com.example.rankupandroid.rankup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

enum class GamePhase {
    END, DEAL, PLAY
}

class RankUpViewModel : ViewModel() {
    private val _cardsInHand: MutableLiveData<ArrayList<Card>> = MutableLiveData<ArrayList<Card>>()
    val cardsInHand: LiveData<ArrayList<Card>> = _cardsInHand

    private val _gamePhase = MutableLiveData(GamePhase.END)
    val gamePhase: LiveData<GamePhase> = _gamePhase

    val dealingBegin: Int = 0
    val dealingEnd: Int = 12

    private val handOpponent1 = arrayListOf<Card>()
    private val handTeammate = arrayListOf<Card>()
    private val handOpponent2 = arrayListOf<Card>()

    private var cardSequence = mutableListOf<Int>()

    fun initializeDeck() {
        _cardsInHand.value = arrayListOf()
        handOpponent1.clear()
        handTeammate.clear()
        handOpponent2.clear()

        cardSequence = (0..53).toMutableList()
        cardSequence.shuffle()
    }

    fun dealCardTo(playerInt: Int) {
        val card = Card(cardSequence.removeFirst())
        when (playerInt % 4) {
            0 -> {
                _cardsInHand.value?.add(card)
                _cardsInHand.value = _cardsInHand.value
            }
            1 -> handOpponent1.add(card)
            2 -> handTeammate.add(card)
            3 -> handOpponent2.add(card)
        }
    }

    fun toNextPhase() {
        _gamePhase.value = when (_gamePhase.value!!) {
            GamePhase.END -> GamePhase.DEAL
            GamePhase.DEAL -> GamePhase.PLAY
            GamePhase.PLAY -> GamePhase.END
        }
    }
}