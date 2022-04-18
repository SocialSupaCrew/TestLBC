package com.testlbc.data.repository.remote

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET


interface SongService {
    @GET("/img/shared/technical-test.json")
    suspend fun get(): List<SongJson>
}

data class SongJson(
    @SerializedName("albumId") val albumId: Int,
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("url") val url: String,
    @SerializedName("thumbnailUrl") val thumbnailUrl: String
)
