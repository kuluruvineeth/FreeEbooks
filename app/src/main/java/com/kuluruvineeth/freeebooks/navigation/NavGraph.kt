package com.kuluruvineeth.freeebooks.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.navArgument
import com.kuluruvineeth.freeebooks.others.NetworkObserver
import com.kuluruvineeth.freeebooks.ui.screens.*

@Composable
fun NavGraph(
    navController: NavHostController,
    paddingValues: PaddingValues,
    networkStatus : NetworkObserver.Status
) {
    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.Home.route,
        modifier = Modifier
            .padding(paddingValues)
            .background(MaterialTheme.colorScheme.background)
    ){
        composable(route = BottomBarScreen.Home.route){
            HomeScreen(navController,networkStatus)
        }
        composable(route = BottomBarScreen.Categories.route){
            CategoriesScreen()
        }
        composable(route = BottomBarScreen.Library.route){
            LibraryScreen()
        }
        composable(route = BottomBarScreen.Settings.route){
            SettingsScreen()
        }

        //Other Screens
        composable(
            route = Screens.BookDetailScreen.route, arguments = listOf(
                navArgument(BOOK_DETAIL_ARG_KEY){
                    type = NavType.StringType
                }
            )
        ){backStackEntry ->
            val bookId = backStackEntry.arguments!!.getString(BOOK_DETAIL_ARG_KEY)!!
            BookDetailScreen(bookId,navController,networkStatus)
        }
    }
}