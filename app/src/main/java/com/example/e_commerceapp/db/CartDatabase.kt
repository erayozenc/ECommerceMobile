package com.example.e_commerceapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.e_commerceapp.responses.cart.Cart
import com.example.e_commerceapp.responses.products.Product

@Database(
        entities = [Cart::class],
        version = 1
)
@TypeConverters(CategoryTypeConverter::class,ProductTypeConverter::class)
abstract class CartDatabase : RoomDatabase(){
    abstract fun getDao(): CartDao
}