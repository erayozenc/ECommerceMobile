package com.example.e_commerceapp.responses.auth

data class User(
    val email: String,
    val id: String,
    val name: String,
    val token: String?,
    val favorites : List<String>,
    val isAdmin : Boolean
)