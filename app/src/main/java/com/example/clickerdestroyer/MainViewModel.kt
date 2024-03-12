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


    private fun changeMonster() {
        creature.value.apply {
            name = "Jerry"
            reward = 300
            hp = 31
            imageOfMonster = ImagesOfMonsters().Images.random()
        }
    }

    fun insertData(player: Player) = viewModelScope.launch {
        mainDb.dao.insertData(player)
    }

    fun getData(name: String) = viewModelScope.launch {
        if (mainDb.dao.getData(name) == null) {
            insertData(
                player.value
            )
        } else {
            player.value = mainDb.dao.getData(name)
        }

    }

    private fun killMonster() {
        player.value.money += creature.value.reward
        insertData(player.value)
        changeMonster()
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