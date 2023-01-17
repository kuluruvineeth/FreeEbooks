package com.kuluruvineeth.freeebooks.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuluruvineeth.freeebooks.api.BooksApi
import com.kuluruvineeth.freeebooks.api.models.BookSet
import com.kuluruvineeth.freeebooks.api.models.ExtraInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
            //val extraInfo = BooksApi.getExtraInfo(bookItem.books.firstOrNull()?.title ?: "AgriRize")
            val extraInfo = ExtraInfo()
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
}
