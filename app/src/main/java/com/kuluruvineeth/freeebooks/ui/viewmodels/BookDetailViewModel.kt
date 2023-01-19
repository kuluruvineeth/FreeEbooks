package com.kuluruvineeth.freeebooks.ui.viewmodels

import android.app.DownloadManager
import android.content.Context
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
import com.kuluruvineeth.freeebooks.utils.toToast

data class ScreenState(
    val isLoading: Boolean = true,
    val item: BookSet = BookSet(0,null,null, emptyList()),
    val extraInfo: ExtraInfo = ExtraInfo()
)

class BookDetailViewModel : ViewModel() {
    var state by mutableStateOf(ScreenState())

    fun getBookDetails(bookId: String){
        viewModelScope.launch(Dispatchers.IO) {
            val bookItem = BooksApi.getBookById(bookId).getOrNull()!!
            val extraInfo = BooksApi.getExtraInfo(bookItem.books.first().title)
            state = if(extraInfo!=null){
                state.copy(
                    isLoading = false,
                    item = bookItem,
                    extraInfo = extraInfo
                )
            }else{
                state.copy(
                    isLoading = false,
                    item = bookItem
                )
            }
        }
    }

    fun downloadBook(book: Book, activity: MainActivity):String{
        if(activity.checkStoragePermission()){
            //setup download manager
            val filename = book.title.split(" ").joinToString("+")
            val manager = activity.getSystemService(
                Context.DOWNLOAD_SERVICE
            ) as DownloadManager
            val uri = Uri.parse(book.formats.applicationepubzip)
            val request = DownloadManager.Request(uri)

            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setAllowedOverRoaming(true)
                .setAllowedOverMetered(true)
                .setTitle(book.title)
                .setDescription(BookUtils.getAuthorsAsString(book.authors))
                .setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOWNLOADS,
                    Constants.DOWNLOAD_DIR + "/" + "${filename}.epub"
                )
            //start downloading
            manager.enqueue(request)
            return activity.getString(R.string.downloading)
        }else{
            return activity.getString(R.string.storage_perm_error)
        }
    }
}
