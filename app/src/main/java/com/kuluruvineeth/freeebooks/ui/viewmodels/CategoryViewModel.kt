package com.kuluruvineeth.freeebooks.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuluruvineeth.freeebooks.api.BooksApi
import com.kuluruvineeth.freeebooks.api.models.Book
import com.kuluruvineeth.freeebooks.others.Paginator
import kotlinx.coroutines.launch

data class CategorisedBooksState(
    val isLoading: Boolean = false,
    val items: List<Book> = emptyList(),
    val error: String? = null,
    val endReached: Boolean = false,
    val page: Long = 1L
)


class CategoryViewModel(category: String) : ViewModel() {

    companion object{
        val CATEGORIES_ARRAY =
            listOf(
                "animal",
                "children",
                "classics",
                "countries",
                "crime",
                "education",
                "fiction",
                "geography",
                "history",
                "literature",
                "law",
                "music",
                "periodicals",
                "psychology",
                "philosophy",
                "religion",
                "romance",
                "science",
            )
    }

    var state by mutableStateOf(CategorisedBooksState())

    private val paginator = Paginator(
        initialPage = state.page,
        onLoadUpdated = {
            state = state.copy(isLoading = it)
        },
        onRequest = {nextPage ->
            BooksApi.getBooksByCategory(category,nextPage)
        },
        getNextPage = {
            state.page + 1L
        },
        onError = {
            state = state.copy(error = it?.localizedMessage)
        },
        onSuccess = {bookSet, newPage ->
            state = state.copy(
                items = (state.items + bookSet.books),
                page = newPage,
                endReached = bookSet.books.isEmpty()
            )
        }
    )

    init {
        loadNextItems()
    }

    fun loadNextItems(){
        viewModelScope.launch {
            paginator.loadNextItems()
        }
    }
}