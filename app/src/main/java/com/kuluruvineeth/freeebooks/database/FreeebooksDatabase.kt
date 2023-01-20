package com.kuluruvineeth.freeebooks.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kuluruvineeth.freeebooks.others.Constants


@Database(entities = [LibraryItem::class], version = 1, exportSchema = true)
abstract class FreeebooksDatabase : RoomDatabase()
{
    abstract fun getLibraryDao(): LibraryDao

    companion object{

        @Volatile
        private var INSTANCE: FreeebooksDatabase? = null

        fun getInstance(context: Context): FreeebooksDatabase{
            /*
            if the INSTANCE is not null, then return it,
            if it is, then create the database and save
            in instance variable then return it.
            */
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FreeebooksDatabase::class.java,
                    Constants.DATABASE_NAME
                ).build()
                INSTANCE = instance
                //return instance
                instance
            }
        }
    }
}