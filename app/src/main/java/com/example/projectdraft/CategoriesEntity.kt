package com.example.projectdraft

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CategoriesEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String
)