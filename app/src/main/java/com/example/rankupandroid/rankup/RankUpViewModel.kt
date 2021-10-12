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

    private val handOfOthers =
        arrayOf(arrayListOf<Card>(), arrayListOf<Card>(), arrayListOf<Card>())

    private var cardSequence = mutableListOf<Int>()

    private var selectedCardInts = mutableSetOf<Int>()

    fun initializeDeck() {
        _cardsInHand.value = arrayListOf()
        handOfOthers.forEach {
            it.clear()
        }

        cardSequence = (0..53).toMutableList()
        cardSequence.shuffle()
    }

    fun dealCardTo(playerInt: Int) {
        val card = Card(cardSequence.removeFirst())
        when (playerInt) {
            0 -> {
                _cardsInHand.value?.add(card)
                _cardsInHand.value = _cardsInHand.value
            }
            else -> handOfOthers[playerInt - 1].add(card)
        }
    }

    fun toNextPhase() {
        _gamePhase.value = when (_gamePhase.value!!) {
            GamePhase.END -> GamePhase.DEAL
            GamePhase.DEAL -> GamePhase.PLAY
            GamePhase.PLAY -> GamePhase.END
        }
    }

    fun selectCardInHand(cardInt: Int) {
        selectedCardInts.add(cardInt)
    }

    fun deselectCardInHand(cardInt: Int) {
        selectedCardInts.remove(cardInt)
    }

    fun playCards(playerInt: Int): Pair<String?, Int> {
        if (playerInt == 0) {
            return when {
                selectedCardInts.size == 1 -> {
                    _cardsInHand.value!!.removeIf {
                        it.value in selectedCardInts
                    }
                    _cardsInHand.value = _cardsInHand.value
                    val res = Pair(null, selectedCardInts.first())
                    selectedCardInts.clear()
                    res
                }
                selectedCardInts.isNotEmpty() -> {
                    Pair("select only ONE card!", -1)
                }
                else -> {
                    Pair("no card selected!", -1)
                }
            }
        } else {
            val card = handOfOthers[playerInt - 1].removeFirst()
            return Pair(null, card.value)
        }
    }
}