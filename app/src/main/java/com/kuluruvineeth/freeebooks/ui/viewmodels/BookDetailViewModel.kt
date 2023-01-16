package com.kuluruvineeth.freeebooks.ui.viewmodels

import com.kuluruvineeth.freeebooks.api.models.BookSet

data class ScreenState(
    val isLoading: Boolean = false,
    val item: BookSet = BookSet(0,null,null, emptyList())
)

class BookDetailViewModel {

}
