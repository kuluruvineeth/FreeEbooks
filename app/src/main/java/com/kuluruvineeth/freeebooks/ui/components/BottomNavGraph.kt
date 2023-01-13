package com.kuluruvineeth.freeebooks.ui.components

import androidx.compose.runtime.Composable
import androidx.navigation.compose.composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.kuluruvineeth.freeebooks.ui.screens.HomeScreen
import com.kuluruvineeth.freeebooks.ui.screens.LibraryScreen
import com.kuluruvineeth.freeebooks.ui.screens.SettingsScreen

@Composable
fun BottomNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.Home.route
    ){
        composable(route = BottomBarScreen.Home.route){
            HomeScreen()
        }
        composable(route = BottomBarScreen.Library.route){
            LibraryScreen()
        }
        composable(route = BottomBarScreen.Settings.route){
            SettingsScreen()
        }
    }
}