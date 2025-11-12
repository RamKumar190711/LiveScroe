package com.toqsoft.livescore.data.api

import com.toqsoft.livescore.domain.model.CurrentMatchesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("v1/currentMatches")
    suspend fun getCurrentMatches(
        @Query("apikey") apiKey: String,
        @Query("offset") offset: Int = 0
    ): Response<CurrentMatchesResponse>
}
