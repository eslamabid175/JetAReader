package com.eslamaped.jetareader.screens.search

import android.content.ClipData.Item
import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eslamaped.jetareader.data.Resource
import com.eslamaped.jetareader.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookSearchViewModel @Inject constructor(private val repository: BookRepository) : ViewModel(){
var list :List<com.eslamaped.jetareader.model.Item> by mutableStateOf(listOf())
var isLoading: Boolean by mutableStateOf(true)
    init {
        loadBooks()
    }

    private fun loadBooks() {
        SearchBook("flutter")
    }

  fun SearchBook(query: String) {
        viewModelScope.launch(Dispatchers.Default) {
            if (query.isEmpty()) {
                return@launch
            }
            try {
                when (val response = repository.getBooks(query)) {
            is Resource.Success -> {
                list = response.data!!
                if (list.isNotEmpty()) isLoading = false
            }
                    is Resource.Error -> {
                        isLoading = false
                        Log.e("SUCCESS_SEARCH", "SearchBook: ${response.message}")
            }
            else -> {
                isLoading = false
            }
                }
            }catch (e: Exception){
                isLoading = false
                Log.d("ERORR_SEARCH", "SearchBook: ${e.message.toString()}")
            }
        }
    }
}