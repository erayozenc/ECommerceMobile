package com.example.e_commerceapp.responses.cart

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.e_commerceapp.responses.products.Product

@Entity(tableName = "carts")
data class Cart(
        @PrimaryKey(autoGenerate = true)
        val uuid : Int,
    val product: Product,
    var quantity: Int = 1
)