package com.example.clickerdestroyer.view

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.clickerdestroyer.MainViewModel
import com.example.clickerdestroyer.R
import com.example.clickerdestroyer.data.Creature
import com.example.clickerdestroyer.data.Player


enum class BounceState { Pressed, Released }

@SuppressLint("FlowOperatorInvokedInComposition")
@Composable
fun MainContent(
    viewModel: MainViewModel = hiltViewModel(), navController: NavController,
) {
    VideoPlayer(uri = Uri.parse("android.resource://ClickerDestroyer/" + R.raw.moon))
    viewModel.getData("Jacko")

    val monster = viewModel.creature.value
    var player = viewModel.player.value

    val player_mod =
        navController.currentBackStackEntry?.savedStateHandle?.get<Player>("Player_mod")

    if (player_mod != null) {
        player = player_mod
        viewModel.insertData(player)
    }

    var currentState: BounceState by remember { mutableStateOf(BounceState.Released) }
    val transition = updateTransition(targetState = currentState, label = "animation")
    val scale: Float by transition.animateFloat(
        transitionSpec = { spring(stiffness = 900f) }, label = ""
    ) { state ->
        // 0.75f размер картинки во время нажатия
        if (state == BounceState.Pressed) {
            0.75f
        } else {
            //когда отпускаешь нажатие, картинка возвращает свой размер
            1f
        }
    }

    Column(verticalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxSize()) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.End,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    20.dp
                )
        ) {
            PlayerInfo(player = player)
        }
        Box(modifier = Modifier
            .fillMaxWidth()
            , contentAlignment = Alignment.Center,) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = R.drawable.monster),
                    contentDescription = "gfg",
                    modifier = Modifier
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                        }
                        .pointerInput(Unit) {
                            detectTapGestures(onPress = {
                                viewModel.attack()
                                // Устанавливает текущее состояние на Нажатое,
                                // чтобы затриггерить анимацию нажатия
                                currentState = BounceState.Pressed

                                // Ожидает отжатия кнопки, чтобы изменить сотояние на Отжатое
                                tryAwaitRelease()

                                currentState = BounceState.Released
                            })
                        }
                )
                MobInfo(monster = monster)
            }
        }
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Button(
                onClick = {
                    navController.currentBackStackEntry?.savedStateHandle?.set(
                        key = "Player",
                        value = viewModel.player.value
                    )
                    navController.navigate("Shop")
                },
            ) {
            }
        }
    }
}

@Composable
private fun MobInfo(monster: Creature) {
    Text(
        monster.hp.toString(),
        textAlign = TextAlign.Center,
        color = Color.White,
        modifier = Modifier
            .padding(10.dp)
    )
}

@Composable
private fun PlayerInfo(player: Player) {
    Text(text = player.damage.toString())
    Text(text = "Money: ${player.money}", color = Color.White)
}

@Preview
@Composable
fun TestFor() {
    var currentState: BounceState by remember { mutableStateOf(BounceState.Released) }
    val transition = updateTransition(targetState = currentState, label = "animation")
    val scale: Float by transition.animateFloat(
        transitionSpec = { spring(stiffness = 900f) }, label = ""
    ) { state ->
        // 0.75f размер картинки во время нажатия
        if (state == BounceState.Pressed) {
            0.75f
        } else {
            //когда отпускаешь нажатие, картинка возвращает свой размер
            1f
        }
    }
    VideoPlayer(uri = Uri.parse("android.resource://ClickerDestroyer/" + R.raw.moon))
    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.End,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    20.dp
                )
        ) {
            Text(text = "moneyd")
            Text(text = "money2d")
        }
        Column(modifier = Modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .pointerInput(Unit) {
                detectTapGestures(onPress = {
                    // Устанавливает текущее состояние на Нажатое,
                    // чтобы затриггерить анимацию нажатия
                    currentState = BounceState.Pressed

                    // Ожидает отжатия кнопки, чтобы изменить сотояние на Отжатое
                    tryAwaitRelease()

                    currentState = BounceState.Released
                })
            }
            .fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = R.drawable.monster),
                    contentDescription = "gfg"
                )
                Text("sdasd")
                Text("dssd")
            }

        }
        Text("233123")
    }
}

@Composable
fun Shop(navController: NavController, viewModel: MainViewModel = hiltViewModel()) {
    val player =
        navController.previousBackStackEntry?.savedStateHandle?.get<Player>("Player")
    Column {
        if (player != null) {
            Button(onClick = {
                player.damage = player.damage + 15
                Log.d("damage", viewModel.player.value.money.toString())
            }) {
                Text("+15")
            }
            Text("Player damage and money is ${player.damage} ${player.money}")
        }
        Button(onClick = {
            navController.previousBackStackEntry
                ?.savedStateHandle
                ?.set("Player_mod", player)
            navController.popBackStack()
        }) {
            Text(text = "Back")
        }
    }

}