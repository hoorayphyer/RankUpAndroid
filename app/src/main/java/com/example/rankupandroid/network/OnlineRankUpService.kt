package com.example.rankupandroid.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// this is using a fake json online database which has a users sector
private val BASE_URL = "https://jsonplaceholder.typicode.com/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit_service = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    // TODO a comment to the line above from the reviewer
    // You can now also use the KotlinX Serialisation library(https://github.com/Kotlin/kotlinx.serialization)
    // which is in the stable release.
    // This library supports JSON deserialisation without Reflection based lookups and
    // hence can save a lot of memory and time for large projects and also supports MultiPlatform.
    // The syntax is fairly easy but very powerful when deserialising large JSON objects
    .baseUrl(BASE_URL)
    .build()

// this is a Retrofit interface object
interface OnlineRankUpService {
    // return a List because the top level json is a json array
    @GET("users")
    suspend fun queryPlayers(): List<RemotePlayerInfo>
}


object RankUpRemote {
    val service: OnlineRankUpService by lazy {
        retrofit_service.create(OnlineRankUpService::class.java)
    }
}