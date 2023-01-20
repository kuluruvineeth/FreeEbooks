package com.kuluruvineeth.freeebooks.di

import android.content.Context
import com.kuluruvineeth.freeebooks.database.FreeebooksDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class MainModule {

    @Provides
    fun provideAppContext(@ApplicationContext context: Context) = context

    @Singleton
    @Provides
    fun provideFreeebooksDatabase(@ApplicationContext context: Context) =
        FreeebooksDatabase.getInstance(context)

    @Provides
    fun provideLibraryDao(freeebooksDatabase: FreeebooksDatabase) = freeebooksDatabase.getLibraryDao()
}