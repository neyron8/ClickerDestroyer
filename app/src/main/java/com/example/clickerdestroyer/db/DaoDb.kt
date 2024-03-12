package com.example.clickerdestroyer.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.clickerdestroyer.data.Creature
import com.example.clickerdestroyer.data.Player

@Dao
interface DaoDb {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDataPlayer(player: Player)

    @Delete
    suspend fun deleteDataPlayer(player: Player)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDataCreature(creature: Creature)

    @Delete
    suspend fun deleteDataCreature(creature: Creature)

    @Query("SELECT * FROM player WHERE name = :name")
    suspend fun getDataPlayer(name: String): Player

    @Query("SELECT * FROM creature")
    suspend fun getDataCreature(): Creature
}