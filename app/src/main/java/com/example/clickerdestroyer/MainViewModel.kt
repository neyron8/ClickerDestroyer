package com.example.clickerdestroyer

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.clickerdestroyer.data.Creature
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    private val creature = mutableStateOf(Creature())

    fun getMonster() = creature
}