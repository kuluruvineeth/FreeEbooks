package com.kuluruvineeth.freeebooks.ui.viewmodels

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuluruvineeth.freeebooks.MainActivity
import com.kuluruvineeth.freeebooks.api.BooksApi
import com.kuluruvineeth.freeebooks.api.models.Book
import com.kuluruvineeth.freeebooks.api.models.BookSet
import com.kuluruvineeth.freeebooks.api.models.ExtraInfo
import com.kuluruvineeth.freeebooks.others.Constants
import com.kuluruvineeth.freeebooks.utils.BookUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.kuluruvineeth.freeebooks.R
import com.kuluruvineeth.freeebooks.database.LibraryDao
import com.kuluruvineeth.freeebooks.database.LibraryItem
import com.kuluruvineeth.freeebooks.others.BookDownloader
import com.kuluruvineeth.freeebooks.utils.toToast
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class BookDetailScreenState(
    val isLoading: Boolean = true,
    val item: BookSet = BookSet(0,null,null, emptyList()),
    val extraInfo: ExtraInfo = ExtraInfo(),
    val bookLibraryItem: LibraryItem? = null

)

@HiltViewModel
class BookDetailViewModel @Inject constructor(
    val libraryDao: LibraryDao,
    val bookDownloader: BookDownloader
    ) : ViewModel() {
    var state by mutableStateOf(BookDetailScreenState())

    fun getBookDetails(bookId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val bookItem = BooksApi.getBookById(bookId).getOrNull()!!
            val extraInfo = BooksApi.getExtraInfo(bookItem.books.first().title)
            state = if (extraInfo != null) {
                state.copy(
                    item = bookItem,
                    extraInfo = extraInfo
                )
            } else {
                state.copy(item = bookItem)
            }
            state = state.copy(
                bookLibraryItem = libraryDao.getItemById(bookId.toInt()),
                isLoading = false
            )
        }
    }

    @SuppressLint("Range")
    fun downloadBook(
        book: Book, activity: MainActivity, downloadProgressListener: (Float, Int) -> Unit
    ): String {
        return if (activity.checkStoragePermission()) {
            bookDownloader.downloadBook(book = book,
                downloadProgressListener = downloadProgressListener,
                onDownloadSuccess = {
                    insertIntoDB(book, bookDownloader.getFilenameForBook(book))
                    state = state.copy(bookLibraryItem = libraryDao.getItemById(book.id))
                })
            activity.getString(R.string.downloading_book)
        } else {
            activity.getString(R.string.storage_perm_error)
        }
    }

    private fun insertIntoDB(book: Book, filename: String) {
        val libraryItem = LibraryItem(
            book.id,
            book.title,
            BookUtils.getAuthorsAsString(book.authors),
            "/storage/emulated/0/${Environment.DIRECTORY_DOWNLOADS}/${Constants.DOWNLOAD_DIR}/$filename",
            System.currentTimeMillis()
        )
        libraryDao.insert(libraryItem)
    }
}
