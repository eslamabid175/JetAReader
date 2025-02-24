package com.eslamaped.jetareader.screens.details

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.eslamaped.jetareader.components.ReaderAppBar
import com.eslamaped.jetareader.components.RoundedButtonn
import com.eslamaped.jetareader.data.Resource
import com.eslamaped.jetareader.model.Item
import com.eslamaped.jetareader.model.MBook
import com.eslamaped.jetareader.navigation.ReaderScreens
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun ReaderBookDetailsScreen(navController: NavController,bookId:String,
                            viewModel: DetailsViewModel = hiltViewModel()) {

    Scaffold(topBar = {
       ReaderAppBar(title = "Book Details",
           icon = Icons.Default.ArrowBack,showProfile = false,navController = navController){
           navController.navigate(ReaderScreens.SearchScreen.name)
                                                                                                                              }
                      }) {innerpaddingValues ->
        Surface (modifier = Modifier
            .padding(innerpaddingValues)
            .fillMaxSize()) {
Column(modifier = Modifier.padding(top=12.dp),
        verticalArrangement = Arrangement.Top,
    horizontalAlignment = Alignment.CenterHorizontally) {

    val bookInfo = produceState<Resource<Item>>(initialValue = Resource.Loading())  {
        value = viewModel.getBookInfo(bookId)
        
    }.value

    if (bookInfo.data == null) {

        Row (){

            LinearProgressIndicator()
            Text(text = "Loading...")
        }
    } else {
      ShowBookDetails(bookInfo,navController)
    }

}
                   }

                          }
                                                                          }

@Composable
fun ShowBookDetails(bookInfo: Resource<Item>, navController: NavController) {

    val bookData = bookInfo.data?.volumeInfo
    val googleBookId = bookInfo.data?.id


    Card(modifier = Modifier.padding(34.dp),
        shape = CircleShape,
        elevation = CardDefaults.cardElevation(4.dp)) {
Image(painter = rememberAsyncImagePainter(model = bookData!!.imageLinks?.thumbnail),
    contentDescription ="book image" ,
    modifier = Modifier.width(90.dp)
        .height(90.dp).padding(1.dp))
    }


            Text(text = bookData?.title.toString(), style = MaterialTheme.typography.headlineLarge,
                overflow = TextOverflow.Ellipsis,
                maxLines = 19)
            Text(text = "Authors: ${bookData?.authors.toString()}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Page Count: ${bookData?.pageCount.toString()}", style = MaterialTheme.typography.bodyLarge)

            Text(text = "Categories: ${bookData?.categories.toString()}", style = MaterialTheme.typography.bodyLarge,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis)
            Text(text = "Published: ${bookData?.publishedDate.toString()}", style = MaterialTheme.typography.bodyLarge)

Spacer(modifier = Modifier.height(5.dp))

   val cleanDescription = HtmlCompat.fromHtml(bookData!!.description,
       HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
    val localDims= LocalContext.current.resources.displayMetrics
    Surface(modifier = Modifier
        .height(localDims.heightPixels.dp.times(0.09f))
        .padding(4.dp),
    shape = RectangleShape,
        border = BorderStroke(1.dp, Color.DarkGray)
    ) {

    LazyColumn(modifier = Modifier.padding(3.dp)){
        item {
            Text(text = cleanDescription)
        }
    }
    }

    //Buttons
    Row(modifier = Modifier.padding(top = 6.dp),
        horizontalArrangement = Arrangement.SpaceAround) {
        RoundedButtonn(label = "Save") {
            //Save this book to the firestore database
            val book = MBook(
                title = bookData.title,
                authors = bookData.authors.toString(),
                description = bookData.description,
              notes = "",
                photoUrl = bookData.imageLinks?.thumbnail,
                publishedDate = bookData.publishedDate,
                pageCount = bookData.pageCount.toString(),
                categories = bookData.categories.toString(),
                rating = 0.0,
                googleBookId = googleBookId
               ,
                userId = FirebaseAuth.getInstance().currentUser?.uid.toString()

            )
           saveToFirebase(book,navController)

         //   navController.navigate(ReaderScreens.SearchScreen.name)

        }
        Spacer(modifier = Modifier.width(25.dp))
        RoundedButtonn(label = "Cancel") {
            navController.popBackStack()
        }

    }

}

fun saveToFirebase(book: MBook,navController: NavController) {
val db=FirebaseFirestore.getInstance()
    val dbcollection=db.collection("books")
    if (book.toString().isNotEmpty()){
dbcollection.add(book)
    .addOnSuccessListener { documentRefer ->
        val docid=documentRefer.id
        dbcollection.document(docid)
            .update(hashMapOf("id" to docid) as Map<String, Any>)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    navController.popBackStack()
                }
            }.addOnFailureListener {
                Log.w("Errorr" , "SaveToFirebase: Error updating document", it)
            }
    }
    }else{

    }
}
