package com.example.projectdraft

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
fun CategoriesScreen(
    viewModel: HomeViewModel,
    onCategoryClick: (String) -> Unit
) {
    // Collect the shared flow once
    val products by viewModel.products.collectAsState(initial = emptyList())

    Column {
        TopBar()
        GroupedCategoriesLazy(products, onCategoryClick)
    }
}

@Composable
fun GroupedCategoriesLazy(
    products: List<ProductWithCategoryAndSubcategory>,
    onCategoryClick: (String) -> Unit
){

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
                    modifier = Modifier
                        .padding(bottom = 2.dp)
                        .clickable { onCategoryClick(catName) },
                    fontWeight = FontWeight.ExtraBold
                )
            }

            item {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(productsInCat) { product ->
                        CategoryItem(
                            icon = product.imageRes,
                            name = product.subcategoryName,
                            onClick = { onCategoryClick(catName) }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

        }

    }
}

@Composable
fun CategoryItem(icon: Int, name: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .size(width = 90.dp, height = 140.dp)
            .border(1.5.dp, Color.LightGray, MaterialTheme.shapes.medium)
            .clickable { onClick() }, // 👈 make it clickable
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(icon),
            contentDescription = "Category Icon",
            modifier = Modifier.size(90.dp)
        )

        Text(
            text = name,
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

            val sampleProducts = listOf(
                ProductWithCategoryAndSubcategory(id = 1, name = "Fridge", price = 799.99, categoryName = "Electronics", subcategoryName = "Fridges", imageRes = R.drawable.test_fridge , description = "This is a very powerful fridge"),
                ProductWithCategoryAndSubcategory(id =2, name = "Washing Machine", price =999.99, categoryName = "Electronics", subcategoryName = "Washing Machines", imageRes = R.drawable.test_washm, description = null) ,
                ProductWithCategoryAndSubcategory(id =3, name = "Samsung TV", price =999.99, categoryName = "Electronics", subcategoryName = "Televisions", imageRes = R.drawable.test_tv, description = null),
                ProductWithCategoryAndSubcategory(id =4, name = "Ramtons Blender", price =999.99, categoryName = "Electronics", subcategoryName = "Blenders", imageRes = R.drawable.test_blender, description = null),
                ProductWithCategoryAndSubcategory(id =5, name = "Bread",price = 5.99, categoryName = "Pastries", subcategoryName = "Bread", imageRes = R.drawable.test_bread, description = null),
                ProductWithCategoryAndSubcategory(id =6, name = "Brookside Milk",price = 6.99, categoryName = "Drinks", subcategoryName = "Milk" , imageRes = R.drawable.test_milk, description = null),
            )
            GroupedCategoriesLazy(
                products = sampleProducts,
                onCategoryClick = { clickedCategory ->
                    // For preview, just print/log or ignore
                    println("Clicked category: $clickedCategory")
                }
            )
        }
    }
}