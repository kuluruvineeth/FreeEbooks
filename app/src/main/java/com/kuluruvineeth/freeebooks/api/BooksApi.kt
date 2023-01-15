package com.kuluruvineeth.freeebooks.api

import android.util.Log
import com.google.gson.Gson
import com.kuluruvineeth.freeebooks.api.models.BookSet
import okhttp3.*
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class BooksApi {

    companion object BookApiConstants{
        const val BASE_URL = "https://gutendex.com/books"
    }

    private val okHttpClient = OkHttpClient()
    private val gsonClient = Gson()


    suspend fun getAllBooks(page: Long): Result<BookSet>{
        val request = Request.Builder().get().url("${BASE_URL}?page=$page").build()
        return makeApiRequest(request)
    }

    private suspend fun makeApiRequest(request: Request): Result<BookSet> =
        suspendCoroutine { continuation ->
            okHttpClient.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    continuation.resumeWithException(e)
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    response.use {
                        continuation.resume(
                            Result.success(
                                gsonClient.fromJson(
                                    response.body!!.string(),
                                    BookSet::class.java
                                )
                            )
                        )
                    }
                    Log.d("RESPONSE : ",response.body.toString())
                }
            })
        }
}