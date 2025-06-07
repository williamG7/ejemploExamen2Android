package com.example.apilist

import android.app.Application
import androidx.room.Room
import com.example.apilist.data.database.CharacterDatabase

class CharacterAplication : Application() {
    companion object {
        lateinit var database: CharacterDatabase
    }

    override fun onCreate() {
        super.onCreate()
        database =
            Room.databaseBuilder(this, CharacterDatabase::class.java, "CharacterDatabase").build()
    }
}