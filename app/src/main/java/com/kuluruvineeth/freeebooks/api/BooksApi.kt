package com.kuluruvineeth.freeebooks.api

import com.kuluruvineeth.freeebooks.api.models.AllBooks

class BooksApi {

    companion object BookApiConstants{
        const val BASE_URL = "https://gutendex.com/"
    }

    fun getAllBooks() : AllBooks {
        TODO("Not Yet Implemented")
    }

    fun getNextBookSet(books: AllBooks): AllBooks{
        TODO("Not Yet Implemented")
    }
}