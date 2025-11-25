package com.example.projectdraft

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CategoriesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category : CategoriesEntity)

    @Query("SELECT * FROM CategoriesEntity")
    suspend fun getallCategories(): List<CategoriesEntity>

    @Query("SELECT COUNT(*) FROM CategoriesEntity")
    suspend fun countCategories(): Int

}