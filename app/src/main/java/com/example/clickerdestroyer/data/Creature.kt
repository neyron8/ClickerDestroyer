package com.example.clickerdestroyer.data

import com.example.clickerdestroyer.R

data class Creature(
    var hp: Int = 100,
    var typeOfCreature: String = "Mob",
    var level: Int = 1,
    var name: String = "Simple $typeOfCreature",
    var reward: Int = 20,
    var imageOfMonster: Int = R.drawable.monster1
)
