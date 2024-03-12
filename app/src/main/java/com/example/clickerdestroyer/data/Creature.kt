package com.example.clickerdestroyer.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.clickerdestroyer.R

@Entity(tableName = "creature")
data class Creature(
    var hp: Int = 100,
    @PrimaryKey(autoGenerate = false)
    var typeOfCreature: String = "Mob",
    var level: Int = 1,
    var name: String = "Simple $typeOfCreature",
    var reward: Int = 20,
    var imageOfMonster: Int = R.drawable.monster1
)