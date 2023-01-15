package com.kuluruvineeth.freeebooks.navigation

import com.kuluruvineeth.freeebooks.R

sealed class BottomBarScreen(
    val route: String,
    val title: Int,
    val icon: Int
){

    object Home : BottomBarScreen(
        route = "home",
        title = R.string.navigation_home,
        icon = R.drawable.ic_nav_home
    )

    object Library : BottomBarScreen(
        route = "library",
        title = R.string.navigation_library,
        icon = R.drawable.ic_nav_library
    )

    object Search : BottomBarScreen(
        route = "search",
        title = R.string.home_search_icon_desc,
        icon = R.drawable.ic_search
    )

    object Settings : BottomBarScreen(
        route = "settings",
        title = R.string.navigation_settings,
        icon = R.drawable.ic_nav_settings
    )
}