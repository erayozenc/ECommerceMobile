package com.example.e_commerceapp.api

import com.example.e_commerceapp.responses.categories.CategoryListResponse
import com.example.e_commerceapp.responses.categories.CategoryResponse
import retrofit2.http.*

interface CategoryApi {

    @FormUrlEncoded
    @POST("api/categories/")
    suspend fun addCategory(
            @Field("name") name: String
    ) : CategoryResponse


    @GET("api/categories/all")
    suspend fun getCategoryList(
            @Header("auth-token") authToken: String?
    ) : CategoryListResponse

    @GET("api/categories/{id}")
    suspend fun getCategory(
            @Path("id") id: String,
            @Header("auth-token") authToken: String?
    ) : CategoryResponse
}