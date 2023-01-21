package com.kuluruvineeth.freeebooks.navigation

import SettingsScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.navigation.compose.composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.navArgument
import com.kuluruvineeth.freeebooks.others.NetworkObserver
import com.kuluruvineeth.freeebooks.ui.screens.*

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
fun NavGraph(
    navController: NavHostController,
    paddingValues: PaddingValues,
    networkStatus : NetworkObserver.Status
) {
    NavHost(
        navController = navController,
        startDestination = Screens.SplashScreen.route,
        modifier = Modifier
            .padding(paddingValues)
            .background(MaterialTheme.colorScheme.background)
    ){
        //Splash Screen
        composable(route = Screens.SplashScreen.route){
            SplashScreen(navController = navController)
        }

        composable(route = BottomBarScreen.Home.route){
            HomeScreen(navController,networkStatus)
        }
        composable(route = BottomBarScreen.Categories.route){
            CategoriesScreen(navController)
        }
        composable(route = BottomBarScreen.Library.route){
            LibraryScreen()
        }
        composable(route = BottomBarScreen.Settings.route){
            SettingsScreen(navController)
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

        composable(
            route = Screens.CategoryDetailScreen.route,
            arguments = listOf(
                navArgument(CATEGORY_DETAIL_ARG_KEY){
                    type = NavType.StringType
                }
            )
        ){backStackEntry ->
            val category = backStackEntry.arguments!!.getString(CATEGORY_DETAIL_ARG_KEY)!!
            CategoryDetailScreen(category, navController, networkStatus)
        }

        composable(route = Screens.OSLScreen.route){
            OSLScreen(navController = navController)
        }
    }
}