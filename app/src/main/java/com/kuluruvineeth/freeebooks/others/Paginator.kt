package com.kuluruvineeth.freeebooks.others

class Paginator<Page, BookSet>(
    private val initialPage: Page,
    private inline val onLoadUpdated: (Boolean) -> Unit,
    private inline val onRequest: suspend (nextPage: Page) -> Result<BookSet>,
    private inline val getNextPage: suspend (BookSet) -> Page,
    private inline val onError: suspend (Throwable?) -> Unit,
    private inline val onSuccess: suspend (item: BookSet, newPage: Page) -> Unit
) {

    private var currentPage = initialPage
    private var isMakingRequest = false

    suspend fun loadNextItems() {
        if (isMakingRequest) {
            return
        }
        isMakingRequest = true
        onLoadUpdated(true)
        val result = onRequest(currentPage)
        isMakingRequest = false
        val bookSet = result.getOrElse {
            onError(it)
            onLoadUpdated(false)
            return
        }
        currentPage = getNextPage(bookSet)
        onSuccess(bookSet, currentPage)
        onLoadUpdated(false)
    }

    fun reset() {
        currentPage = initialPage
    }
}