package com.kuluruvineeth.freeebooks.ui.screens

import android.app.DownloadManager
import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.*
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.kuluruvineeth.freeebooks.MainActivity
import com.kuluruvineeth.freeebooks.ui.viewmodels.BookDetailViewModel
import com.kuluruvineeth.freeebooks.R
import com.kuluruvineeth.freeebooks.common.compose.ProgressDots
import com.kuluruvineeth.freeebooks.others.NetworkObserver
import com.kuluruvineeth.freeebooks.ui.theme.comfortFont
import com.kuluruvineeth.freeebooks.utils.BookUtils
import com.kuluruvineeth.freeebooks.utils.Utils
import com.kuluruvineeth.freeebooks.utils.getActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailScreen(
    bookId: String, navController: NavController, networkStatus: NetworkObserver.Status
) {
    val context = LocalContext.current
    val viewModel: BookDetailViewModel = hiltViewModel()
    viewModel.getBookDetails(bookId)
    val state = viewModel.state

    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = {
            SnackbarHost(hostState = it) { data ->
                Snackbar(
                    backgroundColor = MaterialTheme.colorScheme.inverseSurface,
                    contentColor = MaterialTheme.colorScheme.inverseOnSurface,
                    snackbarData = data,
                )
            }
        },
    ) { paddingValues ->
        if (networkStatus == NetworkObserver.Status.Available) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                BookDetailTopBar(onBackClicked = {
                    navController.navigateUp()
                }, onShareClicked = {
                    val intent = Intent(Intent.ACTION_SEND)
                    intent.type = "text/plain"
                    intent.putExtra(
                        Intent.EXTRA_TEXT, "https://www.gutenberg.org/ebooks/$bookId"
                    )
                    val chooser = Intent.createChooser(
                        intent, context.getString(R.string.share_intent_header)
                    )
                    context.startActivity(chooser)
                })

                if (state.isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 65.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        ProgressDots()
                    }
                } else {
                    val book = state.item.books.first()
                    Column(
                        Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(260.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.book_details_bg),
                                contentDescription = "",
                                alpha = 0.2f,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.verticalGradient(
                                            colors = listOf(
                                                MaterialTheme.colorScheme.background,
                                                Color.Transparent,
                                                MaterialTheme.colorScheme.background
                                            ), startY = 15f
                                        )
                                    )
                            )

                            Row(modifier = Modifier.fillMaxSize()) {
                                val imageUrl = state.extraInfo.coverImage.ifEmpty {
                                    book.formats.imagejpeg
                                }

                                Box(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    val imageBackground = if (isSystemInDarkTheme()) {
                                        MaterialTheme.colorScheme.onSurface
                                    } else {
                                        MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp)
                                    }
                                    Box(
                                        modifier = Modifier
                                            .shadow(24.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(imageBackground)
                                    ) {
                                        AsyncImage(
                                            model = ImageRequest.Builder(context).data(imageUrl)
                                                .crossfade(true).build(),
                                            placeholder = painterResource(id = R.drawable.placeholder_cat),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .width(118.dp)
                                                .height(169.dp),
                                            contentScale = ContentScale.Crop
                                        )
                                    }
                                }

                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = book.title,
                                        modifier = Modifier
                                            .padding(
                                                start = 12.dp, end = 8.dp, top = 20.dp
                                            )
                                            .fillMaxWidth(),
                                        fontSize = 24.sp,
                                        fontFamily = comfortFont,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis,
                                        color = MaterialTheme.colorScheme.onBackground,
                                    )

                                    Text(
                                        text = BookUtils.getAuthorsAsString(book.authors),
                                        modifier = Modifier.padding(
                                            start = 12.dp, end = 8.dp, top = 4.dp
                                        ),
                                        fontSize = 18.sp,
                                        fontFamily = comfortFont,
                                        fontWeight = FontWeight.SemiBold,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis,
                                        color = MaterialTheme.colorScheme.onBackground,
                                    )

                                    Spacer(modifier = Modifier.height(50.dp))
                                }
                            }
                        }

                        val pageCount = if (state.extraInfo.pageCount > 0) {
                            state.extraInfo.pageCount.toString()
                        } else {
                            stringResource(id = R.string.not_applicable)
                        }

                        // Check if this book is in downloadQueue.
                        val buttonTextValue =
                            if (viewModel.bookDownloader.isBookCurrentlyDownloading(book.id)) {
                                stringResource(id = R.string.cancel)
                            } else {
                                if (state.bookLibraryItem != null) stringResource(id = R.string.read_book_button) else stringResource(
                                    id = R.string.download_book_button
                                )
                            }

                        var buttonText by remember { mutableStateOf(buttonTextValue) }
                        var progressState by remember { mutableStateOf(0f) }
                        var showProgressBar by remember { mutableStateOf(false) }

                        // Callable which updates book details screen button.
                        val updateBtnText: (Int?) -> Unit = { downloadStatus ->
                            buttonText = when (downloadStatus) {
                                DownloadManager.STATUS_RUNNING -> {
                                    showProgressBar = true
                                    context.getString(R.string.cancel)
                                }
                                DownloadManager.STATUS_SUCCESSFUL -> {
                                    showProgressBar = false
                                    context.getString(R.string.read_book_button)
                                }
                                else -> {
                                    showProgressBar = false
                                    context.getString(R.string.download_book_button)
                                }
                            }
                        }

                        // Check if this book is in downloadQueue.
                        if (viewModel.bookDownloader.isBookCurrentlyDownloading(book.id)) {
                            progressState =
                                viewModel.bookDownloader.getRunningDownload(book.id)?.progress?.collectAsState()?.value!!
                            LaunchedEffect(key1 = progressState, block = {
                                updateBtnText(viewModel.bookDownloader.getRunningDownload(book.id)?.status)
                            })
                        }

                        Utils.prettyCount(book.downloadCount)?.let {
                            MiddleBar(
                                bookLang = BookUtils.getLanguagesAsString(book.languages),
                                pageCount = pageCount,
                                downloadCount = it,
                                progressValue = progressState,
                                buttonText = buttonText,
                                showProgressBar = showProgressBar
                            ) {
                                when (buttonText) {
                                    context.getString(R.string.read_book_button) -> {
                                        val bookLibraryItem = state.bookLibraryItem
                                        /**
                                         *  Library item could be null if we reload the screen
                                         *  while some download was running, in that case we'll
                                         *  de-attach from our old state where download function
                                         *  will update library item and our new state will have
                                         *  no library item, i.e. null.
                                         */
                                        /**
                                         *  Library item could be null if we reload the screen
                                         *  while some download was running, in that case we'll
                                         *  de-attach from our old state where download function
                                         *  will update library item and our new state will have
                                         *  no library item, i.e. null.
                                         */
                                        /**
                                         *  Library item could be null if we reload the screen
                                         *  while some download was running, in that case we'll
                                         *  de-attach from our old state where download function
                                         *  will update library item and our new state will have
                                         *  no library item, i.e. null.
                                         */
                                        /**
                                         *  Library item could be null if we reload the screen
                                         *  while some download was running, in that case we'll
                                         *  de-attach from our old state where download function
                                         *  will update library item and our new state will have
                                         *  no library item, i.e. null.
                                         */
                                        /**
                                         *  Library item could be null if we reload the screen
                                         *  while some download was running, in that case we'll
                                         *  de-attach from our old state where download function
                                         *  will update library item and our new state will have
                                         *  no library item, i.e. null.
                                         */
                                        /**
                                         *  Library item could be null if we reload the screen
                                         *  while some download was running, in that case we'll
                                         *  de-attach from our old state where download function
                                         *  will update library item and our new state will have
                                         *  no library item, i.e. null.
                                         */
                                        /**
                                         *  Library item could be null if we reload the screen
                                         *  while some download was running, in that case we'll
                                         *  de-attach from our old state where download function
                                         *  will update library item and our new state will have
                                         *  no library item, i.e. null.
                                         */
                                        /**
                                         *  Library item could be null if we reload the screen
                                         *  while some download was running, in that case we'll
                                         *  de-attach from our old state where download function
                                         *  will update library item and our new state will have
                                         *  no library item, i.e. null.
                                         */
                                        if (bookLibraryItem == null) {
                                            viewModel.viewModelScope.launch(Dispatchers.IO) {
                                                val libraryItem =
                                                    viewModel.libraryDao.getItemById(book.id)!!
                                                Utils.openBookFile(context, libraryItem)
                                            }
                                        } else {
                                            Utils.openBookFile(context, bookLibraryItem)
                                        }

                                    }
                                    context.getString(R.string.download_book_button) -> {
                                        val message = viewModel.downloadBook(
                                            book, (context.getActivity() as MainActivity)
                                        ) { downloadProgress, downloadStatus ->
                                            progressState = downloadProgress
                                            updateBtnText(downloadStatus)
                                        }
                                        coroutineScope.launch {
                                            scaffoldState.snackbarHostState.showSnackbar(
                                                message = message,
                                            )
                                        }
                                    }
                                    context.getString(R.string.cancel) -> {
                                        viewModel.bookDownloader.cancelDownload(
                                            viewModel.bookDownloader.getRunningDownload(book.id)?.downloadId
                                        )
                                    }
                                }
                            }
                        }

                        Text(
                            text = stringResource(id = R.string.book_synopsis),
                            modifier = Modifier.padding(start = 12.dp, end = 8.dp),
                            fontSize = 20.sp,
                            fontFamily = comfortFont,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground,
                        )

                        val synopsis = state.extraInfo.description.ifEmpty {
                            stringResource(id = R.string.book_synopsis_not_found)
                        }

                        Text(
                            text = synopsis,
                            modifier = Modifier.padding(14.dp),
                            fontFamily = comfortFont,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onBackground,
                        )

                    }
                }
            }
        } else {
            NoInternetScreen()
        }
    }


}

@ExperimentalMaterial3Api
@Composable
fun MiddleBar(
    bookLang: String,
    pageCount: String,
    downloadCount: String,
    progressValue: Float,
    buttonText: String,
    showProgressBar: Boolean,
    onButtonClick: () -> Unit
) {
    val progress by animateFloatAsState(targetValue = progressValue)
    Column(modifier = Modifier.fillMaxWidth()) {
        AnimatedVisibility(visible = showProgressBar) {
            if (progressValue > 0f) {
                // Determinate progress bar.
                LinearProgressIndicator(
                    progress = progress,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                        .padding(start = 14.dp, end = 14.dp, top = 6.dp)
                        .clip(RoundedCornerShape(40.dp))
                )
            } else {
                // Indeterminate progress bar.
                LinearProgressIndicator(
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                        .padding(start = 14.dp, end = 14.dp, top = 6.dp)
                        .clip(RoundedCornerShape(40.dp))
                )
            }
        }

        androidx.compose.material3.Card(
            modifier = Modifier
                .height(90.dp)
                .fillMaxWidth()
                .padding(start = 12.dp, end = 12.dp, top = 12.dp, bottom = 6.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(
                    2.dp
                )
            )
        ) {
            Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Row {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_book_language),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(top = 14.dp, bottom = 14.dp, end = 4.dp)
                        )
                        Text(
                            text = bookLang,
                            modifier = Modifier.padding(top = 14.dp, bottom = 14.dp, start = 4.dp),
                            fontSize = 18.sp,
                            fontFamily = comfortFont,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                    }

                }
                Divider(
                    modifier = Modifier
                        .fillMaxHeight(0.6f)
                        .width(2.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Row {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_book_pages),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(top = 13.dp, bottom = 15.dp, end = 4.dp)
                        )
                        Text(
                            text = pageCount,
                            modifier = Modifier.padding(top = 14.dp, bottom = 14.dp, start = 4.dp),
                            fontSize = 18.sp,
                            fontFamily = comfortFont,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                    }
                }
                Divider(
                    modifier = Modifier
                        .fillMaxHeight(0.6f)
                        .width(2.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Row {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_book_downloads),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(top = 15.dp, bottom = 13.dp, end = 4.dp)
                        )
                        Text(
                            text = downloadCount,
                            modifier = Modifier.padding(top = 14.dp, bottom = 14.dp, start = 4.dp),
                            fontSize = 18.sp,
                            fontFamily = comfortFont,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                    }
                }
            }
        }

        androidx.compose.material3.Card(
            onClick = { onButtonClick() },
            modifier = Modifier
                .height(75.dp)
                .fillMaxWidth()
                .padding(start = 12.dp, end = 12.dp, top = 6.dp, bottom = 12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = buttonText,
                    fontSize = 18.sp,
                    fontFamily = comfortFont,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
        }
    }
}

@Composable
fun BookDetailTopBar(
    onBackClicked: () -> Unit,
    onShareClicked: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .padding(22.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp))
                .clickable {
                    onBackClicked()
                }
        ){
            Icon(
                imageVector = Icons.Outlined.ArrowBack,
                contentDescription = stringResource(id = R.string.back_button_desc),
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(14.dp)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = stringResource(id = R.string.book_detail_header),
            modifier = Modifier.padding(bottom = 5.dp),
            color = MaterialTheme.colorScheme.onBackground,
            fontStyle = MaterialTheme.typography.headlineMedium.fontStyle,
            fontFamily = comfortFont,
            fontSize = 22.sp
        )

        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .padding(22.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp))
                .clickable { onShareClicked() }
        ){
            Icon(
                imageVector = Icons.Outlined.Share,
                contentDescription = stringResource(id = R.string.back_button_desc),
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(14.dp)
            )
        }
    }
}