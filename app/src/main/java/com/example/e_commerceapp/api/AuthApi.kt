package com.example.e_commerceapp.api

import com.example.e_commerceapp.responses.auth.AuthResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {

    @FormUrlEncoded
    @POST("api/user/register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ) : AuthResponse

    @FormUrlEncoded
    @POST("api/user/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ) : AuthResponse

    @FormUrlEncoded
    @POST("api/user/login")
    suspend fun login() : ResponseBody
}