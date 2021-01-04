package com.example.e_commerceapp.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.e_commerceapp.responses.cart.Cart
import com.example.e_commerceapp.responses.products.Product

@Dao
interface CartDao {

    @Insert
    suspend fun addProductToCart(cart: Cart): Long

    @Update
    suspend fun updateProductInCart(cart: Cart)

    @Delete
    suspend fun deleteProductFromCart(cart: Cart)

    @Query("SELECT * FROM carts")
    fun getAllProducts(): LiveData<List<Cart>>

    @Query("DELETE FROM carts")
    suspend fun deleteAllProductsFromCart()

}