package com.kuluruvineeth.freeebooks.ui.components

import com.kuluruvineeth.freeebooks.R

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: Int
){

    object Home : BottomBarScreen(
        route = "home",
        title = "Home",
        icon = R.drawable.ic_nav_home
    )

    object Library : BottomBarScreen(
        route = "library",
        title = "Library",
        icon = R.drawable.ic_nav_library
    )

    object Settings : BottomBarScreen(
        route = "settings",
        title = "Settings",
        icon = R.drawable.ic_nav_settings
    )
}