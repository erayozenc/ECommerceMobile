package com.example.e_commerceapp.db

import androidx.room.TypeConverter
import com.example.e_commerceapp.responses.categories.Category
import com.google.gson.Gson

class CategoryTypeConverter {

    @TypeConverter
    fun fromGson(gson : String) : Category {
        return Gson().fromJson(gson, Category::class.java)
    }

    @TypeConverter
    fun toGson(category: Category) : String{
        return Gson().toJson(category)
    }
}