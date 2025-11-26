package com.example.projectdraft

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ListingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListing(listing: ProductListingEntity): Long

    @Query("""
    SELECT pl.price, s.name AS storeName, s.websiteUrl
    FROM product_listings pl
    INNER JOIN stores s ON pl.storeId = s.id
    WHERE pl.productId = :productId
    ORDER BY pl.price ASC
""")
    suspend fun getSortedListingsForProduct(productId: Int): List<StorePriceListing>
}