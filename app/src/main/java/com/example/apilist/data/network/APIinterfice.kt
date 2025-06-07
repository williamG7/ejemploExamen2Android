package com.example.apilist.data.network

import com.example.apilist.data.model.Data
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url

interface APIinterfice {

    @GET("character/")
    suspend fun getCharacters(): Response<Data>

    @GET
    suspend fun getCharacterById(@Url url: String): Response<com.example.apilist.data.model.Result>

    companion object {
        val BASE_URL = "https://rickandmortyapi.com/api/"
        fun create(): APIinterfice {
            val client = OkHttpClient.Builder().build()
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(APIinterfice::class.java)
        }
    }
}
