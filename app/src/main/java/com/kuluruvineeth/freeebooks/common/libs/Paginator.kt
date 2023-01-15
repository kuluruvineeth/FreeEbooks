package com.kuluruvineeth.freeebooks.common.libs

interface Paginator<Key, BookSet> {

    suspend fun loadNextItems()
    fun reset()
}