package com.kuluruvineeth.freeebooks.api.models

import com.google.gson.annotations.SerializedName

data class Translator(
    @SerializedName("birth_year")
    val birthYear: Int,
    @SerializedName("death_year")
    val deathYear: Int,
    @SerializedName("name")
    val name: String = "N/A"
)