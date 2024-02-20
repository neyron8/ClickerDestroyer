package com.example.clickerdestroyer

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.clickerdestroyer.data.Creature
import com.example.clickerdestroyer.data.Player


enum class BounceState { Pressed, Released }
@Composable
fun MainContent(viewModel: MainViewModel = hiltViewModel())
{
    viewModel.getData("Jacko")
    val monster = viewModel.creature.value
    val player = viewModel.player.value
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        PlayerInfo(player = player)
        Box(modifier = Modifier.height(400.dp),contentAlignment = Alignment.BottomCenter){
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
            Box {
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
                            })
                    MobInfo(monster)
                }
            }
        }
    }
}

@Composable
private fun MobInfo(monster: Creature) {
    Text(monster.name, textAlign = TextAlign.Center)
    Text(monster.hp.toString(), textAlign = TextAlign.Center)
}

@Composable
private fun PlayerInfo(player: Player) {
    Box(contentAlignment = Alignment.TopCenter){
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = player.name)
            Text(text = player.money.toString())
        }
    }
}
