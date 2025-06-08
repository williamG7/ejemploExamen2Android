package com.example.apilist.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CharacterEntity")
data class CharacterEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val description: String,
    val imageUrl: String,
    val species: String? = null,
    val homeworld: String? = null
)