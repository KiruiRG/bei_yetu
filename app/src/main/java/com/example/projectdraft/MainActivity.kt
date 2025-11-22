package com.example.projectdraft

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projectdraft.ProjectdraftTheme
 // ✅ use your actual theme name


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProjectdraftTheme { // ✅ fix theme here
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
               // }
            }
        }
    }
}
