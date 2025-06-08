package com.example.apilist.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apilist.data.database.CharacterEntity
import com.example.apilist.data.model.Character
import com.example.apilist.data.network.Repository
import com.example.apilist.data.network.SettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class APIviewmodel(private val repository1: SettingsRepository) : ViewModel() {
    val repository = Repository()

    // LiveData para el estado de carga
    private val _loading = MutableLiveData(true)
    val loading: LiveData<Boolean> = _loading

    // LiveData para la lista de personajes
    private val _characters = MutableLiveData<List<Character>>()
    val characters: LiveData<List<Character>> = _characters

    // LiveData para los favoritos
    private val _favorites = MutableLiveData<List<CharacterEntity>>()
    val favorites: LiveData<List<CharacterEntity>> = _favorites

    // LiveData para el personaje actual (detalle)
    private val _characterDetail = MutableLiveData<Character?>()
    val characterDetail: LiveData<Character?> = _characterDetail

    // LiveData para el estado de favorito
    private val _isFavorite = MutableLiveData(false)
    val isFavorite: LiveData<Boolean> = _isFavorite

    // LiveData para el modo de vista (Lista/Grid)
    private val _viewMode = MutableLiveData<String>("List")
    val viewMode: LiveData<String> = _viewMode

    // LiveData para mostrar toast
    private val _showToast = MutableLiveData(false)
    val showToast: LiveData<Boolean> = _showToast
    private var toastMessage = ""

    // LiveData para el tema oscuro
    private val _isDarkTheme = MutableLiveData(false)
    val isDarkTheme: LiveData<Boolean> = _isDarkTheme

    init {
        // Inicializar valores por defecto
        _viewMode.value = "List"
        _isDarkTheme.value = false
    }

    // Función para cambiar entre lista y grid
    fun setViewMode(mode: String) {
        _viewMode.value = mode
    }

    // Función para cambiar el tema
    fun onToggleTheme(isDark: Boolean) {
        _isDarkTheme.value = isDark
    }

    // Función para obtener todos los personajes (Star Wars)
    fun getCharacters() {
        viewModelScope.launch {
            try {
                _loading.value = true
                val response = withContext(Dispatchers.IO) {
                    repository.getAllCharacters()
                }
                _characters.value = response
                _loading.value = false
            } catch (e: Exception) {
                Log.e("APIviewmodel", "Error fetching characters", e)
                _loading.value = false
            }
        }
    }

    // Función para obtener un personaje por nombre (Star Wars)
    fun getCharacterByName(name: String) {
        viewModelScope.launch {
            try {
                _loading.value = true
                val character = withContext(Dispatchers.IO) {
                    repository.getCharacterByName(name)
                }
                _characterDetail.value = character

                character?.id?.let { id ->
                    val isFav = withContext(Dispatchers.IO) {
                        repository.isFavorite(id)
                    }
                    _isFavorite.value = (isFav != null)
                }

                _loading.value = false
            } catch (e: Exception) {
                Log.e("APIviewmodel", "Error fetching character", e)
                _loading.value = false
            }
        }
    }

    // Función para obtener favoritos
    fun getFavorites() {
        viewModelScope.launch {
            try {
                _loading.value = true
                val favs = withContext(Dispatchers.IO) {
                    repository.getFavorites()
                }
                _favorites.value = favs
                _loading.value = false
            } catch (e: Exception) {
                Log.e("APIviewmodel", "Error fetching favorites", e)
                _loading.value = false
            }
        }
    }

    // Función para alternar favorito
    fun toggleFavorite() {
        viewModelScope.launch {
            try {
                val character = _characterDetail.value ?: return@launch
                val characterEntity = CharacterEntity(
                    id = character.id,
                    name = character.name,
                    description = character.description,
                    imageUrl = character.imageUrl
                )

                if (_isFavorite.value == true) {
                    withContext(Dispatchers.IO) {
                        repository.deleteFavorite(characterEntity)
                    }
                    toastMessage = "Eliminado de favoritos"
                } else {
                    withContext(Dispatchers.IO) {
                        repository.saveAsFavorite(characterEntity)
                    }
                    toastMessage = "Añadido a favoritos"
                }

                _isFavorite.value = !_isFavorite.value!!
                _showToast.value = true
            } catch (e: Exception) {
                Log.e("APIviewmodel", "Error toggling favorite", e)
                toastMessage = "Error al actualizar favoritos"
                _showToast.value = true
            }
        }
        _showToast.value = false
    }

    // Función para eliminar todos los favoritos
    fun deleteAllFavorites() {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    repository.deleteAllFavorites()
                }
                _favorites.value = emptyList()
            } catch (e: Exception) {
                Log.e("APIviewmodel", "Error deleting all favorites", e)
            }
        }
    }

    // Función para obtener el mensaje del toast
    fun getToastMessage(): String {
        return toastMessage
    }
}