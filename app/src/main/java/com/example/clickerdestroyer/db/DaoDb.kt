package com.example.clickerdestroyer.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.clickerdestroyer.data.Player

@Dao
interface DaoDb {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(film: Player)

    @Delete
    suspend fun deleteData(film: Player)

    @Query("SELECT * FROM player WHERE name = :name")
    suspend fun getData (name : String) : Player
}