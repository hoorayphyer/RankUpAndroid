package com.example.rankupandroid.network

import com.squareup.moshi.Json

data class RankUpPlayerJson(
    val players: List<IndividualPlayerInfo>
) {
    data class IndividualPlayerInfo(
        @Json(name = "id") val account_id: Long,
        private val name: String,
        @Json(name = "username") val nickname: String,
        private val email: String,
        private val address: String,
        private val phone: String,
        private val website: String,
        private val company: String
    ) {
    }
}