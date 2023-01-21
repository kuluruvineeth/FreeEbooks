package com.kuluruvineeth.freeebooks.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mikepenz.aboutlibraries.ui.compose.LibrariesContainer
import com.mikepenz.aboutlibraries.ui.compose.LibraryDefaults
import com.kuluruvineeth.freeebooks.R
import com.kuluruvineeth.freeebooks.ui.theme.comfortFont

@Composable
fun OSLScreen(
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 8.dp)
        ) {
            OSLTopAppBar{
                navController.navigateUp()
            }
            Divider(
                color = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp),
                thickness = 2.dp
            )
        }
        LibrariesContainer(
            modifier = Modifier.fillMaxSize(),
            showAuthor = true,
            showVersion = true,
            showLicenseBadges = true,
            colors = LibraryDefaults.libraryColors(
                backgroundColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onBackground,
                badgeBackgroundColor = MaterialTheme.colorScheme.primary,
                badgeContentColor = MaterialTheme.colorScheme.onPrimary
            )
        )
    }
}

@Composable
fun OSLTopAppBar(onBackClicked: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .padding(start = 18.dp, top = 2.dp, bottom = 18.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp))
                .clickable { onBackClicked() }
        ){
            Icon(
                imageVector = Icons.Outlined.ArrowBack, 
                contentDescription = stringResource(id = R.string.back_button_desc),
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(10.dp)
            )
        }
        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = stringResource(id = R.string.open_source_header),
            modifier = Modifier.padding(
                bottom = 16.dp
            ),
            color = MaterialTheme.colorScheme.onBackground,
            fontStyle = MaterialTheme.typography.headlineMedium.fontStyle,
            fontFamily = comfortFont,
            fontSize = 24.sp
        )
        
        Spacer(modifier = Modifier.weight(1.56f))
    }
}