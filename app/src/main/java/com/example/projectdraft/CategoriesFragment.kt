package com.example.projectdraft

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

class CategoriesFragment : Fragment() {

    // Grab the activity‑scoped instance
    private val viewModel by lazy {
        ViewModelProvider(requireActivity())[HomeViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        return ComposeView(requireContext()).apply {
            setContent {
                ProjectdraftTheme {
                    Surface {
                        // Collect the products once and pass them to the screen
                        val products by viewModel.products.collectAsState(initial = emptyList())
                        /*CategoriesScreen(products = products)*/
                    }
                }
            }
        }
    }
}

// Data classes for categories
data class CategoryItem(val name: String, val imageRes: Int, val label: String)
data class Category(val title: String, val items: List<CategoryItem>)

@Composable
fun CategoriesScreen(viewModel: HomeViewModel) {
    // Collect the shared flow once
    val products by viewModel.products.collectAsState(initial = emptyList())

    Column {
        TopBar()
        GroupedCategoriesLazy(products)
    }
}

@Composable
fun GroupedCategoriesLazy(products: List<ProductWithName>){

    val byCategory = products.groupBy { it.categoryName }

    LazyColumn (
        modifier = Modifier
            .padding(horizontal = 30.dp, vertical = 15.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ){
        byCategory.forEach { (catName, productsInCat) ->
            /*The groupBy above creates a map with the categoryName as the key and all the
            * data in the table as the value. So Kotlin automatically knows that catName is
            * equivalent to the value of the key and productsInCat is the values*/
            item {
                Text(
                    text = catName,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
            }

            // Show up to 3 items per row
            val rows = productsInCat.chunked(3)
            rows.forEach { row ->
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        row.forEach { product ->
                            CategoryItem(icon = product.imageRes, name = product.name)
                        }
                        // optional: add empty placeholders if you want a fixed 3‑slot width
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

        }

    }
}

@Composable
fun CategoryItem(icon: Int, name : String){
    Column(
        modifier = Modifier
            .size(width = 90.dp, height = 140.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Image(
            painter = painterResource(icon),
            contentDescription = "Category Icon",
            modifier = Modifier.size(90.dp)
        )

        Text(
            text = "$name",
            textAlign = TextAlign.Center
        )
    }
}

@Preview(
    name = "Light Mode",
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    name = "Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun previewCategoriesScreen() {
    ProjectdraftTheme {
        Surface (modifier = Modifier.fillMaxSize()){
            GroupedCategoriesLazy(
                listOf(
                    ProductWithName(1, "Fridge", 799.99,1, "Electronics",R.drawable.test_fridge ),
                    ProductWithName(2, "Washing Machine", 999.99,1, "Electronics",R.drawable.test_washm) ,
                    ProductWithName(3, "Samsung TV", 999.99,1, "Electronics",R.drawable.test_tv, ),
                    ProductWithName(4, "Ramtons Blender", 999.99,1, "Electronics",R.drawable.test_blender),
                    ProductWithName(5, "Bread",5.99, 2,"Pastries", R.drawable.test_bread),
                    ProductWithName(6, "Brookside Milk",6.99, 3,"Drinks", R.drawable.test_milk),
                )
            )
        }
    }
}