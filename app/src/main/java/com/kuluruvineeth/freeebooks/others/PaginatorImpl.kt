package com.kuluruvineeth.freeebooks.others

class PaginatorImpl<Page,BookSet>(
    private val initialPage: Page,
    private inline val onLoadUpdated: (Boolean) -> Unit,
    private inline val onRequest: suspend (nextPage: Page) -> Result<BookSet>,
    private inline val getNextPage: suspend (BookSet) -> Page,
    private inline val onError: suspend (Throwable?) -> Unit,
    private inline val onSuccess: suspend (items: BookSet, newPage: Page) -> Unit
) : Paginator<Page, BookSet> {

    private var currentKey = initialPage
    private var isMakingRequest = false

    override suspend fun loadNextItems() {
        if(isMakingRequest){
            return
        }
        isMakingRequest = true
        onLoadUpdated(true)
        val result = onRequest(currentKey)
        isMakingRequest = false
        val items = result.getOrElse {
            onError(it)
            onLoadUpdated(false)
            return
        }
        currentKey = getNextPage(items)
        onSuccess(items, currentKey)
        onLoadUpdated(false)
    }

    override fun reset() {
        currentKey = initialPage
    }
}