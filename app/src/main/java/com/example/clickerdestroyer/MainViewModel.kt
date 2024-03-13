package com.example.clickerdestroyer

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clickerdestroyer.data.Creature
import com.example.clickerdestroyer.data.ImagesOfMonsters
import com.example.clickerdestroyer.data.Player
import com.example.clickerdestroyer.db.MainDb
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val mainDb: MainDb) : ViewModel() {
    var creature = mutableStateOf(Creature())
    var player = mutableStateOf(Player("Jacko"))

    var openDialog = mutableStateOf(false)

    private fun changeMonster() {
        creature.value.apply {
            name = "Jerry"
            level++
            hp = level * 100
            reward = hp
            imageOfMonster = ImagesOfMonsters().Images.random()
        }
    }

    fun insertDataPlayer(player: Player) = viewModelScope.launch {
        mainDb.dao.insertDataPlayer(player)
    }

    private fun insertDataCreature(creature: Creature) = viewModelScope.launch {
        mainDb.dao.insertDataCreature(creature)
    }

    fun getData(name: String) = viewModelScope.launch {
        if (mainDb.dao.getDataPlayer(name) == null) {
            insertDataPlayer(player.value)
            insertDataCreature(creature.value)
        } else {
            player.value = mainDb.dao.getDataPlayer(name)
            creature.value = mainDb.dao.getDataCreature()
        }
    }

    private fun killMonster() {
        player.value.money += creature.value.reward
        insertDataPlayer(player.value)
        changeMonster()
        insertDataCreature(creature.value)
        openDialog.value = true
    }

    fun attack() {
        if ((creature.value.hp <= 0) || ((creature.value.hp - player.value.damage) <= 0)) {
            killMonster()
        } else {
            creature.value.hp -= player.value.damage
        }
    }

    fun upgradeDamage(k: Int) {
        player.value.money -= k * player.value.damage
        player.value.damage += k
    }
}