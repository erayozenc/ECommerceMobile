package com.example.e_commerceapp.responses.products

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.e_commerceapp.responses.categories.Category
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Product(
        val __v: Int,
        val _id: String,
        val category: Category,
        val cloudinary_id: String,
        val createdAt: String,
        val description: String,
        val image: String,
        val name: String,
        val price: Int,
        val quantity: Int,
        val sold: Int,
        val updatedAt: String
): Serializable