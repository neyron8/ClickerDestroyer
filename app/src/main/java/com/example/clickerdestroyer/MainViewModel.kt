package com.example.clickerdestroyer

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.clickerdestroyer.data.Creature
import com.example.clickerdestroyer.data.Player
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    private var creature = mutableStateOf(Creature())
    private var player = mutableStateOf(Player("Jacko"))

    fun getMonster() = creature

    fun getPlayer() = player

    private fun changeMonster() {
        creature.value.apply {
            name = "Jerry"
            reward = 300
            hp = 31
        }
    }

    private fun killMonster() {
        player.value.money += creature.value.reward
        changeMonster()
    }

    fun attack(){
        if ((creature.value.hp <= 0) || ((creature.value.hp - player.value.damage) <= 0)) {
            killMonster()
        } else {
            creature.value.hp -= player.value.damage
        }
    }
}