package com.toqsoft.livescore.data.repository

import com.toqsoft.livescore.data.api.ApiService
import com.toqsoft.livescore.domain.model.CricketScore
import retrofit2.Response
import javax.inject.Inject

class CricketRepository @Inject constructor(private val api: ApiService) {

    suspend fun getLiveMatches(apiKey: String): Response<List<CricketScore>> {
        val allMatches = mutableListOf<CricketScore>()
        var offset = 0
        var totalRows = 1

        while (offset < totalRows) {
            val response = api.getCurrentMatches(apiKey, offset)
            if (response.isSuccessful) {
                val body = response.body()
                totalRows = body?.info?.totalRows ?: 0
                allMatches.addAll(body?.data ?: emptyList())
                offset += body?.data?.size ?: 0
            } else {
                throw Exception("HTTP ${response.code()}: ${response.message()}")
            }
        }

        return Response.success(allMatches)
    }
}
