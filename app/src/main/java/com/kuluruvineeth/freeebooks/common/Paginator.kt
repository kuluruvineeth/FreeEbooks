package com.kuluruvineeth.freeebooks.common

interface Paginator<Key, Item> {

    suspend fun loadNextItems()
    fun reset()
}