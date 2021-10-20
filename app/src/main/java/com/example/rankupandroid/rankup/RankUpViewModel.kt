package com.example.rankupandroid.rankup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rankupandroid.history.GameHistory
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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

    private val handOfOthers =
        arrayOf(arrayListOf<Card>(), arrayListOf<Card>(), arrayListOf<Card>())

    private var cardSequence = mutableListOf<Int>()

    private var selectedCardInts = mutableSetOf<Int>()

    private var _history = GameHistory()
    val history get() = _history

    private var _whoseTurn = 0
    val whoseTurn get() = _whoseTurn

    private var _isGameFinished = MutableLiveData<Boolean>()
    val isGameFinished: LiveData<Boolean> = _isGameFinished

    private var _clearPlayedCards = MutableLiveData<Boolean>()
    val clearPlayedCards: LiveData<Boolean> = _clearPlayedCards

    fun initializeDeck() {
        _gamePhase.value = GamePhase.DEAL
        _cardsInHand.value = arrayListOf()
        handOfOthers.forEach {
            it.clear()
        }

        cardSequence = (0..53).toMutableList()
        cardSequence.shuffle()

        val currentTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        _history = GameHistory(startTime = currentTime.format(formatter))
    }

    private fun roundEnd(): Int {
        return (dealingEnd - dealingBegin) / 4
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

    fun startPlayPhase() {
        _gamePhase.value = GamePhase.PLAY
        runMainLoop()
    }

    fun selectCardInHand(cardInt: Int) {
        selectedCardInts.add(cardInt)
    }

    fun deselectCardInHand(cardInt: Int) {
        selectedCardInts.remove(cardInt)
    }

    private val _cardsToPlay = listOf(
        MutableLiveData<Int>(), MutableLiveData<Int>(),
        MutableLiveData<Int>(), MutableLiveData<Int>()
    )
    val cardsToPlay: List<LiveData<Int>> = _cardsToPlay

    var cardsPlayedDeferred = mutableListOf(
        CompletableDeferred<Int>(), CompletableDeferred<Int>(),
        CompletableDeferred<Int>(), CompletableDeferred<Int>()
    )

    private fun runMainLoop() {
        viewModelScope.launch(Dispatchers.Default) {
            for (round in 0 until roundEnd()) {
                var isDoClearCards = true
                for (player_i in 0 until 4) {
                    _whoseTurn = player_i
                    if (player_i != 0) AIPlay(player_i)
                    val cardInt = cardsPlayedDeferred[player_i].await()
                    if (isDoClearCards) {
                        _clearPlayedCards.postValue(true)
                        isDoClearCards = false
                    }
                    _history.rounds(player_i).add(cardInt)
                    _cardsToPlay[player_i].postValue(cardInt)
                    cardsPlayedDeferred[player_i] = CompletableDeferred()
                }
            }
            _gamePhase.postValue(GamePhase.END)
            _isGameFinished.postValue(true)
        }
    }

    private suspend fun AIPlay(player_i: Int) {
        delay(500)
        val card = handOfOthers[player_i - 1].removeFirst()
        cardsPlayedDeferred[player_i].complete(card.value)
    }

    fun HumanPlay(): String? {
        return when {
            selectedCardInts.size == 1 -> {
                _cardsInHand.value!!.removeIf {
                    it.value in selectedCardInts
                }
                _cardsInHand.value = _cardsInHand.value
                val card = selectedCardInts.first()
                selectedCardInts.clear()
                cardsPlayedDeferred[0].complete(card)
                null
            }
            selectedCardInts.isNotEmpty() -> "select only ONE card!"
            else -> "no card selected!"
        }
    }
}