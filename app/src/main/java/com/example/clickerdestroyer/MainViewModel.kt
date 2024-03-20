package com.example.clickerdestroyer

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clickerdestroyer.data.Creature
import com.example.clickerdestroyer.data.ImagesOfMonsters
import com.example.clickerdestroyer.data.Player
import com.example.clickerdestroyer.db.MainDb
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val mainDb: MainDb) : ViewModel() {
    private val _creature = MutableStateFlow(Creature())
    val creature = _creature.asStateFlow()
    //var creature = mutableStateOf(Creature())

    private val _player = MutableStateFlow(Player("Jacko"))
    val player = _player.asStateFlow()

    var loading = mutableStateOf(false)
    private var randomChance = 0

    var openDialog = mutableStateOf(false)

    fun updatePlayer(player: Player) {
        _player.value = player
    }

    suspend fun changeLoadingState() {
        delay(1500)
        loading.value = false
    }

    fun getRandomReward() {
        _player.value.money += (2000..5000).random()
    }

    private fun changeMonster() {
        _creature.value.apply {
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
            _player.value = mainDb.dao.getDataPlayer(name)
            _creature.value = mainDb.dao.getDataCreature()
        }
    }

    private fun killMonster() {
        _player.value.money += creature.value.reward
        insertDataPlayer(_player.value)
        changeMonster()
        insertDataCreature(_creature.value)
        randomChance = (0..100).random()
        if (randomChance in 80..100)
            openDialog.value = true
        //Log.d("KILL", "Killed")
    }

    fun attack() {
        if ((_creature.value.hp <= 0) || ((_creature.value.hp - player.value.damage) <= 0)) {
            killMonster()
        } else {
            _creature.value.hp -= _player.value.damage
        }
    }

    fun upgradeDamage(k: Int) {
        _player.value.money -= k * _player.value.damage
        _player.value.damage += k
        Log.d("Money", _player.value.money.toString())
        Log.d("Damage", _player.value.damage.toString())
    }
}