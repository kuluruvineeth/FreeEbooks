package com.kuluruvineeth.freeebooks.api

import android.util.Log
import com.google.gson.Gson
import com.kuluruvineeth.freeebooks.BuildConfig
import com.kuluruvineeth.freeebooks.api.models.BookSet
import com.kuluruvineeth.freeebooks.api.models.ExtraInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.net.URLEncoder
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class BooksApi {

    companion object BookApiConstants{
        const val BASE_URL = "https://gutendex.com/books"
        const val GOOGLE_BOOKS_URL = "https://www.googleapis.com/books/v1/volumes"
        val GOOGLE_API_KEY = BuildConfig.API_KEY
    }

    private val okHttpClient = OkHttpClient()
    private val gsonClient = Gson()


    suspend fun getAllBooks(page: Long): Result<BookSet>{
        val request = Request.Builder().get().url("${BASE_URL}?page=$page").build()
        return makeApiRequest(request)
    }

    suspend fun getBookDescription(bookName: String){

    }

    suspend fun getExtraInfo(bookName: String): ExtraInfo? = suspendCoroutine { continuation ->
        val encodedName = URLEncoder.encode(bookName,"UTF-8")
        val url = "${GOOGLE_BOOKS_URL}?q=$encodedName&startIndex=0&maxResults=1&apiKey=$GOOGLE_API_KEY"
        val request = Request.Builder().get().url(url).build()

        okHttpClient.newCall(request).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                continuation.resumeWithException(e)
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    continuation.resume(
                        parseExtraInfoJson(response.body!!.toString())
                    )
                }
            }
        })
    }

    fun parseExtraInfoJson(jsonString: String): ExtraInfo? {
        val jsonObj = JSONObject(jsonString)
        val totalItems = jsonObj.getInt("totalItems")
        return if (totalItems != 0) {
            val items = jsonObj.getJSONArray("items")
            val item = items.getJSONObject(0)
            val volumeInfo = item.getJSONObject("volumeInfo")
            val imageLinks = volumeInfo.getJSONObject("imageLinks")
            // Build Extra info.
            val coverImage = imageLinks.getString("thumbnail")
            val pageCount = volumeInfo.getInt("pageCount")
            val description = volumeInfo.getString("description")
            ExtraInfo(coverImage, pageCount, description)
        } else {
            null
        }
    }

    suspend fun searchBooks(query: String): Result<BookSet>{
        val encodedString = withContext(Dispatchers.IO){
            URLEncoder.encode(query, "UTF-8")
        }
        val request = Request.Builder().get().url("${BASE_URL}?search=$encodedString").build()
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