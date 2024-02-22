package com.example.clickerdestroyer

import android.annotation.SuppressLint
import android.net.Uri
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.example.clickerdestroyer.data.Creature
import com.example.clickerdestroyer.data.Player


enum class BounceState { Pressed, Released }

@SuppressLint("FlowOperatorInvokedInComposition")
@Composable
fun MainContent(
    viewModel: MainViewModel = hiltViewModel()
) {
    VideoPlayer(uri = Uri.parse("android.resource://ClickerDestroyer/" + R.raw.moon))
    viewModel.getData("Jacko")

    val monster = viewModel.creature.value
    val player = viewModel.player.value

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

    Column(verticalArrangement = Arrangement.SpaceBetween) {
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
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center){
            Button(onClick = { /*TODO*/ },) {
                
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
    //Text(text = player.name)
    Text(text = "Money: ${player.money}", color = Color.White)
}

@Composable
@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
fun VideoPlayer(uri: Uri) {
    val context = LocalContext.current

    val exoPlayer = remember {
        ExoPlayer.Builder(context)
            .build()
            .apply {
                val defaultDataSourceFactory = DefaultDataSource.Factory(context)
                val dataSourceFactory: DataSource.Factory = DefaultDataSource.Factory(
                    context,
                    defaultDataSourceFactory
                )
                val source = ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(MediaItem.fromUri(uri))

                setMediaSource(source)
                prepare()
            }
    }

    exoPlayer.playWhenReady = true
    exoPlayer.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
    exoPlayer.repeatMode = androidx.media3.common.Player.REPEAT_MODE_ONE

    DisposableEffect(
        AndroidView(factory = {
            PlayerView(context).apply {
                hideController()
                useController = false
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM

                player = exoPlayer
                layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
            }
        })
    ) {
        onDispose { exoPlayer.release() }
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

val gradientColors = listOf(
    Color.Red,
    Color.Magenta,
    Color.Blue,
    Color.Cyan,
    Color.Green,
    Color.Yellow,
    Color.Red
)

fun Modifier.drawRainbowBorder(
    strokeWidth: Dp,
    durationMillis: Int
) = composed {

    val infiniteTransition = rememberInfiniteTransition(label = "rotation")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "rotation"
    )

    val brush = Brush.sweepGradient(gradientColors)

    Modifier.drawWithContent {

        val strokeWidthPx = strokeWidth.toPx()
        val width = size.width
        val height = size.height

        drawContent()

        with(drawContext.canvas.nativeCanvas) {
            val checkPoint = saveLayer(null, null)

            // Destination
            drawRect(
                color = Color.Gray,
                topLeft = Offset(strokeWidthPx / 2, strokeWidthPx / 2),
                size = Size(width - strokeWidthPx, height - strokeWidthPx),
                style = Stroke(strokeWidthPx)
            )

            // Source
            rotate(angle) {
                drawCircle(
                    brush = brush,
                    radius = size.width,
                    blendMode = BlendMode.SrcIn,
                )
            }

            restoreToCount(checkPoint)
        }
    }
}