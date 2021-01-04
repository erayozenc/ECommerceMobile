package com.example.e_commerceapp.di

import com.example.e_commerceapp.api.AuthApi
import com.example.e_commerceapp.api.CategoryApi
import com.example.e_commerceapp.api.ProductApi
import com.example.e_commerceapp.db.CartDao
import com.example.e_commerceapp.repository.AuthRepository
import com.example.e_commerceapp.repository.CategoryRepository
import com.example.e_commerceapp.repository.ProductRepository
import com.example.e_commerceapp.util.UserPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideAuthRepository(
        api: AuthApi,
        preferences: UserPreferences
    ) : AuthRepository{
        return AuthRepository(api,preferences)
    }

    @Singleton
    @Provides
    fun provideProductRepository(
        api: ProductApi,
        dao: CartDao,
        preferences: UserPreferences
    ) : ProductRepository{
        return ProductRepository(api,dao,preferences)
    }

    @Singleton
    @Provides
    fun provideCategoryRepository(
            api: CategoryApi,
            preferences: UserPreferences
    ) : CategoryRepository{
        return CategoryRepository(api,preferences)
    }
}