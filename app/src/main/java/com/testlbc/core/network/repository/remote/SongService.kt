package com.testlbc.core.network.repository.remote

import com.google.gson.annotations.SerializedName
import io.reactivex.Single
import retrofit2.http.GET


interface SongService {
    @GET("/img/shared/technical-test.json")
    fun get(): Single<List<SongJson>>
}

data class SongJson(
    @SerializedName("albumId") val albumId: Int,
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("url") val url: String,
    @SerializedName("thumbnailUrl") val thumbnailUrl: String
)
