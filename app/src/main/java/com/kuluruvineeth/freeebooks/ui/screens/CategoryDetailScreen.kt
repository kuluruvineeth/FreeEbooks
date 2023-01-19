package com.kuluruvineeth.freeebooks.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.kuluruvineeth.freeebooks.common.compose.ProgressDots
import com.kuluruvineeth.freeebooks.navigation.Screens
import com.kuluruvineeth.freeebooks.others.NetworkObserver
import com.kuluruvineeth.freeebooks.others.viewModelFactory
import com.kuluruvineeth.freeebooks.ui.common.BookItemCard
import com.kuluruvineeth.freeebooks.ui.viewmodels.CategoryViewModel
import com.kuluruvineeth.freeebooks.utils.BookUtils
import kotlinx.coroutines.delay
import com.kuluruvineeth.freeebooks.R
import com.kuluruvineeth.freeebooks.ui.theme.comfortFont

@Composable
fun CategoryDetailScreen(
    category: String,
    navController: NavController,
    networkStatus: NetworkObserver.Status
) {
    if(networkStatus == NetworkObserver.Status.Available){
        val viewModel = viewModel<CategoryViewModel>(factory = viewModelFactory {
            CategoryViewModel(category)
        })
        val state = viewModel.state

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            CategoryDetailTopBar(
                category = category,
                onBackClicked = {
                    navController.navigateUp()
                }
            )
            if(state.page == 1L && state.isLoading){
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }else{
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(start = 8.dp, end = 8.dp)
                ){
                    items(state.items.size){
                        val item = state.items[it]
                        if(it>= state.items.size - 1 && !state.endReached && !state.isLoading){
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
                                author = BookUtils.getAuthorsAsString(item.authors),
                                coverImageUrl = item.formats.imagejpeg,
                                language = BookUtils.getLanguagesAsString(item.languages),
                                subjects = BookUtils.getSubjectsAsString(item.subjects,3)
                            ) {
                                navController.navigate(
                                    Screens.BookDetailScreen.withBookId(item.id.toString())
                                )
                            }
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
    }else{
        var showNoInternet by remember {
            mutableStateOf(false)
        }
        LaunchedEffect(key1 = Unit){
            delay(250)
            showNoInternet = true
        }
        if(showNoInternet){
            NoInternetScreen()
        }
    }
}

@Composable
fun CategoryDetailTopBar(
    category: String,
    onBackClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .padding(22.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp))
                .clickable { onBackClicked() }
        ) {
            Icon(
                imageVector = Icons.Outlined.ArrowBack,
                contentDescription = stringResource(
                    id = R.string.back_button_desc
                ),
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(14.dp)
            )
        }
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = category,
            modifier = Modifier.padding(bottom = 2.dp),
            color = MaterialTheme.colorScheme.onBackground,
            fontStyle = MaterialTheme.typography.headlineMedium.fontStyle,
            fontFamily = comfortFont,
            fontSize = 28.sp
        )
    }
}