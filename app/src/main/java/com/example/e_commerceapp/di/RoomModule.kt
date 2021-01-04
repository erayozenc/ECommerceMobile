package com.example.e_commerceapp.di

import android.content.Context
import androidx.room.Room
import com.example.e_commerceapp.db.CartDatabase
import com.example.e_commerceapp.util.Constants.CART_DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun provideCartDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
            app,
            CartDatabase::class.java,
            CART_DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideCartDao(db: CartDatabase) = db.getDao()
}