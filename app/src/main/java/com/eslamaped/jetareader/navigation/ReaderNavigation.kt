package com.eslamaped.jetareader.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.eslamaped.jetareader.screens.ReaderSplashScreen
import com.eslamaped.jetareader.screens.details.ReaderBookDetailsScreen
import com.eslamaped.jetareader.screens.home.Home
import com.eslamaped.jetareader.screens.home.HomeScreenViewModel
import com.eslamaped.jetareader.screens.login.ReaderLoginScreen
import com.eslamaped.jetareader.screens.search.BookSearchViewModel
import com.eslamaped.jetareader.screens.stats.ReaderStatsScreen
import com.eslamaped.jetareader.screens.update.BookUpdateScreen
import com.eslamaped.jetareader.screens.search.SearchScreen

@Composable
fun ReaderNavigation() {
val navcontroller= rememberNavController()
    NavHost(navController = navcontroller, startDestination = ReaderScreens.SplashScreen.name ){

       composable(ReaderScreens.SplashScreen.name){
        ReaderSplashScreen(navController=navcontroller)
    }
        composable(ReaderScreens.LoginScreen.name){
            ReaderLoginScreen(navController=navcontroller)
        }

        composable(ReaderScreens.ReaderStatsScreen.name){
            val homeViewModel = hiltViewModel<HomeScreenViewModel>()
            ReaderStatsScreen(navController=navcontroller,viewModel = homeViewModel)
        }

        val updateName = ReaderScreens.UpdateScreen.name
        composable("$updateName/{bookItemId}",
            arguments = listOf(navArgument("bookItemId") {
                type = NavType.StringType
            })) { navBackStackEntry ->

            navBackStackEntry.arguments?.getString("bookItemId").let {
                BookUpdateScreen(navController = navcontroller, bookItemId = it.toString())
            }

        }
        composable(ReaderScreens.ReaderHomeScreen.name){
            val homeViewModel = hiltViewModel<HomeScreenViewModel>()
            Home(navController =navcontroller,viewModel = homeViewModel)
        }


        val detailName = ReaderScreens.DetailsScreen.name
        composable("$detailName/{bookId}", arguments = listOf(navArgument("bookId"){
            type = NavType.StringType
        })){backStackEntry ->
            backStackEntry.arguments?.getString("bookId").let {
            ReaderBookDetailsScreen(navController=navcontroller,bookId=it.toString())
        }
            }
        composable(ReaderScreens.SearchScreen.name){
            val viewModel = hiltViewModel<BookSearchViewModel>()
            SearchScreen(navController=navcontroller,viewModel)
        }
    }


}