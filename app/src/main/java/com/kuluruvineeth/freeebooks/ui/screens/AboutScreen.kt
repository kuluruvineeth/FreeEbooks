package com.kuluruvineeth.freeebooks.ui.screens

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kuluruvineeth.freeebooks.BuildConfig
import com.kuluruvineeth.freeebooks.ui.common.CustomTopAppBar
import com.kuluruvineeth.freeebooks.R
import com.kuluruvineeth.freeebooks.others.Constants
import com.kuluruvineeth.freeebooks.ui.theme.comfortFont

@Composable
fun AboutScreen(
    navController: NavController
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        CustomTopAppBar(headerText = stringResource(id = R.string.about_header)){
            navController.navigateUp()
        }
        
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(148.dp),
            contentAlignment = Alignment.Center
        ){
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp))
            ){
                Image(
                    modifier = Modifier.size(100.dp),
                    painter = painterResource(id = R.drawable.ic_splash_screen),
                    contentDescription = null
                )
            }
        }

        Text(
            text = stringResource(id = R.string.app_name),
            modifier = Modifier.fillMaxWidth(),
            fontSize = 24.sp,
            fontFamily = comfortFont,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Version ${BuildConfig.VERSION_NAME}",
            modifier = Modifier.fillMaxWidth(),
            fontSize = 14.sp,
            fontFamily = comfortFont,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(id = R.string.about_desc),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 22.dp, end = 22.dp),
            fontSize = 14.sp,
            fontFamily = comfortFont,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(id = R.string.developed_by),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, bottom = 12.dp),
            fontSize = 16.sp,
            fontFamily = comfortFont,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Card(
            modifier = Modifier
                .height(135.dp)
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(
                    2.dp
                )
            )
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.github_profile),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .size(90.dp)
                        .clip(CircleShape)
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 14.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.dev_name),
                        fontSize = 18.sp,
                        fontFamily = comfortFont,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = stringResource(id = R.string.dev_username),
                        fontSize = 16.sp,
                        fontFamily = comfortFont,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    
                    Spacer(modifier = Modifier.height(6.dp))

                    Row {
                        LinkButton(
                            text = "Github",
                            icon = ImageVector.vectorResource(id = R.drawable.ic_github_logo)
                        ){
                            val uri: Uri = Uri.parse(Constants.DEV_GITHUB_URL)
                            val intent = Intent(Intent.ACTION_VIEW,uri)
                            try {
                                context.startActivity(intent)
                            }catch (exc: ActivityNotFoundException){
                                exc.printStackTrace()
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        
                        LinkButton(
                            text = "LinkedIn",
                            icon = painterResource(id = R.drawable.ic_linkedin_logo)
                        ){
                            val uri: Uri = Uri.parse(Constants.DEV_LINKEDIN_URL)
                            val intent = Intent(Intent.ACTION_VIEW,uri)
                            try {
                                context.startActivity(intent)
                            }catch (exc : ActivityNotFoundException){
                                exc.printStackTrace()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LinkButton(
    text: String,
    icon: Any,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable { onClick() }
            .padding(all = 4.dp)
    ) {
        if(icon is ImageVector){
            Icon(
                imageVector = icon as ImageVector,
                contentDescription = null,
                modifier = Modifier.size(size = 18.dp),
                tint = MaterialTheme.colorScheme.onSurface
            )
        }else{
            Icon(
                painter = icon as Painter,
                contentDescription = null,
                modifier = Modifier.size(size = 18.dp),
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        Text(
            text = text.uppercase(),
            fontWeight = FontWeight.Bold,
            fontFamily = comfortFont,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}