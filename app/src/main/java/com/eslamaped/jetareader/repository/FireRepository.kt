package com.eslamaped.jetareader.repository

import com.eslamaped.jetareader.data.DataOrExc
import com.eslamaped.jetareader.model.MBook
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FireRepository @Inject constructor(
    private val queryBook :Query
) {
    suspend fun getAllBooksFromDatabase() : DataOrExc<List<MBook>, Boolean, Exception>{
        val dataOrExc = DataOrExc<List<MBook>, Boolean, Exception>()
try {
dataOrExc.loading=true
    dataOrExc.data=queryBook.get().await().documents.map{documentSnapshot->
        documentSnapshot.toObject(MBook::class.java)!!

    }
    if (!dataOrExc.data.isNullOrEmpty()) dataOrExc.loading=false
}catch (e: FirebaseFirestoreException){
dataOrExc.e=e
}
return dataOrExc
    }

}