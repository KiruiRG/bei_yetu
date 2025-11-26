package com.example.projectdraft

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

/*This file ensures that only one instance of the database is created for the whole app.
* If it didn't exist, if eg HomePage needs a Database, it would create
* an instance Database 1 then Product Details would create instance Database 2. But with
* this, all of them would use Instance Database 1. You'll notice that this is an object
* (an object is an instance of a class). So they'll all use this*/

object DatabaseProvider {
    /*This makes it a singleton (only one object exists in the entire app)*/
    private var db: AppDatabase? = null
    /*Stores the single AppDatabase instance.*/

    fun getDatabase(context: Context): AppDatabase {
        /*getDatabase() returns the database instance(AppDatabase).
        * context means a running environment. It gives you access to things like:
        * The app’s resources (strings, images, etc.).
        * The ability to start activities, services, or broadcast receivers.
        * A way to read or write files that belong to the app.
        * Think of it as a backstage pass that lets different parts of your code (activities, fragments, services) talk to the
        * Android system and the rest of the app. In a fragment you usually get it with requireContext() or from the hosting activity;
        * in an activity you can just use this.*/
        if (db == null) {
            db = Room.databaseBuilder(
                context.applicationContext,/*This ensures that the database lives as long as
                the app runs, not just one screen*/
                AppDatabase::class.java,/*Converts AppDatabase to a java class cz RoomBuilder usually expects a java class*/
                "beiyetu_db"
            )
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onOpen(db: SupportSQLiteDatabase) {
                        super.onOpen(db)
                        db.execSQL("PRAGMA foreign_keys=ON;")
                    }
                })
                .build()
        }
        return db!!
    }
}
