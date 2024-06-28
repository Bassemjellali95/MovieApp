package com.example.moviesapp.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.moviesapp.ui.presentation.MovieDetailsScreen
import com.example.moviesapp.ui.presentation.MoviesScreen
import com.example.nearbuy.navigation.Screen

@Composable
fun SetupNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.MOVIES.route,
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(300)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(300)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(300)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(300)
            )
        }
    ) {
        composable(
            route = Screen.MOVIES.route,
            content = { MoviesScreen(navController = navController) }
        )
        composable(
            Screen.DETAILS.route + "/{movieId}",
            arguments = listOf(
                navArgument(name = "movieId") {
                    type = NavType.IntType
                    defaultValue = 0
                },
            )
        ) { navBackStackEntry ->
            MovieDetailsScreen(
                navController = navController,
                movieId = navBackStackEntry.arguments?.getInt("movieId"),
            )
        }
    }
}