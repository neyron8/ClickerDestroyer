package com.example.clickerdestroyer.data

data class Creature(
    var hp: Int = 100,
    var typeOfCreature: String = "Mob",
    var level: Int = 1,
    var name: String = "Simple $typeOfCreature",
    var reward: Int = 20
)
