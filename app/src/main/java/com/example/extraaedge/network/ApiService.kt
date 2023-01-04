package com.example.extraaedge.network

import com.google.gson.JsonElement
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("rockets")
    fun getRocketList(): Single<JsonElement?>?

    @GET("rockets")
    fun getRocketDetails(
        @Query("id") id: String?
    ): Single<JsonElement?>?
}