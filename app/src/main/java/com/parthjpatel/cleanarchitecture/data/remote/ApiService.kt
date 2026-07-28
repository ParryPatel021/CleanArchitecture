package com.parthjpatel.cleanarchitecture.data.remote

import com.parthjpatel.cleanarchitecture.data.model.PixabayResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("api/")
    suspend fun getImages(
        @Query("key") key: String = "40308333-07c19e899666cb68334ed3a46",
        @Query("q") q: String,
    ): PixabayResponse
}