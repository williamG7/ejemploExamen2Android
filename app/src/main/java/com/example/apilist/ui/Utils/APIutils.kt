package com.example.apilist.ui.Utils

fun getIdFromUrl(url: String, string: String): Int {
    val regex = """/(\d+)/?$""".toRegex()
    return regex.find(url)?.groupValues?.get(1)?.toInt()
        ?: throw IllegalArgumentException("URL no contiene un ID válido: $url")
}