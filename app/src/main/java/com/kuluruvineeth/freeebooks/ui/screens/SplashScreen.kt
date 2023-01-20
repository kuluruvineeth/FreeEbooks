package com.kuluruvineeth.freeebooks.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.kuluruvineeth.freeebooks.navigation.BottomBarScreen
import kotlinx.coroutines.delay
import com.kuluruvineeth.freeebooks.R
import com.kuluruvineeth.freeebooks.ui.theme.comfortFont

@Composable
fun SplashScreen(
    navController: NavHostController
) {
    var startAnimation by remember {
        mutableStateOf(false)
    }
    var alphaAnim = animateFloatAsState(
        targetValue = if(startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 1000
        )
    )

    LaunchedEffect(key1 = true){
        startAnimation = true
        delay(1200)
        navController.popBackStack()

        navController.navigate(BottomBarScreen.Home.route)
    }
    Splash(alpha = alphaAnim.value)
}

@Composable
fun Splash(
    alpha: Float
) {
    Box(
        modifier = Modifier
            .background(androidx.compose.material3.MaterialTheme.colorScheme.background)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1.3f))

            Image(
                modifier = Modifier
                    .size(300.dp)
                    .alpha(alpha = alpha),
                painter = painterResource(id = R.drawable.ic_splash_screen),
                contentDescription = null
            )

            Spacer(
                modifier = Modifier.weight(1f)
            )

            Text(
                text = stringResource(id = R.string.app_name),
                fontFamily = comfortFont,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(
                modifier = Modifier.weight(0.4f)
            )
        }
    }
}