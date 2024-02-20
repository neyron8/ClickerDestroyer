package com.example.clickerdestroyer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.clickerdestroyer.ui.theme.ClickerDestroyerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ClickerDestroyerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting()
                }
            }
        }
    }
}

enum class BounceState { Pressed, Released }
@Composable
fun Greeting(mainViewModel: MainViewModel = hiltViewModel())
{
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
    val monster = mainViewModel.getMonster()
    var player = mainViewModel.getPlayer()

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = player.value.name)
            Text(text = player.value.money.toString())
            Image(
                painter = painterResource(id = R.drawable.monster),
                contentDescription = "gfg",
                modifier = Modifier.graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }.pointerInput(Unit) {
                    detectTapGestures(onPress = {
                        mainViewModel.attack()
                        // Устанавливает текущее состояние на Нажатое,
                        // чтобы затриггерить анимацию нажатия
                        currentState = BounceState.Pressed

                        // Ожидает отжатия кнопки, чтобы изменить сотояние на Отжатое
                        tryAwaitRelease()

                        currentState = BounceState.Released
                    })
                })
            Text(monster.value.name, textAlign = TextAlign.Center)
            Text(monster.value.hp.toString(), textAlign = TextAlign.Center)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ClickerDestroyerTheme {
        Greeting()
    }
}

