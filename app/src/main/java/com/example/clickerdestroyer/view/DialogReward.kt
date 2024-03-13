package com.example.clickerdestroyer.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.clickerdestroyer.MainViewModel
import com.example.clickerdestroyer.R
import com.example.clickerdestroyer.ui.theme.Purple40
import com.example.clickerdestroyer.ui.theme.Purple80

@Composable
fun DialogReward(openDialogCustom: MutableState<Boolean>, mainViewModel: MainViewModel) {
    Dialog(onDismissRequest = { openDialogCustom.value = false }) {
        CustomDialogUI(openDialogCustom = openDialogCustom, mainViewModel = mainViewModel)
    }
}

@Composable
fun CustomDialogUI(
    modifier: Modifier = Modifier,
    openDialogCustom: MutableState<Boolean>,
    mainViewModel: MainViewModel
) {
    Card(
        //shape = MaterialTheme.shapes.medium,
        shape = RoundedCornerShape(10.dp),
        // modifier = modifier.size(280.dp, 240.dp)
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
                androidx.compose.material3.Text(
                    text = "Congratulations! \n Now you gonna have bonus reward",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.labelLarge,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                androidx.compose.material3.Text(
                    text = "Test your luck",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 10.dp, start = 25.dp, end = 25.dp)
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            //.......................................................................
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .background(Purple80),
                horizontalArrangement = Arrangement.SpaceAround
            ) {

                androidx.compose.material3.TextButton(onClick = {
                    mainViewModel.player.value.money += 900
                    //change later in other fun
                    //there will be animation
                    openDialogCustom.value = false
                }) {
                    androidx.compose.material3.Text(
                        "Roll",
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.Black,
                        modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)
                    )
                }
            }
        }
    }
}
/*
@SuppressLint("UnrememberedMutableState")
@Preview(name="Custom Dialog")
@Composable
fun MyDialogUIPreview(){
    CustomDialogUI(openDialogCustom = mutableStateOf(false))
}

 */