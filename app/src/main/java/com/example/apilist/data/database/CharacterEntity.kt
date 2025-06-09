package com.example.apilist.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CharacterEntity")
data class CharacterEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val imageUrl: String
)