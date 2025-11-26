package com.example.projectdraft

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector


sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Home : Screen("home", "Home", Icons.Default.Home)
    object Orders : Screen("orders", "My Order", Icons.Default.ShoppingCart)
    object Categories : Screen("categories", "Categories", Icons.Default.List)
    object Account : Screen("account", "Account", Icons.Default.Person)
    //object Statistics : Screen("statistics", "Statistics", Icons.Default.BarChart)
}
