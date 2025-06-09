package com.example.apilist.data.model

import com.google.gson.annotations.SerializedName

data class Character(
    @SerializedName("_id")
    val id: String,
    val name: String,
    val description: String,
    @SerializedName("image")
    val image: String
)
