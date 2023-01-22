package com.kuluruvineeth.freeebooks.ui.screens

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.kuluruvineeth.freeebooks.navigation.BottomBarScreen
import com.kuluruvineeth.freeebooks.navigation.NavGraph
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.kuluruvineeth.freeebooks.others.NetworkObserver
import com.kuluruvineeth.freeebooks.ui.theme.comfortFont


@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(networkStatus: NetworkObserver.Status) {
    val navController = rememberAnimatedNavController()
    Scaffold(
        bottomBar = {
            BottomBar(navController = navController)
        },
        containerColor = MaterialTheme.colorScheme.background
    ) {
        NavGraph(navController = navController,networkStatus)
    }
}

@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        BottomBarScreen.Home,
        BottomBarScreen.Categories,
        BottomBarScreen.Library,
        BottomBarScreen.Settings
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val bottomBarDestination = screens.any { it.route == currentDestination?.route }


    AnimatedVisibility(
        visible = bottomBarDestination,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxWidth(),
        enter = slideInVertically(initialOffsetY = {it}),
        exit = slideOutVertically(targetOffsetY = {it}),
        content = {
            Row(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp))
                    .padding(12.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                screens.forEach { screen ->
                    CustomBottomNavigationItem(
                        screen = screen,
                        isSelected = screen.route == currentDestination?.route
                    ){
                        navController.navigate(screen.route){
                            popUpTo(BottomBarScreen.Home.route)
                            launchSingleTop = true
                        }
                    }
                }
            }
        }
    )
}


@Composable
fun CustomBottomNavigationItem(
    screen: BottomBarScreen,
    isSelected: Boolean,
    onClick: () -> Unit
) {

    val background = if(isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
        else Color.Transparent
    val contentColor = if(isSelected) MaterialTheme.colorScheme.primary else
        MaterialTheme.colorScheme.onBackground

    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(background)
            .clickable(onClick = onClick)
    ){
        Row(
            modifier = Modifier
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = screen.icon),
                contentDescription = null,
                tint = contentColor
            )
            AnimatedVisibility(visible = isSelected) {
                Text(
                    text = stringResource(id = screen.title),
                    color = contentColor,
                    fontFamily = comfortFont,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }
    }

}