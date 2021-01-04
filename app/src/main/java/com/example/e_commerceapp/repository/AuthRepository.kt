package com.example.e_commerceapp.repository

import com.example.e_commerceapp.api.AuthApi
import com.example.e_commerceapp.util.UserPreferences
import retrofit2.Retrofit
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val api: AuthApi,
    private val preferences: UserPreferences
): BaseRepository() {

    suspend fun register(
        name : String,
        email : String,
        password: String
    ) = safeApiCall {
        api.register(name, email, password)
    }

    suspend fun login(
        email : String,
        password: String
    ) = safeApiCall {
        api.login(email, password)
    }

    suspend fun logout() = safeApiCall {
        api.login()
    }

    suspend fun saveAuthToken(token: String){
        preferences.saveAuthToken(token)
    }

    suspend fun deleteAuthToken(){
        preferences.clearPreferenceStorage()
    }
}