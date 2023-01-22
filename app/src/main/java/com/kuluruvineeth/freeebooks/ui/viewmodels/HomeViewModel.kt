package com.kuluruvineeth.freeebooks.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuluruvineeth.freeebooks.api.BooksApi
import com.kuluruvineeth.freeebooks.api.models.Book
import com.kuluruvineeth.freeebooks.others.Paginator
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


data class AllBooksState(
    val isLoading: Boolean = false,
    val items: List<Book> = emptyList(),
    val error: String? = null,
    val endReached: Boolean = false,
    val page: Long = 1L
)

data class TopBarState(
    val searchText: String = "",
    val isSearchBarVisible: Boolean = false,
    val isSearching: Boolean = false,
    val searchResults: List<Book> = emptyList()
)

sealed class UserAction {
    object SearchIconClicked : UserAction()
    object CloseIconClicked : UserAction()
    data class TextFieldInput(val text : String) : UserAction()
}


class HomeViewModel : ViewModel() {
    var allBooksState by mutableStateOf(AllBooksState())
    var topBarState by mutableStateOf(TopBarState())

    private val paginator = Paginator(
        initialPage = allBooksState.page,
        onLoadUpdated = {
            allBooksState = allBooksState.copy(isLoading = it)
        },
        onRequest = {nextPage ->
            BooksApi.getAllBooks(nextPage)
        },
        getNextPage = {
            allBooksState.page + 1L
        },
        onError = {
            allBooksState = allBooksState.copy(error = it?.localizedMessage)
        },
        onSuccess = {bookSet, newPage ->
            allBooksState = allBooksState.copy(
                items = (allBooksState.items + bookSet.books),
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

    private var searchJob: Job? = null

    fun onAction(userAction: UserAction){
        when(userAction){
            UserAction.CloseIconClicked -> {
                topBarState = topBarState.copy(isSearchBarVisible = false)
            }
            UserAction.SearchIconClicked -> {
                topBarState = topBarState.copy(isSearchBarVisible = true)
            }
            is UserAction.TextFieldInput -> {
                topBarState = topBarState.copy(searchText = userAction.text)
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    if(userAction.text.isNotBlank()){
                        topBarState = topBarState.copy(isSearching = true)
                    }
                    delay(500L)
                    searchBooks(userAction.text)
                }
            }
        }
    }

    private suspend fun searchBooks(query: String){
        val bookSet = BooksApi.searchBooks(query)
        topBarState = topBarState.copy(
            searchResults = bookSet.getOrNull()!!.books,
            isSearching = false
        )
    }
}