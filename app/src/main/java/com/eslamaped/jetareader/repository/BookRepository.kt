package com.eslamaped.jetareader.repository

import com.eslamaped.jetareader.data.Resource
import com.eslamaped.jetareader.model.Item
import com.eslamaped.jetareader.network.BooksApi
import javax.inject.Inject
import kotlin.Exception

class BookRepository @Inject constructor(private val api: BooksApi) {


    //
// val dataOrException =    DataOrExeption<List<Item>,Boolean,Exception>()
// val bookInfoDataOrException =DataOrExeption<Item,Boolean,Exception>()
    suspend fun getBooks(searchQuery: String): Resource<List<Item>> {

        return try {
            Resource.Loading(data = true)

            val itemList = api.getAllBooks(searchQuery).items

            if (itemList.isNotEmpty()) Resource.Loading(false)
            Resource.Success(data = itemList)

        } catch (e: Exception) {
            Resource.Error(message = e.message.toString())
        }
    }

    suspend fun getBookInfo(bookId: String): Resource<Item> {
        val response = try {
            Resource.Loading(true)
            api.getBookInfo(bookId)

        } catch (exception: Exception) {
            return Resource.Error(message = "An error occurred ${exception.message.toString()}")
        }
        Resource.Loading(false)
        return Resource.Success(data = response)
    }
}
