package com.example.e_commerceapp.repository

import com.example.e_commerceapp.api.CategoryApi
import com.example.e_commerceapp.api.ProductApi
import com.example.e_commerceapp.util.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CategoryRepository @Inject constructor(
    private val api : CategoryApi,
    private val preferences: UserPreferences
) : BaseRepository(){

    suspend fun saveCategory(
        name : String
    ) = safeApiCall {
        api.addCategory(name)
    }

    suspend fun getCategoryList(
        authToken: String?
    ) = safeApiCall {
        api.getCategoryList(authToken)
    }

    suspend fun getCategory(authToken: String?,id: String) = safeApiCall {
        api.getCategory(id,authToken)
    }

    suspend fun getAuthToken() = preferences.authToken.first()
}