package com.example.e_commerceapp.ui.home.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerceapp.repository.CategoryRepository
import com.example.e_commerceapp.responses.categories.CategoryListResponse
import com.example.e_commerceapp.responses.categories.CategoryResponse
import com.example.e_commerceapp.responses.products.ProductListResponse
import com.example.e_commerceapp.responses.products.ProductResponse
import com.example.e_commerceapp.util.Resource
import kotlinx.coroutines.launch

class CategoryViewModel @ViewModelInject constructor(
        private val repository: CategoryRepository
) : ViewModel() {

    private val _categoryListResponse : MutableLiveData<Resource<CategoryListResponse>> = MutableLiveData()
    val categoryListResponse : LiveData<Resource<CategoryListResponse>>
        get() =_categoryListResponse

    private val _categoryResponse : MutableLiveData<Resource<CategoryResponse>> = MutableLiveData()
    val categoryResponse : LiveData<Resource<CategoryResponse>>
        get() =_categoryResponse

    fun saveCategory(
            name : String
    ) = viewModelScope.launch {
        repository.saveCategory(name)
    }

    fun getCategoryList(
            authToken: String?
    ) = viewModelScope.launch {
        _categoryListResponse.value = Resource.Loading
        _categoryListResponse.value = repository.getCategoryList(authToken)
    }


    fun getCategory(authToken: String?, id : String) = viewModelScope.launch {
        _categoryResponse.value = Resource.Loading
        _categoryResponse.value = repository.getCategory(authToken, id)
    }

    suspend fun getAuthToken() = repository.getAuthToken()
}