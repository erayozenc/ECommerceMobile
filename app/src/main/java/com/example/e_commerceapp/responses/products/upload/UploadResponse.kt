package com.example.e_commerceapp.responses.products.upload

import com.example.e_commerceapp.responses.products.Product

data class UploadResponse(
    val error: String?,
    val product: Product?
)