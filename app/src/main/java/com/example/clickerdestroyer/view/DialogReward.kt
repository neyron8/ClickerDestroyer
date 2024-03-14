package com.example.clickerdestroyer.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.clickerdestroyer.MainViewModel
import com.example.clickerdestroyer.R
import com.example.clickerdestroyer.ui.theme.Purple40
import kotlinx.coroutines.launch

@Composable
fun DialogReward(openDialogCustom: MutableState<Boolean>, mainViewModel: MainViewModel) {
    Dialog(onDismissRequest = { openDialogCustom.value = false }) {
        CustomDialogUI(mainViewModel = mainViewModel)
    }
}

@Composable
fun CustomDialogUI(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.padding(10.dp, 5.dp, 10.dp, 10.dp)
    ) {
        Column(
            modifier
                .background(Color.White)
        ) {

            //.......................................................................
            Image(
                painter = painterResource(id = R.drawable.treasure),
                contentDescription = null, // decorative
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(
                    color = Purple40
                ),
                modifier = Modifier
                    .padding(top = 35.dp)
                    .height(70.dp)
                    .fillMaxWidth(),

                )

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Congratulations! \n Now you gonna have bonus reward",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.labelLarge,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Test your luck",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 10.dp, start = 25.dp, end = 25.dp)
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.bodyMedium
                )
                IndeterminateCircularIndicator(mainViewModel)
            }
        }
    }
}

@Composable
fun IndeterminateCircularIndicator(mvm: MainViewModel) {
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Button(
            onClick = {
                mvm.loading.value = true
                scope.launch {
                    mvm.changeLoadingState()
                    mvm.getRandomReward()
                    mvm.openDialog.value = false
                }
            },
            enabled = !mvm.loading.value,
        ) {
            Text("Start loading")
        }

        if (!mvm.loading.value) return

        CircularProgressIndicator(
            modifier = Modifier.width(48.dp),
            color = MaterialTheme.colorScheme.secondary,
        )
    }
}
