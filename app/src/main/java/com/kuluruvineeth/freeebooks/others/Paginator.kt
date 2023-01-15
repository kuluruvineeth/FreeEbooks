package com.kuluruvineeth.freeebooks.others

interface Paginator<Key, BookSet> {

    suspend fun loadNextItems()
    fun reset()
}