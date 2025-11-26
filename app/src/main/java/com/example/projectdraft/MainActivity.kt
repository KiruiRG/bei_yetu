package com.example.projectdraft

import android.R.attr.defaultValue
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.findNavController
import androidx.navigation.navArgument

import com.example.projectdraft.ProjectdraftTheme
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProjectdraftTheme {
                val navController = rememberNavController()
                val viewModel: HomeViewModel = viewModel()
                val items = listOf(
                    Screen.Home, Screen.Orders, Screen.Categories, Screen.Account
                )

                Scaffold(
                    bottomBar = {
                        BottomNavigation (
                            backgroundColor = Color.White
                        ){
                            val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
                            items.forEach { screen ->
                                BottomNavigationItem(
                                    icon = { Icon(screen.icon, contentDescription = screen.label) },
                                    label = { Text(screen.label) },
                                    selected = currentRoute == screen.route,
                                    onClick = {
                                        if (screen == Screen.Home) {
                                            navController.navigate("home?searchQuery=null") {
                                                popUpTo("home?searchQuery={searchQuery}") { saveState = false }
                                                launchSingleTop = true
                                            }
                                        } else {
                                            navController.navigate(screen.route) {
                                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        }

                                    },
                                    selectedContentColor = Color(0xFF601EF9),
                                    unselectedContentColor = Color.Gray
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "home?searchQuery={searchQuery}",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(
                            route = "home?searchQuery={searchQuery}",
                            arguments = listOf(
                                navArgument("searchQuery") {
                                    type = NavType.StringType
                                    nullable = true
                                    defaultValue = null
                                }
                            )
                        ) { backStackEntry ->
                            val searchQuery = backStackEntry.arguments?.getString("searchQuery")
                            HomePageScreen(
                                viewModel = viewModel,
                                navController = navController,
                                searchQuery = searchQuery
                            )
                        }

                        composable("productDetail/{productId}") { backStackEntry ->
                            val productId = backStackEntry.arguments?.getString("productId")?.toInt() ?: 0
                            ProductDetailScreen(productId = productId, viewModel = viewModel)
                        }

                        composable(Screen.Categories.route) {
                            CategoriesScreen(
                                viewModel = viewModel,
                                onCategoryClick = { categoryName ->
                                    navController.navigate("home?searchQuery=$categoryName") {
                                        popUpTo("home?searchQuery={searchQuery}") { saveState = false }
                                        launchSingleTop = true
                                    }
                                }
                            )
                        }

                        // Other screens
                        // composable(Screen.Orders.route) { OrdersScreen() }
                        // composable(Screen.Account.route) { AccountScreen() }
                        // composable(Screen.Statistics.route) { StatisticsScreen() }
                    }

                }
            }
        }
    }
}

/*class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent() {
            ProjectdraftTheme { // ✅ fix theme here
                /*
                val viewModel: ResultsViewModel = viewModel()
                val products by viewModel.products.collectAsState() // ✅ add correct type inference
                var showResults by remember { mutableStateOf(false) }

                if (!showResults) {
                    SearchScreen { query ->
                        viewModel.search(query)
                        showResults = true
                    }
                } //else {
                  //  ResultsScreen(products)
               // }*/

                /*val viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
                HomePageScreen(viewModel)*/

            }
        }
    }
}*/
