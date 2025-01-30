package com.example.bookhub.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookhub.R

@Composable
fun InfoScreen() {
    Column(
        modifier = Modifier.background(Color.Black)
            .padding(26.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())

    ){
        Text(
            text = stringResource(R.string.about_app),
            color = Color.White,
            fontSize = 16.sp,
            modifier = Modifier.padding(vertical = 20.dp)
        )
    }
}