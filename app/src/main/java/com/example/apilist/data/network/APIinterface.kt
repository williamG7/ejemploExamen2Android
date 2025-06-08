package com.example.apilist.data.network

import com.example.apilist.data.model.Character
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface APIinterface {

    @GET("api/v1/characters")
    suspend fun getAllCharacters(): Response<List<Character>>

    @GET("api/v1/characters/name/{name}")
    suspend fun getCharacterByName(@Path("name") name: String): Response<Character>

    companion object {
        private const val BASE_URL = "https://stavvars-databank-server.vercel.app/"

        fun create(): APIinterface {
            val client = OkHttpClient.Builder()
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(APIinterface::class.java)
        }
    }
}