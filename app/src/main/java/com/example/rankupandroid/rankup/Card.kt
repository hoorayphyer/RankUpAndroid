package com.example.rankupandroid.rankup

data class Card(val value: Short) {
    init {
        require(value in 0..53) { "$value is out of the range of [0,54)" }
    }
}