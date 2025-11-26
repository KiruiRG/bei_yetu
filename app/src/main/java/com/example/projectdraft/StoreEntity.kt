package com.example.projectdraft

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stores")
data class StoreEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val websiteUrl: String // optional: for linking to store
)