package com.example.projectdraft

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import com.example.projectdraft.ui.theme.ProjectdraftTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider

class SearchResultsFragment : Fragment() {

    private val viewModel by lazy {
        ViewModelProvider(requireActivity())[HomeViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val query = arguments?.getString("query") ?: ""

        return ComposeView(requireContext()).apply {
            setContent {
                ProjectdraftTheme {
                    Surface {
                        val products by viewModel.products.collectAsState(initial = emptyList())
                        SearchScreen(products = products, query = query)
                    }
                }
            }
        }
    }
}

@Composable
fun SearchScreen(products: List<ProductWithCategoryAndSubcategory>, query: String) {
    val filteredProducts = products.filter {
        it.name.contains(query, ignoreCase = true) ||
                it.categoryName.contains(query, ignoreCase = true) ||
                it.subcategoryName.contains(query, ignoreCase = true)
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Search results for \"$query\"",
        style = MaterialTheme.typography.titleLarge.copy(fontSize = 16.sp),
        modifier = Modifier.padding(bottom = 12.dp)
        )

        if (filteredProducts.isEmpty()) {
            Text("No results found.")
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(filteredProducts) { product ->
                    ProductItem(product)
                }
            }
        }
    }
}

@Composable
fun ProductItem(product: ProductWithCategoryAndSubcategory) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.LightGray)
            .padding(12.dp)
    ) {
        Image(
            painter = painterResource(product.imageRes),
            contentDescription = product.name,
            modifier = Modifier.size(80.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(product.name, fontWeight = FontWeight.Bold)
            Text("Price: ${product.price}")
            Text(product.categoryName)
        }
    }
}
@Preview(
    name = "Search Screen Light",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true
)
@Preview(
    name = "Search Screen Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
fun PreviewSearchScreen() {
    val sampleProducts = listOf(
        ProductWithCategoryAndSubcategory(
            id = 1,
            name = "Samsung TV",
            price = 90000.0,
            categoryName = "Electronics",
            subcategoryName = "Televisions",
            imageRes = R.drawable.test_tv,
            description = null
        ),
        ProductWithCategoryAndSubcategory(
            id = 2,
            name = "Hisense TV",
            price = 12999.0,
            categoryName = "Electronics",
            subcategoryName = "Televisions",
            imageRes = R.drawable.test_tv,
            description = null
        ),
        ProductWithCategoryAndSubcategory(
            id = 3,
            name = "LG OLED TV",
            price = 79999.0,
            categoryName = "Electronics",
            subcategoryName = "Televisions",
            imageRes = R.drawable.test_tv,
            description = null
        ),
        ProductWithCategoryAndSubcategory(
            id = 3,
            name = "Sony Bravia TV",
            price = 85999.0,
            categoryName = "Electronics",
            subcategoryName = "Televisions",
            imageRes = R.drawable.test_tv,
            description = null
        ),
        ProductWithCategoryAndSubcategory(
            id = 3,
            name = "TCL TV",
            price = 60000.0,
            categoryName = "Electronics",
            subcategoryName = "Televisions",
            imageRes = R.drawable.test_tv,
            description = null
        ),
        ProductWithCategoryAndSubcategory(
            id = 3,
            name = "Vitron TV",
            price = 55000.0,
            categoryName = "Electronics",
            subcategoryName = "Televisions",
            imageRes = R.drawable.test_tv,
            description = null
        ),
        ProductWithCategoryAndSubcategory(
            id = 3,
            name = "Haier TV",
            price = 67500.0,
            categoryName = "Electronics",
            subcategoryName = "Televisions",
            imageRes = R.drawable.test_tv,
            description = null
        )
    )

    ProjectdraftTheme {
        Surface {
            SearchScreen(products = sampleProducts, query = "TV")
        }
    }
}
