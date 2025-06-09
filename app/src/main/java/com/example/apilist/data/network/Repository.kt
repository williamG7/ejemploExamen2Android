package com.example.apilist.data.network

import com.example.apilist.CharacterAplication
import com.example.apilist.data.database.CharacterEntity
import com.example.apilist.data.model.Character

class Repository {

    val daoInterface = CharacterAplication.database.CharacterDAO()

    val apiInterface = APIinterface.create()

    suspend fun saveAsFavorite(character: CharacterEntity) = daoInterface.addCharacter(character)
    suspend fun deleteFavorite(character: CharacterEntity) = daoInterface.deleteCharacter(character)
    suspend fun isFavorite(characterId: String) = daoInterface.getCharacterById(characterId)
    suspend fun getFavorites() = daoInterface.getAllCharacters()
    suspend fun deleteAllFavorites() = daoInterface.deleteAllCharacters()

    suspend fun getAllCharacters(): List<Character> {
        val response = apiInterface.getAllCharacters()
        if (response.isSuccessful) {
            return response.body()?.data ?: emptyList()
        } else {
            throw Exception("Error al obtener personajes: ${response.code()}")
        }
    }

    suspend fun getCharacterByName(name: String): Character? {
        return apiInterface.getCharacterByName(name).body()
    }
}