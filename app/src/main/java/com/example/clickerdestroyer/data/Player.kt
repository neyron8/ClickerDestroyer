package com.example.clickerdestroyer.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "player")
data class Player (
    @PrimaryKey var name: String,
    var damage: Int = 5,
    var money:Int = 0
) : Parcelable