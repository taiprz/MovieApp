package com.example.movieapp

import android.animation.ValueAnimator
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import com.example.movieapp.ui.home.HomeViewModel
import com.example.movieapp.ui.splashcreen.SplashScreenViewModel
import com.example.movieapp.ui.details.DetailsView
import com.example.movieapp.ui.favorites.FavoritesView
import com.example.movieapp.ui.favorites.FavoritesViewModel
import com.example.movieapp.data.utils.Screen
import com.example.movieapp.ui.home.HomeView
import com.example.movieapp.ui.theme.Parchment
import com.example.movieapp.ui.theme.PetalFrost
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue
import kotlin.jvm.java

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val splashScreenViewModel: SplashScreenViewModel by lazy {
        ViewModelProvider(this@MainActivity)[SplashScreenViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen().apply {
            setKeepOnScreenCondition { splashScreenViewModel.isSplashScreenVisible.value }
            setOnExitAnimationListener { splash ->
                val rotationAnimator = ValueAnimator.ofFloat(0f, 90f)
                rotationAnimator.duration = 1500
                rotationAnimator.addUpdateListener {
                    splash.iconView.rotation = it.animatedValue as Float
                }
                rotationAnimator.doOnEnd { splash.remove() }
                rotationAnimator.start()
            }
        }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            Scaffold(
                bottomBar = {
                    NavigationBar {
                        val screens = listOf(Screen.Home, Screen.Favorites)
                        screens.forEach { screen ->
                            NavigationBarItem(
                                selected = navController.currentDestination?.route == screen.route,
                                onClick = {
                                    navController.navigate(screen.route) {
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                icon = {
                                    when(screen) {
                                        Screen.Home ->
                                            AsyncImage(
                                                modifier = Modifier
                                                    .size(20.dp),
                                            model = R.drawable.ic_like,
                                            contentDescription = "Home"
                                        )

                                        Screen.Favorites ->
                                            AsyncImage(
                                                modifier = Modifier
                                                    .size(20.dp),
                                            model = R.drawable.ic_heart,
                                            contentDescription = "Favorites"
                                        )
                                        else -> {}
                                    }
                                },
                                label = { Text(screen.name) }
                            )
                        }
                    }
                }
            ) { padding ->
                NavHost(
                    navController = navController,
                    startDestination = Screen.Home.route,
                    modifier = Modifier
                        .padding(padding)

                ) {
                    composable(Screen.Home.route) {
                        val homeViewModel: HomeViewModel = hiltViewModel()
                        HomeView(
                            homeViewModel = homeViewModel,
                            navController = navController
                        )
                    }

                    composable(Screen.Favorites.route) {
                        val favoritesViewModel = hiltViewModel<FavoritesViewModel>()
                        FavoritesView(
                            favoritesViewModel = favoritesViewModel,
                            navController = navController
                        )
                    }

                    composable(Screen.Details.route + "/{movieId}",
                        arguments = listOf(navArgument("movieId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val movieId = backStackEntry.arguments?.getInt("movieId") ?: 0
                        DetailsView(movieId = movieId, navController = navController)
                    }
                }
            }
        }
    }
}

