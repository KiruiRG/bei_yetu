package com.example.projectdraft

import android.graphics.Paint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.util.TableInfo

@Composable
fun ProductDetailScreen(
    productId: Int,
    viewModel: HomeViewModel = viewModel()
){
    // Trigger loading when screen opens
    LaunchedEffect(productId) {
        viewModel.loadProductDetails(productId)
    }

    val productWithListings by viewModel.productDetails.collectAsState()

    Column {
        TopBar()
        productWithListings?.let { (product, listings) ->
            ProductDetails(product = product, listings = listings)
        }
    }
}
@Composable
fun ProductDetails(
    product: ProductWithCategoryAndSubcategory,
    listings: List<StorePriceListing>
) {
    val bestListing = listings.minByOrNull { it.price }
    var commentText by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentPadding = PaddingValues(bottom = 10.dp)
        ) {
            item {
                Image(
                    painter = painterResource(product.imageRes),
                    contentDescription = product.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "${product.name} | ${product.subcategoryName}",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                bestListing?.let {
                    Text(
                        text = "Ksh ${it.price}",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                                .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp, bottom = 20.dp),
                        contentAlignment = Alignment.Center
                    ){
                        Button(
                            onClick = { /* TODO: open it.websiteUrl */ },
                            modifier = Modifier
                                .padding(top = 8.dp)
                        ) {
                            Text("Buy at ${it.storeName}")
                        }
                    }

                }

                Spacer(modifier = Modifier.height(16.dp))

                if (listings.size > 1) {
                    Text(
                        "Other Stores",
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier
                            .background(Color.DarkGray, shape = RoundedCornerShape(4.dp))
                            .padding(start = 10.dp, end = 50.dp, top = 10.dp, bottom = 10.dp)
                            .border(1.dp,Color.DarkGray, shape = RoundedCornerShape(4.dp)),
                        color = Color.White
                    )

                    listings.filter { it != bestListing }.forEach { store ->
                        Box(
                            modifier = Modifier
                                .background(Color.LightGray, shape = RoundedCornerShape(4.dp))
                                .padding(vertical = 10.dp, horizontal = 15.dp),
                        ){
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(store.storeName)
                                Text("Ksh ${store.price}")
                                Button(onClick = { /* TODO: open store.websiteUrl */ }) {
                                    Text("Buy")
                                }
                            }
                        }
                        
                    }
                }

                Spacer(modifier = Modifier.height(50.dp))

                Text(
                    "Product Description",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier
                        .background(Color.DarkGray, shape = RoundedCornerShape(4.dp))
                        .padding(start = 10.dp, end = 50.dp, top = 10.dp, bottom = 10.dp)
                        .border(1.dp,Color.DarkGray, shape = RoundedCornerShape(4.dp)),
                    color = Color.White
                )
                Text(
                    product.description ?: "No description available.",
                    modifier = Modifier
                        .background(Color.LightGray, shape = RoundedCornerShape(4.dp))
                        .padding(vertical = 10.dp, horizontal = 15.dp),
                )
            }

            item {
                Spacer(modifier = Modifier.height(50.dp))
                Text(
                    "Comment",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = commentText,
                    onValueChange = { commentText = it },
                    placeholder = { Text("Sign in to comment...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(100.dp)

                )
            }

            item {
                // Comment button anchored at bottom right
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Button(onClick = { /* TODO: handle comment */ }) {
                        Text("Comment")
                    }
                }
            }
        }

    }
}


@Preview(showBackground = true)
@Composable
fun ProductDetailScreenPreview() {
    val sampleProduct = ProductWithCategoryAndSubcategory(
        id = 1,
        name = "Samsung 55\" TV",
        subcategoryName = "Televisions",
        categoryName = "Electronics",
        imageRes = R.drawable.test_tv,
        price = 55000.00,
        description = "Experience stunning 4K clarity with the Samsung UHD TV. Powered by the Crystal Processor 4K Engine."
    )

    val sampleListings = listOf(
        StorePriceListing(price = 57999.0, storeName = "Naivas", websiteUrl = "https://naivas.online"),
        StorePriceListing(price = 58500.0, storeName = "Quick Mart", websiteUrl = "https://quickmart.co.ke"),
        StorePriceListing(price = 60999.0, storeName = "Carrefour", websiteUrl = "https://carrefour.ke")
    )

    Column {
        TopBar()
        ProductDetails(product = sampleProduct, listings = sampleListings)
    }
}

