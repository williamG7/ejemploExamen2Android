package com.example.apilist.ui.navigation

import kotlinx.serialization.Serializable

sealed class Destinations {
    @Serializable
    object Home : Destinations()

    @Serializable
    object Favorites : Destinations()

    @Serializable
    data class DetailScreen(val characterUrl: String) : Destinations()
}