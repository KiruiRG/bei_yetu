package com.example.projectdraft

import androidx.room.*

/*This file contains all product SQL queries*/
@Dao
/*@Dao - Tells Room that this is a DAO instance.
* DAO stands for Data Access Object. It's a piece of code that hides all the SQL work so that we
* don't have to directly write these queries in code*/
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity)
    /*This inserts a ProductEntity. It replaces if the primary key already exists*/

    @Query("SELECT * FROM ProductEntity")
    suspend fun getAllProducts(): List<ProductEntity>
    //This returns every row from ProductEntity (the table)

    @Query("""
        SELECT p.*, c.name AS catName 
        FROM ProductEntity p 
        INNER JOIN CategoriesEntity c 
        ON p.categoryId = c.id
    """)
    fun getAllProductsWithCategoryName(): List<ProductWithName>
    /*Breakdown of the JOIN:
    * SELECT p.* selects all the columns of the ProductEntity
    * c.name selects the name column in CategoriesEntity
    * AS catName means that once you select the categories name column, rename that column catName(we'll use this in the result)
    * FROM ProductEntity p treats ProductEntity as the left side of the join and gives it the short alias p
    * INNER JOIN CategoryEntity c ON p.categoryId = c.id – combines each product row with the category row whose id matches the
    * product’s categoryId. Only rows that have a matching category are returned (i.e., products without a valid category are omitted).
    * You know that INNER JOIn only returns rows that match
    * So the output will be all the columns from the table ProductEntity with an additional column catName that will be stored in
    * data class ProductWithName. I've put it in ProductEntity*/

    @Query("""
        SELECT p.*, c.name AS catName
        FROM ProductEntity p
        INNER JOIN CategoriesEntity c
        ON p.categoryId = c.id
        WHERE c.name = :categoryName
    """)
    suspend fun getProductsByCategoryName(categoryName: String): List<ProductWithName>

    @Query("SELECT COUNT(*) FROM ProductEntity")
    suspend fun countProducts(): Int
    //This counts the number of items in the products entity
}
