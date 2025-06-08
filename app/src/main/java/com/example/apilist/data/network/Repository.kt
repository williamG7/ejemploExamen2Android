package com.example.apilist.data.network

import com.example.apilist.CharacterAplication
import com.example.apilist.data.database.CharacterEntity
import com.example.apilist.data.model.Character

class Repository {
    // Room Database Access
    val daoInterface = CharacterAplication.database.CharacterDAO()

    // API Service
    val apiInterface = APIinterface.create()

    // Database functions (se mantienen igual)
    suspend fun saveAsFavorite(character: CharacterEntity) = daoInterface.addCharacter(character)
    suspend fun deleteFavorite(character: CharacterEntity) = daoInterface.deleteCharacter(character)
    suspend fun isFavorite(characterId: Int) = daoInterface.getCharacterById(characterId)
    suspend fun getFavorites() = daoInterface.getAllCharacters()
    suspend fun deleteAllFavorites() = daoInterface.deleteAllCharacters()

    // API functions (adaptadas para Star Wars)
    suspend fun getAllCharacters(): List<com.example.apilist.data.model.Character> {
        return apiInterface.getAllCharacters().body() ?: emptyList()
    }

    suspend fun getCharacterByName(name: String): Character? {
        return apiInterface.getCharacterByName(name).body()
    }
}