package com.eslamaped.jetareader.screens.details

import androidx.lifecycle.ViewModel
import com.eslamaped.jetareader.data.Resource
import com.eslamaped.jetareader.model.Item
import com.eslamaped.jetareader.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val repository: BookRepository)
    : ViewModel(){
suspend fun getBookInfo(bookId: String): Resource<Item>{
return  repository.getBookInfo(bookId)
}

}