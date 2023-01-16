package com.kuluruvineeth.freeebooks.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.kuluruvineeth.freeebooks.api.models.BookSet

data class ScreenState(
    val isLoading: Boolean = true,
    val item: BookSet = BookSet(0,null,null, emptyList())
)

class BookDetailViewModel : ViewModel() {
    var state by mutableStateOf(ScreenState())
}
