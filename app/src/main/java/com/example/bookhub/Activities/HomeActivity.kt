package com.example.bookhub
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bookhub.Activities.ui.theme.BookHubTheme
import com.example.bookhub.Components.InfoScreen
import com.example.bookhub.Navigation.MyBottomBar
import com.example.bookhub.Navigation.MyTopBar
import com.example.bookhub.Navigation.NavItem
import com.example.bookhub.Navigation.Screen
import com.example.bookhub.Screens.SplashScreen
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

class HomeActivity : ComponentActivity() {

    // Get the database instance (AppDatabase)
    private val database by lazy {
        AppDatabase.getDatabase(applicationContext) // Assuming you have a method to get the database instance
    }

    // Initialize the ViewModel with the database instance using ViewModelProvider
    private val viewModel: BooksViewModel by viewModels {
        BooksViewModelFactory(database)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            BookHubTheme {

                // Initialize navController
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = Screen.Splash.route) {

                    // Splash Screen
                    composable(Screen.Splash.route) {
                        SplashScreen {
                            navController.navigate(Screen.Main.route) {
                                popUpTo(Screen.Splash.route) { inclusive = true } // Remove Splash from back stack
                            }
                        }
                    }

                    // Main Screen
                    composable(Screen.Main.route) {
                        MainScreenContent(viewModel, navController) // Pass navController to MainScreenContent
                    }

                    // Saved Books Screen
                    composable(Screen.SavedBooks.route) {
                        SavedBooksScreen(viewModel, navController) // Pass navController to MainScreenContent
                    }


                    composable(
                        route = "bookDetails/{id}/{bookTitle}/{bookAuthor}/{bookDescription}/{bookImageUrl}/{bookUrl}",
                        arguments = listOf(
                            navArgument("id") { type = NavType.StringType },
                            navArgument("bookTitle") { type = NavType.StringType },
                            navArgument("bookAuthor") { type = NavType.StringType },
                            navArgument("bookDescription") { type = NavType.StringType },
                            navArgument("bookImageUrl") { type = NavType.StringType },
                            navArgument("bookUrl") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        val id = URLDecoder.decode(backStackEntry.arguments?.getString("id") ?: "", StandardCharsets.UTF_8.toString())
                        val bookTitle = URLDecoder.decode(backStackEntry.arguments?.getString("bookTitle") ?: "", StandardCharsets.UTF_8.toString())
                        val bookAuthor = URLDecoder.decode(backStackEntry.arguments?.getString("bookAuthor") ?: "", StandardCharsets.UTF_8.toString())
                        val bookDescription = URLDecoder.decode(backStackEntry.arguments?.getString("bookDescription") ?: "", StandardCharsets.UTF_8.toString())
                        val bookImageUrl = URLDecoder.decode(backStackEntry.arguments?.getString("bookImageUrl") ?: "", StandardCharsets.UTF_8.toString())
                        val bookUrl = URLDecoder.decode(backStackEntry.arguments?.getString("bookUrl") ?: "", StandardCharsets.UTF_8.toString())

                        // Display book details
                        BookDetailsScreen(id, bookTitle, bookAuthor, bookDescription, bookImageUrl, bookUrl, navController, viewModel)
                    }

              }
            }
        }
    }
}



@Composable
fun MainScreenContent(viewModel: BooksViewModel, navController: NavController) {
    val selectedScreen = remember { mutableStateOf<NavItem>(NavItem.Library) }

    Scaffold(
        topBar = {
            MyTopBar(
                onInfoClick = { selectedScreen.value = NavItem.AppInfo },
                isInfoScreen = selectedScreen.value is NavItem.AppInfo // Pass boolean flag
            )
        },
        bottomBar = {
            MyBottomBar(selectedScreen)
        },

    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(innerPadding)
        ) {
            when (selectedScreen.value) {
                NavItem.AppInfo -> InfoScreen()
                NavItem.Library -> Library(viewModel, navController)
                NavItem.SavedBooks -> SavedBooksScreen(viewModel,navController)
            }
        }
    }
}

