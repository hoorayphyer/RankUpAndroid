package com.example.rankupandroid.rankup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.rankupandroid.history.GameHistory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.collections.ArrayList

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

    private var round = 0

    private val handOfOthers =
        arrayOf(arrayListOf<Card>(), arrayListOf<Card>(), arrayListOf<Card>())

    private var cardSequence = mutableListOf<Int>()

    private var selectedCardInts = mutableSetOf<Int>()

    private var history = GameHistory()

    fun initializeDeck() {
        _cardsInHand.value = arrayListOf()
        handOfOthers.forEach {
            it.clear()
        }

        cardSequence = (0..53).toMutableList()
        cardSequence.shuffle()

        round = 0

        val currentTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        history = GameHistory(startTime = currentTime.format(formatter))
    }

    fun advanceRound() {
        ++round
    }

    fun isGameFinished(): Boolean {
        return round == (dealingEnd - dealingBegin) / 4
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
                    val card = selectedCardInts.first()
                    val res = Pair(null, card)
                    history.rounds(playerInt).add(card)
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
            history.rounds(playerInt).add(card.value)
            return Pair(null, card.value)
        }
    }

    fun thisGameHistory(): GameHistory {
        require(isGameFinished())
        return history
    }
}