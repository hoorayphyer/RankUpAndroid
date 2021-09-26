package com.example.rankupandroid.network

import com.squareup.moshi.Json

data class RemotePlayerInfo(
    @Json(name = "id") val account_id: Long,
    private val name: String,
    @Json(name = "username") val nickname: String,
    private val email: String,
    private val address: AddressInfo,
    private val phone: String,
    private val website: String,
    private val company: CompanyInfo
) {
    data class AddressInfo(
        val street: String,
        val suite: String,
        val city: String,
        val zipcode: String,
        val geo: GeoInfo
    ) {
        data class GeoInfo(
            val lat: String,
            val lng: String
        )
    }

    data class CompanyInfo(
        val name: String,
        val catchPhrase: String,
        val bs: String
    )
}