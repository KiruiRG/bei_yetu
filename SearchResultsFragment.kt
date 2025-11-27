package com.example.projectdraft.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.example.projectdraft.DisplayPriceItem
import com.example.projectdraft.Product
import com.example.projectdraft.ProductDetailsFragments
import com.example.projectdraft.StorePrice
import com.example.projectdraft.ui.theme.ProjectdraftTheme

class SearchResultsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val query = arguments?.getString("SEARCH_QUERY") ?: "Default Search"
        val products = getMockProducts()

        return ComposeView(requireContext()).apply {
            setContent {
                ProjectdraftTheme {
                    SearchResultsScreen(
                        query = query,
                        products = products,
                        onItemClick = { item -> openProductDetail(item) }
                    )
                }
            }
        }
    }

    private fun openProductDetail(item: DisplayPriceItem) {
        val intent = Intent(requireContext(), ProductDetailsFragments::class.java).apply { //link to the product details page
            putExtra("PRODUCT_NAME", item.productName)
            putExtra("STORE_NAME", item.storeName)
            putExtra("PRICE", item.price)
            putExtra("URL", item.url)
        }
        startActivity(intent)
    }

    // Mock data
    private fun getMockProducts(): List<Product> {
        return listOf(
            Product(
                name = "Gaming Mouse RGB Pro",
                imageUrl = "",
                prices = listOf(
                    StorePrice("Store B", 5599.00, "https://store-b.net/mouse"),
                    StorePrice("Store A", 4999.00, "https://store-a.com/mouse-pro"),
                    StorePrice("Store C", 5150.00, "https://store-c.org/mouse-rgb")
                )
            ),
            Product(
                name = "27-inch 4K Monitor",
                imageUrl = "",
                prices = listOf(
                    StorePrice("Store A", 45099.00, "https://store-a.com/4k-monitor"),
                    StorePrice("Store D", 43500.00, "https://store-d.co/monitor-4k")
                )
            )
        )
    }
}
