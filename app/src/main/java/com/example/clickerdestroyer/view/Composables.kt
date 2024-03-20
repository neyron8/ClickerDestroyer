package com.example.clickerdestroyer.view

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.clickerdestroyer.MainViewModel
import com.example.clickerdestroyer.R
import com.example.clickerdestroyer.data.Creature
import com.example.clickerdestroyer.data.Player


enum class BounceState { Pressed, Released }

@SuppressLint("FlowOperatorInvokedInComposition")
@Composable
fun MainContent(
    viewModel: MainViewModel, navController: NavController,
) {
    VideoPlayer(uri = Uri.parse("android.resource://ClickerDestroyer/" + R.raw.moon))

    val monster = viewModel.creature.value
    var player = viewModel.player.value

    val openDialogCustom = viewModel.openDialog

    val playerMod =
        navController.currentBackStackEntry?.savedStateHandle?.get<Player>("Player_mod")

    playerMod?.let {
        player = playerMod
        viewModel.insertDataPlayer(player)
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

    if (openDialogCustom.value) {
        DialogReward(openDialogCustom = openDialogCustom, mainViewModel = viewModel)
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
            Text(viewModel.player.value.money.toString())
        }
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = monster.imageOfMonster),
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
                Text("Shop")
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
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            modifier = Modifier.size(20.dp),
            painter = painterResource(id = R.drawable.money),
            contentDescription = "moneyIcon"
        )
        Text(fontSize = 20.sp, text = "${player.money}", color = Color.White)
    }
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
            .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = R.drawable.monster5),
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
fun Shop(navController: NavController, mainViewModel: MainViewModel) {
    VideoPlayer(uri = Uri.parse("android.resource://ClickerDestroyer/" + R.raw.moon))
    val player = mainViewModel.player.value

    var damage by remember {
        mutableIntStateOf(player.damage)
    }
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            modifier = Modifier.fillMaxSize(0.5f),
            painter = painterResource(id = R.drawable.raccoon_keeper),
            contentDescription = "Shop Keeper"
        )
        Text(text = "Money: ${player.money}")
        Text(text = "Damage: $damage")
        Button(onClick = {
            if (player.money > (player.damage * 5)) {
                mainViewModel.upgradeDamage(5)
                damage = player.damage
            }
        }) {
            Text("+5 cost: ${(player.damage * 5)}")
        }
        Button(onClick = {
            if (player.money > (player.damage * 15)) {
                mainViewModel.upgradeDamage(15)
                damage = player.damage
            }
        }) {
            Text("+15 cost: ${(player.damage * 15)}")
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