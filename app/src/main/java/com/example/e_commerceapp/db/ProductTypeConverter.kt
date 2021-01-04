package com.example.e_commerceapp.db

import androidx.room.TypeConverter
import com.example.e_commerceapp.responses.categories.Category
import com.example.e_commerceapp.responses.products.Product
import com.google.gson.Gson

class ProductTypeConverter {

    @TypeConverter
    fun fromGson(gson : String) : Product {
        return Gson().fromJson(gson, Product::class.java)
    }

    @TypeConverter
    fun toGson(product: Product) : String{
        return Gson().toJson(product)
    }
}