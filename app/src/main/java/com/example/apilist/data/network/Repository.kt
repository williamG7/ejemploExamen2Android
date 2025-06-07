package com.example.apilist.data.network

import com.example.apilist.CharacterAplication
import com.example.apilist.data.database.CharacterEntity

class Repository {

    val daoInterface = CharacterAplication.database.CharacterDAO()
    val apiInterface = APIinterfice.Companion.create()

    //Database functions
    suspend fun saveAsFavorite(character: CharacterEntity) = daoInterface.addCharacter(character)
    suspend fun deleteFavorite(character: CharacterEntity) = daoInterface.deleteCharacter(character)
    suspend fun isFavorite(characterId: Int) = daoInterface.getCharacterById(characterId)
    suspend fun getFavorites() = daoInterface.getAllCharacters()
    suspend fun deleteAllFavorites() = daoInterface.deleteAllCharacters()


    //API functions
    suspend fun getAllCharacters() = apiInterface.getCharacters()
    suspend fun getCharacter(characterUrl: String) = apiInterface.getCharacterById(characterUrl)

}