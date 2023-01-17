package com.kuluruvineeth.freeebooks.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.kuluruvineeth.freeebooks.R
import com.kuluruvineeth.freeebooks.ui.theme.comfortFont

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookItemCard(
    title: String,
    author: String,
    coverImageUrl: String?,
    language: String,
    subjects: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .height(185.dp)
            .fillMaxWidth(),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(
                2.dp
            )
        ),
        shape = RoundedCornerShape(6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            val imageBackground = if(isSystemInDarkTheme()){
                MaterialTheme.colorScheme.onSurface
            }else{
                MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp)
            }
            Box(
                modifier = Modifier
                    .weight(1.5f)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(imageBackground)
            ) {
                val painter = rememberImagePainter(
                    ImageRequest.Builder(LocalContext.current).data(data = coverImageUrl)
                        .apply(block = fun ImageRequest.Builder.() {
                            placeholder(R.drawable.placeholder_cat)
                            error(R.drawable.placeholder_cat)
                            crossfade(500)
                        }).build()
                )
                Image(
                    painter = painter,
                    contentDescription = stringResource(id = R.string.cover_image_desc),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                if(painter.state is ImagePainter.State.Loading){
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
            Column(
                modifier = Modifier
                    .weight(3f)
                    .fillMaxHeight()
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = title,
                    modifier = Modifier
                        .padding(
                            start = 12.dp,
                            end = 8.dp
                        )
                        .fillMaxWidth(),
                    fontStyle = MaterialTheme.typography.headlineMedium.fontStyle,
                    fontSize = 20.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = author,
                    modifier = Modifier.padding(
                        start = 12.dp,
                        end = 8.dp
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    fontStyle = MaterialTheme.typography.bodySmall.fontStyle,
                    fontSize = 14.sp,
                    fontFamily = comfortFont
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = language,
                    modifier = Modifier.padding(
                        start = 12.dp,
                        end = 8.dp
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 16.sp,
                    fontStyle = MaterialTheme.typography.bodyMedium.fontStyle
                )

                Text(
                    text = subjects,
                    modifier = Modifier.padding(start = 12.dp, end = 8.dp, bottom = 2.dp),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontStyle = MaterialTheme.typography.bodySmall.fontStyle,
                    fontFamily = comfortFont,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}