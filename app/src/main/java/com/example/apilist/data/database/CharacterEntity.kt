package com.example.apilist.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CharacterEntity")
data class CharacterEntity(
    @PrimaryKey val id: Int,
    val created: String,
    val episode: List<String>,
    val gender: String,
    val image: String,
    val name: String,
    val species: String,
    val status: String,
    val type: String,
    val url: String
)
