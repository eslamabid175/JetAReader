package com.eslamaped.jetareader.di

import com.eslamaped.jetareader.network.BooksApi
import com.eslamaped.jetareader.repository.FireRepository
import com.eslamaped.jetareader.utils.Constants
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

@Singleton
@Provides
fun provideFireBookRepository()= FireRepository(queryBook = FirebaseFirestore
    .getInstance().collection("books"))

//    @Singleton
//    @Provides
//fun provideBookRepository(api:BooksApi)= BookRepository(api)

    @Singleton
    @Provides
    fun provideBookApi():BooksApi{
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BooksApi::class.java)
    }

}