package com.example.bookhub.Navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.bookhub.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopBar(onInfoClick: () -> Unit, isInfoScreen: Boolean) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.app_name),
                color = androidx.compose.ui.res.colorResource(id = R.color.topBar)
            )
        },
        actions = {
            IconButton(onClick = onInfoClick) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Info Action",
                    tint = if (isInfoScreen) Color.White else Color.Gray // Fix: Use a boolean state
                )
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = Color.Black
        )
    )
}