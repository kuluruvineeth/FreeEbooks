package com.kuluruvineeth.freeebooks.navigation

import SettingsScreen
import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.kuluruvineeth.freeebooks.others.NetworkObserver
import com.kuluruvineeth.freeebooks.ui.screens.*

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class,
    ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class
)
@Composable
fun NavGraph(
    navController: NavHostController,
    networkStatus : NetworkObserver.Status
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = Screens.SplashScreen.route,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
    ){
        //Splash Screen
        composable(route = Screens.SplashScreen.route){
            SplashScreen(navController = navController)
        }

        //Bottom Navigation Screens
        composable(
            route = BottomBarScreen.Home.route,
            exitTransition = {
                if(initialState.destination.route == Screens.BookDetailScreen.route){
                    slideOutHorizontally(
                        targetOffsetX = {-300},
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = FastOutSlowInEasing
                        )
                    ) + fadeOut(animationSpec = tween(300))
                } else null
            },
            popEnterTransition = {
                if(targetState.destination.route == Screens.BookDetailScreen.route){
                    slideInHorizontally(
                        initialOffsetX = {-300},
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = FastOutSlowInEasing
                        )
                    ) + fadeIn(animationSpec = tween(300))
                } else null
            }
        ){
            HomeScreen(navController = navController, networkStatus = networkStatus)
        }

        composable(
            route = BottomBarScreen.Categories.route,
            exitTransition = {
                if(initialState.destination.route == Screens.BookDetailScreen.route){
                    slideOutHorizontally(
                        targetOffsetX = {-300},
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = FastOutSlowInEasing
                        )
                    ) + fadeOut(animationSpec = tween(300))
                } else null
            },
            popEnterTransition = {
                if(targetState.destination.route == Screens.BookDetailScreen.route){
                    slideInHorizontally(
                        initialOffsetX = {-300},
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = FastOutSlowInEasing
                        )
                    ) + fadeIn(animationSpec = tween(300))
                } else null
            }
        ){
            CategoriesScreen(navController = navController)
        }
        composable(
            route = BottomBarScreen.Library.route,
            exitTransition = {
                if(initialState.destination.route == Screens.BookDetailScreen.route){
                    slideOutHorizontally(
                        targetOffsetX = {-300},
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = FastOutSlowInEasing
                        )
                    ) + fadeOut(animationSpec = tween(300))
                } else null
            },
            popEnterTransition = {
                if(targetState.destination.route == Screens.BookDetailScreen.route){
                    slideInHorizontally(
                        initialOffsetX = {-300},
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = FastOutSlowInEasing
                        )
                    ) + fadeIn(animationSpec = tween(300))
                } else null
            }
        ){
            LibraryScreen(navController)
        }

        composable(
            route = BottomBarScreen.Settings.route,
            exitTransition = {
                if(initialState.destination.route == Screens.BookDetailScreen.route){
                    slideOutHorizontally(
                        targetOffsetX = {-300},
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = FastOutSlowInEasing
                        )
                    ) + fadeOut(animationSpec = tween(300))
                } else null
            },
            popEnterTransition = {
                if(targetState.destination.route == Screens.BookDetailScreen.route){
                    slideInHorizontally(
                        initialOffsetX = {-300},
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = FastOutSlowInEasing
                        )
                    ) + fadeIn(animationSpec = tween(300))
                } else null
            }
        ){
            SettingsScreen(navController)
        }

        //Other Screens
        composable(
            route = Screens.BookDetailScreen.route, arguments = listOf(
                navArgument(BOOK_DETAIL_ARG_KEY){
                    type = NavType.StringType
                }
            ),
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { 300 }, animationSpec = tween(
                        durationMillis = 300, easing = FastOutSlowInEasing
                    )
                ) + fadeIn(animationSpec = tween(300))
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { 300 }, animationSpec = tween(
                        durationMillis = 300, easing = FastOutSlowInEasing
                    )
                ) + fadeOut(animationSpec = tween(300))
            },
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
            ),
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { 300 }, animationSpec = tween(
                        durationMillis = 300, easing = FastOutSlowInEasing
                    )
                ) + fadeIn(animationSpec = tween(300))
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = {-300},
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = FastOutSlowInEasing
                    )
                ) + fadeOut(animationSpec = tween(300))
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = {-300},
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = FastOutSlowInEasing
                    ),
                ) + fadeIn(animationSpec = tween(300))
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { 300 }, animationSpec = tween(
                        durationMillis = 300, easing = FastOutSlowInEasing
                    )
                ) + fadeOut(animationSpec = tween(300))
            },
        ){backStackEntry ->
            val category = backStackEntry.arguments!!.getString(CATEGORY_DETAIL_ARG_KEY)!!
            CategoryDetailScreen(category, navController, networkStatus)
        }

        composable(
            route = Screens.OSLScreen.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { 300 }, animationSpec = tween(
                        durationMillis = 300, easing = FastOutSlowInEasing
                    )
                ) + fadeIn(animationSpec = tween(300))
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { 300 }, animationSpec = tween(
                        durationMillis = 300, easing = FastOutSlowInEasing
                    )
                ) + fadeOut(animationSpec = tween(300))
            },
        ) {
            OSLScreen(navController = navController)
        }

        composable(
            route = Screens.AboutScreen.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { 300 }, animationSpec = tween(
                        durationMillis = 300, easing = FastOutSlowInEasing
                    )
                ) + fadeIn(animationSpec = tween(300))
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { 300 }, animationSpec = tween(
                        durationMillis = 300, easing = FastOutSlowInEasing
                    )
                ) + fadeOut(animationSpec = tween(300))
            },
        ) {
            AboutScreen(navController = navController)
        }
    }
}