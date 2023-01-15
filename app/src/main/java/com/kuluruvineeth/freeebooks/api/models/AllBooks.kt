package com.kuluruvineeth.freeebooks.api.models

import com.google.gson.annotations.SerializedName

data class BookSet(
    @SerializedName("count")
    val count: Int,
    @SerializedName("next")
    val next: String?,
    @SerializedName("previous")
    val previous: String?,
    @SerializedName("results")
    val books: List<Book>
)