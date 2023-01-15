package com.kuluruvineeth.freeebooks.ui.screens

import android.content.res.Resources
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImagePainter
import coil.compose.ImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.kuluruvineeth.freeebooks.R
import com.kuluruvineeth.freeebooks.common.compose.ProgressDots
import com.kuluruvineeth.freeebooks.ui.viewmodels.HomeViewModel
import com.kuluruvineeth.freeebooks.utils.Utils


@Composable
fun HomeScreen() {

    val viewModel = viewModel<HomeViewModel>()
    val state = viewModel.state

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(20.dp)
        ) {
            TopAppBar()
            Divider(
                color = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp),
                thickness = 2.dp
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(start = 8.dp, end = 8.dp)
        ) {
            items(state.items.size){i ->
                val item = state.items[i]
                Log.d("RESPONSE",item.toString())
                if(i >= state.items.size-1 && !state.endReached && !state.isLoading){
                    viewModel.loadNextItems()
                }
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ){
                    BookItemCard(
                        title = item.title,
                        author = Utils.getAuthorsAsString(item.authors),
                        coverImageUrl = item.formats.imagejpeg,
                        language = Utils.getLanguagesAsString(item.languages),
                        subjects = Utils.getSubjectsAsString(item.subjects,3)
                    )
                }
            }
            item {
                if(state.isLoading){
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        ProgressDots()
                    }
                }
            }
        }
    }
}

@Composable
fun TopAppBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 7.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Most Popular",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            fontFamily = FontFamily.Cursive
        )
        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_category_header),
                contentDescription = stringResource(id = R.string.home_search_icon_desc),
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(30.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookItemCard(
    title: String,
    author: String,
    coverImageUrl: String?,
    language: String,
    subjects: String
) {
    Card(
        modifier = Modifier
            .height(210.dp)
            .fillMaxWidth(),
        onClick = {},
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
            Box(
                modifier = Modifier
                    .weight(1.5f)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(6.dp))
            ) {
                val painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current).data(data = coverImageUrl)
                        .apply(block = fun ImageRequest.Builder.() {
                            placeholder(R.drawable.placeholder_cat)
                            error(R.drawable.placeholder_cat)
                            crossfade(800)
                        }).build()
                )
                Image(
                    painter = painter, 
                    contentDescription = stringResource(id = R.string.cover_image_desc),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                if(painter.state is AsyncImagePainter.State.Loading){
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
                            end = 8.dp,
                            top = 8.dp
                        )
                        .fillMaxWidth(),
                    fontStyle = MaterialTheme.typography.headlineMedium.fontStyle,
                    fontSize = 22.sp,
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
                    fontStyle = MaterialTheme.typography.bodySmall.fontStyle
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = language,
                    modifier = Modifier.padding(
                        start = 12.dp,
                        end = 8.dp
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 18.sp,
                    fontStyle = MaterialTheme.typography.bodyMedium.fontStyle
                )

                Text(
                    text = subjects,
                    modifier = Modifier.padding(start = 12.dp, end = 8.dp, bottom = 2.dp),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontStyle = MaterialTheme.typography.bodySmall.fontStyle
                )

                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}