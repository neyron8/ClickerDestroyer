package com.example.clickerdestroyer.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.clickerdestroyer.data.Creature
import com.example.clickerdestroyer.data.Player

@Database(
    entities = [Player::class, Creature::class],
    version = 1
)

abstract class MainDb: RoomDatabase(){
    abstract val dao: DaoDb
}