package com.example.apilist.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.apilist.data.database.CharacterEntity
import com.example.apilist.data.model.Data
import com.example.apilist.data.model.Result
import com.example.apilist.data.network.Repository
import com.example.apilist.data.network.SettingsRepository
import com.example.apilist.ui.Utils.getIdFromUrl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class APIviewmodel(repository1: SettingsRepository) : ViewModel() {

    private val repositorio = repository1

    private val repository = Repository()

    private val _loading = MutableLiveData(true)
    val loading = _loading

    private val _characters = MutableLiveData<Data>()
    val characters = _characters

    private val _favorites = MutableLiveData<MutableList<CharacterEntity>>()
    val favorites = _favorites

    private val _actualCharacter = MutableLiveData<Result>()
    val actualCharacter = _actualCharacter

    private val _isFavorite = MutableLiveData(false)
    val isFavorite = _isFavorite

    private val _viewMode = MutableLiveData<String>("List") //Estado inicial
    val viewMode = _viewMode

    private val _showToast = MutableLiveData(false)
    val showToast = _showToast
    private var toastMessage = ""

    private val _isDarkTheme = MutableLiveData(false)
    val isDarkTheme: LiveData<Boolean> = _isDarkTheme

    //Inicializa el valor de viewMode y isDarkTheme desde SharedPreferences
    init {
        _viewMode.value = repositorio.getSettingValue("viewMode", "List")
        _isDarkTheme.value = repositorio.getSettingValue("isDarkTheme", false)
    }

    //Funcion para cambiar de list a grid
    fun setViewMode(mode: String) {
        viewMode.value = mode
        repositorio.saveSettingValue("viewMode", mode)
    }

    //Funcion para cambiar el tema
    fun onToggleTheme(isDark: Boolean) {
        _isDarkTheme.value = isDark
        repositorio.saveSettingValue("isDarkTheme", isDark)
    }

    fun getCharacters() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getAllCharacters()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    _characters.value = response.body()
                    _loading.value = false
                } else {
                    Log.e("Error:", response.message())
                }
            }
        }
    }

    fun getCharacter(characterUrl: String) {
        val endPoint = characterUrl.removePrefix("https://rickandmortyapi.com/api/")
        viewModelScope.launch {
            _loading.value = true
            val response = withContext(Dispatchers.IO) {
                repository.getCharacter(endPoint)
            }
            if (response.isSuccessful) {
                val character = response.body()
                _actualCharacter.value = character!!
                val characterId = getIdFromUrl(_actualCharacter.value!!.url, "/")
                val favorite = withContext(Dispatchers.IO) {
                    repository.isFavorite(characterId.toInt())
                }
                Log.d("ES FAVORITO?", favorite.toString())
                if (favorite == null) {
                    _isFavorite.value = false
                } else {
                    _isFavorite.value = true
                }
            } else {
                Log.e("Error:", response.message())
            }
            _loading.value = false
        }
    }

    fun getFavorites() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getFavorites()
            withContext(Dispatchers.Main) {
                _favorites.value = response
                _loading.value = false
            }
        }
    }

    fun saveFavorite() {
        viewModelScope.launch(Dispatchers.IO) {
            val character = _actualCharacter.value
            if (character != null) {
                val characterId = getIdFromUrl(character.url, "/")
                val fav = repository.isFavorite(characterId)
                if (fav == null) {
                    repository.saveAsFavorite(
                        CharacterEntity(
                            id = characterId,
                            created = character.created,
                            episode = character.episode,
                            gender = character.gender,
                            image = character.image,
                            name = character.name,
                            species = character.species,
                            status = character.status,
                            type = character.type,
                            url = character.url
                        )
                    )
                    withContext(Dispatchers.Main) {
                        toastMessage = "Añadir a favoritos"
                        _showToast.value = true
                        _isFavorite.value = true
                    }
                } else {
                    repository.deleteFavorite(fav)
                    withContext(Dispatchers.Main) {
                        toastMessage = "Eliminar de favoritos"
                        _showToast.value = true
                        _isFavorite.value = false
                    }
                }
            }
        }
        _showToast.value = false
    }

    fun deleteAllFavorites() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllFavorites()
            withContext(Dispatchers.Main) {
                _favorites.value = mutableListOf() //Deja la lista de favoritos vacia
            }
        }
    }

    fun getToastMessage(): String {
        return toastMessage
    }
}