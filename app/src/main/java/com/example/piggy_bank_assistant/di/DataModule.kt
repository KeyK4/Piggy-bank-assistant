package com.example.piggy_bank_assistant.di

import android.app.Application
import com.example.piggy_bank_assistant.data.roomdb.CategoriesAndPatternsDao
import com.example.piggy_bank_assistant.data.roomdb.CategoriesAndPatternsDatabase
import com.example.piggy_bank_assistant.data.roomdb.RepositoryImpl
import com.example.piggy_bank_assistant.domain.Repository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @ApplicationScope
    @Binds
    fun bindShopListRepository(impl: RepositoryImpl): Repository

    companion object {

        @ApplicationScope
        @Provides
        fun provideShopListDao(
            application: Application
        ): CategoriesAndPatternsDao {
            return CategoriesAndPatternsDatabase.getInstance(application).getCategoriesAndPatternsDao()
        }
    }
}