package com.betrybe.currencyview.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiServiceClient {

    const val BASE_URL = "https://api.apilayer.com/exchangerates_data/"
    const val apiKey: String = "7ndBJZ5bSoD3CNW7p7IxVIDoQugisvgN"

    val instance: ApiService by lazy {
        val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ApiService::class.java)
    }

}