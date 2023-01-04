package com.example.extraaedge.network

import com.example.extraaedge.di.utils.Status
import com.google.gson.JsonElement

class ApiResponse private constructor(
    status: Status,
    responce: JsonElement?,
    alert: String?,
    error: Throwable?
) {
    val status: Status
    val responce: JsonElement?
    val error: Throwable?

    companion object {
        fun loading(): ApiResponse {
            return ApiResponse(Status.LOADING, null, null,null)
        }

        fun success(responce: JsonElement): ApiResponse {
            return ApiResponse(Status.SUCCESS, responce, null,null)
        }

        fun error(error: Throwable): ApiResponse {
            return ApiResponse(Status.ERROR, null,null, error)
        }
    }

    init {
        this.status = status
        this.responce = responce
        this.error = error
    }
}