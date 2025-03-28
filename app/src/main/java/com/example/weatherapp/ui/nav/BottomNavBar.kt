package com.example.weatherapp.ui.nav

import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.example.weatherapp.MainViewModel


@Composable
fun BottomNavBar(viewModel: MainViewModel, items: List<BottomNavItem>) {
    NavigationBar(

        contentColor = Color.Black
    ) {
        // Adicionando NavigationBarItems corretamente no escopo do NavigationBar
        items.forEach { item ->
            //val navBackStackEntry by navController.currentBackStackEntryAsState()
            //val currentRoute = navBackStackEntry?.destination

            NavigationBarItem(
                icon = { Icon(imageVector = item.icon, contentDescription = item.title) },
                label = { Text(text = item.title, fontSize = 12.sp) },
                alwaysShowLabel = true,
                selected = viewModel.page == item.route,
                onClick = {
                    viewModel.page = item.route
                }

            )
        }
    }
}






