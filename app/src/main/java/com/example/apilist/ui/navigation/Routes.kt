package com.example.apilist.ui.navigation


import kotlinx.serialization.Serializable

sealed class Destinations {
    @Serializable
    object Home: Destinations()

    @Serializable
    object Favorites: Destinations()

    @Serializable
    object Settings: Destinations()

    @Serializable
    data class Pantalla11(val myParameter: String)
}