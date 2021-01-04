package com.example.e_commerceapp.api

import com.example.e_commerceapp.responses.GeneralResponse
import com.example.e_commerceapp.responses.auth.AuthResponse
import com.example.e_commerceapp.responses.products.ProductListResponse
import com.example.e_commerceapp.responses.products.ProductResponse
import com.example.e_commerceapp.responses.products.upload.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*

interface ProductApi {

    @Multipart
    @POST("api/products/")
    suspend fun addProduct(
        @Header("auth-token") authToken: String?,
        @Part image: MultipartBody.Part,
        @Part("name") name: RequestBody,
        @Part("description") description: RequestBody,
        @Part("price") price: RequestBody,
        @Part("category") categoryId: RequestBody,
        @Part("quantity") quantity: RequestBody,
    ) : UploadResponse


    @GET("api/products/list")
    suspend fun getProductList(
        @Header("auth-token") authToken: String?,
        @Query("order") order: String?,
        @Query("sortBy") sortBy: String?,
        @Query("limit") limit: Int?
    ) : ProductListResponse


    @GET("api/products/search")
    suspend fun getSearchProductList(
        @Header("auth-token") authToken: String?,
        @Query("search") search: String?,
        @Query("category") category: String?,
    ) : ProductListResponse

    @GET("api/products/{id}")
    suspend fun getProduct(
        @Header("auth-token") authToken: String?,
        @Path("id") id: String
    ) : ProductResponse

    @GET("api/favorites")
    suspend fun getFavoriteProducts(
            @Header("auth-token") authToken: String?
    ) : ProductListResponse

    @GET("api/favorites/user")
    suspend fun getFavoriteProductIds(
            @Header("auth-token") authToken: String?
    ) : List<String>

    @POST("api/favorites/add/{id}")
    suspend fun addFavoriteProduct(
       @Header("auth-token") authToken: String?,
       @Path("id") id: String
    ) : GeneralResponse

    @POST("api/favorites/delete/{id}")
    suspend fun removeFavoriteProduct(
            @Header("auth-token") authToken: String?,
            @Path("id") id: String
    ) : GeneralResponse

    @GET("api/user/")
    suspend fun getCurrentUser(
            @Header("auth-token") authToken: String?
    ) : AuthResponse
}