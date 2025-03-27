package com.example.weatherapp.ui.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.weatherapp.HomePage
import com.example.weatherapp.ListPage
import com.example.weatherapp.MainViewModel
import com.example.weatherapp.MapPage

@Composable
fun MainNavHost(navController: NavHostController,viewModel: MainViewModel) {
    NavHost(navController, startDestination = Route.Home) {
        composable<Route.Home> { HomePage(
            viewModel = viewModel
        )  }
        composable<Route.List> { ListPage(
            viewModel = viewModel
        )  }
        composable<Route.Map>  { MapPage(
            viewModel = viewModel
        )   }
    }
}