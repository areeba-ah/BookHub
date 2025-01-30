package com.example.bookhub.Navigation
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MyBottomBar(selectedScreen: MutableState<NavItem>) {
    val navItems = listOf(NavItem.Library, NavItem.SavedBooks)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
            .padding(bottom = 28.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        navItems.forEach { item ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clickable { selectedScreen.value = item }
                    .padding(horizontal = 16.dp)
            ) {
                Image(
                    painter = item.icon(),
                    contentDescription = item.title,
                    modifier = Modifier.size(26.dp),
                    colorFilter = ColorFilter.tint(
                        if (item == selectedScreen.value) Color.White else Color.Gray
                    )
                )
                Text(
                    text = item.title,
                    color = if (item == selectedScreen.value) Color.White else Color.Gray,
                    fontSize = 12.sp
                )
            }
        }
    }
}
