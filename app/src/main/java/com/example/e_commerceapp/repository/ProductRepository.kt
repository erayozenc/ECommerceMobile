package com.example.e_commerceapp.repository

import com.example.e_commerceapp.api.ProductApi
import com.example.e_commerceapp.db.CartDao
import com.example.e_commerceapp.responses.cart.Cart
import com.example.e_commerceapp.responses.products.upload.UploadRequestBody
import com.example.e_commerceapp.util.UserPreferences
import kotlinx.coroutines.flow.first
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.Multipart
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val api : ProductApi,
    private val dao: CartDao,
    private val preferences: UserPreferences
) : BaseRepository(){

    suspend fun saveProduct(
        authToken: String?,
        image : MultipartBody.Part,
        name : RequestBody,
        description : RequestBody,
        price: RequestBody,
        categoryId: RequestBody,
        quantity: RequestBody
    ) = safeApiCall {
        api.addProduct(authToken, image,name,description, price, categoryId, quantity)
    }

    suspend fun getProductList(
        authToken: String?,
        order : String?,
        sortBy: String?,
        limit: Int?
    ) = safeApiCall {
        api.getProductList(authToken, order, sortBy, limit)
    }

    suspend fun getSearchProductList(
        authToken: String?,
        search : String?,
        category: String?
    ) = safeApiCall {
        api.getSearchProductList(authToken, search, category)
    }

    suspend fun getProduct(authToken: String?,id: String) = safeApiCall {
        api.getProduct(authToken,id)
    }

    suspend fun getFavoriteProductList(
            authToken: String?
    ) = safeApiCall {
        api.getFavoriteProducts(authToken)
    }

    suspend fun getFavoriteProductIdList(
            authToken: String?
    ) = safeApiCall {
        api.getFavoriteProductIds(authToken)
    }

    suspend fun addFavoriteProduct(authToken: String?,id: String) = safeApiCall {
        api.addFavoriteProduct(authToken,id)
    }

    suspend fun removeFavoriteProduct(authToken: String?, id: String) = safeApiCall {
        api.removeFavoriteProduct(authToken, id)
    }

    suspend fun getCurrentUser(authToken: String?) = safeApiCall {
        api.getCurrentUser(authToken)
    }

    suspend fun addProductToCart(cart: Cart) = dao.addProductToCart(cart)
    suspend fun updateProductInCart(cart: Cart) = dao.updateProductInCart(cart)
    suspend fun deleteProductFromCart(cart: Cart) = dao.deleteProductFromCart(cart)
    fun getAllProductsInCart() = dao.getAllProducts()
    suspend fun deleteAllProductsFromCart() = dao.deleteAllProductsFromCart()

    suspend fun getAuthToken() = preferences.authToken.first()
}