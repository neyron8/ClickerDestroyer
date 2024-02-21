package com.example.clickerdestroyer

import android.annotation.SuppressLint
import android.net.Uri
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
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
    viewModel: MainViewModel = hiltViewModel())
{
    VideoPlayer(uri = Uri.parse("android.resource://ClickerDestroyer/" + R.raw.moon))
    viewModel.getData("Jacko")
    
    val monster = viewModel.creature.collectAsState()
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

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        PlayerInfo(player = player)
        Box(modifier = Modifier.height(400.dp).graphicsLayer {
            scaleX = scale
            scaleY = scale
        }.pointerInput(Unit) {
                detectTapGestures(onPress = {
                    viewModel.attack()
                    // Устанавливает текущее состояние на Нажатое,
                    // чтобы затриггерить анимацию нажатия
                    currentState = BounceState.Pressed

                    // Ожидает отжатия кнопки, чтобы изменить сотояние на Отжатое
                    tryAwaitRelease()

                    currentState = BounceState.Released
                })
            },contentAlignment = Alignment.BottomCenter){
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = R.drawable.monster),
                    contentDescription = "gfg")
            }
        }
        MobInfo(monster.value)
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
