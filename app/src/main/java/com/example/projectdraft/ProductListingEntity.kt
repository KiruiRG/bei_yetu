package com.example.projectdraft

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "product_listings",
    foreignKeys = [
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["id"],
            childColumns = ["productId"]
        ),
        ForeignKey(entity = StoreEntity::class, parentColumns = ["id"], childColumns = ["storeId"])
    ],
    indices = [Index("productId"), Index("storeId")]
)


data class ProductListingEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val productId: Int,
    val storeId: Int,
    val price: Double
)

/**
 * This lets you store:
 * One product (e.g. Samsung Fridge)
 * Multiple listings: CityStore → 57,999, SkySoko → 58,500, etc.
 */

data class StorePriceListing(
    val price: Double,
    val storeName: String,
    val websiteUrl: String
)