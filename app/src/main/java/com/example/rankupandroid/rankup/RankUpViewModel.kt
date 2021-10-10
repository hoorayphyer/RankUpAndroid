package com.example.rankupandroid.rankup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RankUpViewModel : ViewModel() {
    private val _cardsInHand: MutableLiveData<List<Card>> = MutableLiveData<List<Card>>()
    val cardsInHand: LiveData<List<Card>> = _cardsInHand

    init {
        // TODO hard coded for tests
        _cardsInHand.value = listOf(Card(2), Card(2), Card(2), Card(2), Card(2))
    }
}