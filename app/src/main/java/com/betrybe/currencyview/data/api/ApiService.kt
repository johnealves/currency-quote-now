package com.betrybe.currencyview.data.api

import com.betrybe.currencyview.data.models.CurrencyRateResponse
import com.betrybe.currencyview.data.models.CurrencySymbolResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiService {

    @Headers("apiKey: ${ApiServiceClient.apiKey}")
    @GET("symbols")
    suspend fun getSymbol(): Response<CurrencySymbolResponse>

    @Headers("apiKey: ${ApiServiceClient.apiKey}")
    @GET("latest")
    suspend fun getRate(
        @Query("base") base: String?,
        @Query("symbols") symbols: String? = "USD, EUR, BRL, JPY, GBP, AUD, CAD, CHF, CNY, HKD, NZD"
    ): Response<CurrencyRateResponse>

}