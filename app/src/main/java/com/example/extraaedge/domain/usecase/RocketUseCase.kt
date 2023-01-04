package com.example.extraaedge.domain.usecase

import android.content.Context
import com.example.extraaedge.domain.model.RocketDataList
import com.example.extraaedge.network.ApiService
import com.example.extraaedge.room.AppDatabase
import com.example.extraaedge.room.RocketDao
import com.google.gson.JsonElement
import dagger.Module
import io.reactivex.Single
import java.util.ArrayList
import javax.inject.Inject

class RocketUseCase @Inject internal constructor(
    apiService: ApiService,
    context: Context
) {
    private val apiService: ApiService
    private val context: Context

    fun getRocketList(): Single<JsonElement?>? {
        return apiService.getRocketList()
    }

    fun getRocketDetails(id: String?): Single<JsonElement?>? {
        return apiService.getRocketDetails(id)
    }

    init {
        this.apiService = apiService
        this.context = context
    }
}