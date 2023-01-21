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
import com.kuluruvineeth.freeebooks.utils.toToast
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class ScreenState(
    val isLoading: Boolean = true,
    val item: BookSet = BookSet(0,null,null, emptyList()),
    val extraInfo: ExtraInfo = ExtraInfo()
)

@HiltViewModel
class BookDetailViewModel @Inject constructor(private val libraryDao: LibraryDao) : ViewModel() {
    var state by mutableStateOf(ScreenState())

    fun getBookDetails(bookId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val bookItem = BooksApi.getBookById(bookId).getOrNull()!!
            val extraInfo = BooksApi.getExtraInfo(bookItem.books.first().title)
            state = if (extraInfo != null) {
                state.copy(isLoading = false, item = bookItem, extraInfo = extraInfo)
            } else {
                state.copy(isLoading = false, item = bookItem)
            }
        }
    }

    @SuppressLint("Range")
    fun downloadBook(book: Book, activity: MainActivity): String {
        if (activity.checkStoragePermission()) {
            // setup download manager.
            val filename = book.title.split(" ").joinToString(separator = "+") + ".epub"
            val manager = activity.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val uri = Uri.parse(book.formats.applicationepubzip)
            val request = DownloadManager.Request(uri)
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                .setAllowedOverRoaming(true)
                .setAllowedOverMetered(true)
                .setTitle(book.title)
                .setDescription(BookUtils.getAuthorsAsString(book.authors))
                .setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOWNLOADS,
                    Constants.DOWNLOAD_DIR + "/" + filename
                )
            // start downloading.
            val downloadId = manager.enqueue(request)
            viewModelScope.launch(Dispatchers.IO) {
                var isDownloadFinished = false
                while (!isDownloadFinished) {
                    val cursor: Cursor =
                        manager.query(DownloadManager.Query().setFilterById(downloadId))
                    if (cursor.moveToFirst()) {
                        when (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))) {
                            DownloadManager.STATUS_SUCCESSFUL -> {
                                insertIntoDB(book, filename)
                                isDownloadFinished = true
                            }
                            DownloadManager.STATUS_PAUSED, DownloadManager.STATUS_PENDING -> {}
                            DownloadManager.STATUS_FAILED -> {
                                isDownloadFinished = true
                            }
                        }
                    } else {
                        // Download cancelled by the user.
                        isDownloadFinished = true
                    }
                    cursor.close()
                }
            }
            return activity.getString(R.string.downloading)
        } else {
            return activity.getString(R.string.storage_perm_error)
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
