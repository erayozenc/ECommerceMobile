package com.example.e_commerceapp.di

import com.example.e_commerceapp.api.AuthApi
import com.example.e_commerceapp.api.CategoryApi
import com.example.e_commerceapp.api.ProductApi
import com.example.e_commerceapp.util.Constants.BASE_URL
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideGsonBuilder() : Gson{
        return GsonBuilder().create()
    }

    @Provides
    @Singleton
    fun provideRetrofit(gson : Gson) : Retrofit.Builder{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
    }

    @Provides
    @Singleton
    fun provideAuthApi(retrofit : Retrofit.Builder) : AuthApi{
        return retrofit.build().create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideProductApi(retrofit : Retrofit.Builder) : ProductApi{
        return retrofit.build().create(ProductApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCategoryApi(retrofit : Retrofit.Builder) : CategoryApi {
        return retrofit.build().create(CategoryApi::class.java)
    }
}