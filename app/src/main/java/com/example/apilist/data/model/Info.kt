package com.example.apilist.data.model

data class Info(
    val total: Int,
    val page: Int,
    val limit: Int,
    val next: String,
    val prev: String?
)

