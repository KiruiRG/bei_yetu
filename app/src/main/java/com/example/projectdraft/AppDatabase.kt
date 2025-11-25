package com.example.projectdraft

import androidx.room.Database
import androidx.room.RoomDatabase

/*This file contains the room database*/

@Database(
    entities = [ProductEntity::class, CategoriesEntity::class],
    version = 2 /*This means the number of changes in the database schema so every time you make a change of the structure
    of the database, you have to update her. For example, I had only put the ProductEntity here but now I've added the
    CategoriesEntity so we need to increment by 1. Please note that you don't increment when you add a Dao below*/
)
abstract class AppDatabase : RoomDatabase() {
    /*Above, AppDatabase must be an abstract class that extends RoomDatabase. Room will generate a  concrete implementation at
     compile time.
     Inside the class you declare abstract functions that return a DAO interface. Each DAO gives you the queries you wrote,
      and Room wires them up to the generated database instance:*/
    abstract fun productDao(): ProductDao
    abstract fun categoriesDao(): CategoriesDao
}
