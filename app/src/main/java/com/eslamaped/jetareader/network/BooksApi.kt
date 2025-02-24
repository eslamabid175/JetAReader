package com.eslamaped.jetareader.network
import com.eslamaped.jetareader.model.Book
import com.eslamaped.jetareader.model.Item
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface BooksApi {

    @GET("volumes")
suspend fun getAllBooks(@Query("q") query: String):Book

    @GET("volumes/{bookId}")
 suspend fun getBookInfo(@Path("bookId") bookId: String): Item


}