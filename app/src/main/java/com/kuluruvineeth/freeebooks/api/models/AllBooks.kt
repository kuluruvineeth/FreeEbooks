package com.kuluruvineeth.freeebooks.api.models

import com.google.gson.annotations.SerializedName

data class AllBooks(
    @SerializedName("count")
    val count: Int,
    @SerializedName("next")
    val next: String,
    @SerializedName("previous")
    val previous: Any,
    @SerializedName("results")
    val results: List<Book>
)