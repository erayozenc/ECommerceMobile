package com.example.e_commerceapp.ui.home.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerceapp.repository.ProductRepository
import com.example.e_commerceapp.responses.GeneralResponse
import com.example.e_commerceapp.responses.auth.AuthResponse
import com.example.e_commerceapp.responses.cart.Cart
import com.example.e_commerceapp.responses.products.Product
import com.example.e_commerceapp.responses.products.ProductListResponse
import com.example.e_commerceapp.responses.products.ProductResponse
import com.example.e_commerceapp.responses.products.upload.UploadRequestBody
import com.example.e_commerceapp.responses.products.upload.UploadResponse
import com.example.e_commerceapp.util.Resource
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.ResponseBody

class ProductViewModel @ViewModelInject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private val _productListResponse : MutableLiveData<Resource<ProductListResponse>> = MutableLiveData()
    val productListResponse : LiveData<Resource<ProductListResponse>>
        get() =_productListResponse

    private val _productSearchListResponse : MutableLiveData<Resource<ProductListResponse>> = MutableLiveData()
    val productSearchListResponse : LiveData<Resource<ProductListResponse>>
        get() =_productSearchListResponse

    private val _productResponse : MutableLiveData<Resource<ProductResponse>> = MutableLiveData()
    val productResponse : LiveData<Resource<ProductResponse>>
        get() =_productResponse

    private val _uploadResponse : MutableLiveData<Resource<UploadResponse>> = MutableLiveData()
    val uploadResponse : LiveData<Resource<UploadResponse>>
        get() =_uploadResponse

    private val _productFavoriteListResponse : MutableLiveData<Resource<ProductListResponse>> = MutableLiveData()
    val productFavoriteListResponse : LiveData<Resource<ProductListResponse>>
        get() =_productFavoriteListResponse

    private val _productFavoriteIdListResponse : MutableLiveData<Resource<List<String>>> = MutableLiveData()
    val productFavoriteIdListResponse : LiveData<Resource<List<String>>>
        get() =_productFavoriteIdListResponse

    private val _removedFavProductResponse : MutableLiveData<Resource<GeneralResponse>> = MutableLiveData()
    val removedFavProductResponse : LiveData<Resource<GeneralResponse>>
        get() =_removedFavProductResponse

    private val _currentUserResponse : MutableLiveData<Resource<AuthResponse>> = MutableLiveData()
    val currentUserResponse : LiveData<Resource<AuthResponse>>
        get() =_currentUserResponse

    fun saveProduct(
        authToken: String?,
        image: MultipartBody.Part,
        name : RequestBody,
        description : RequestBody,
        price: RequestBody,
        categoryId: RequestBody,
        quantity: RequestBody
    ) = viewModelScope.launch {
        _uploadResponse.value = repository.saveProduct(authToken, image, name, description, price, categoryId, quantity)
    }

    fun getProductList(
        authToken: String?,
        order : String?,
        sortBy : String?,
        limit: Int?,
    ) = viewModelScope.launch {
        _productListResponse.value = Resource.Loading
        _productListResponse.value = repository.getProductList(authToken, order, sortBy, limit)
    }

    fun getSearchProductList(
        authToken: String?,
        search : String?,
        category : String?
    ) = viewModelScope.launch {
        _productSearchListResponse.value = Resource.Loading
        _productSearchListResponse.value = repository.getSearchProductList(authToken, search, category)
    }

    fun getProduct(authToken: String?, id : String) = viewModelScope.launch {
        _productResponse.value = Resource.Loading
        _productResponse.value = repository.getProduct(authToken, id)
    }

    fun addFavoriteProduct(
            authToken: String?,
            id : String
    ) = viewModelScope.launch {
        repository.addFavoriteProduct(authToken, id)
    }

    fun removeFavoriteProduct(
            authToken: String?,
            id : String
    ) = viewModelScope.launch {
        _removedFavProductResponse.value = Resource.Loading
        _removedFavProductResponse.value = repository.removeFavoriteProduct(authToken, id)
    }

    fun getFavoriteProductList(
            authToken: String?
    ) = viewModelScope.launch {
        _productFavoriteListResponse.value = Resource.Loading
        _productFavoriteListResponse.value = repository.getFavoriteProductList(authToken)
    }

    fun getFavoriteProductIdList(
            authToken: String?
    ) = viewModelScope.launch {
        _productFavoriteIdListResponse.value = Resource.Loading
        _productFavoriteIdListResponse.value = repository.getFavoriteProductIdList(authToken)
    }

    fun getCurrentUser(
            authToken: String?
    ) = viewModelScope.launch {
        _currentUserResponse.value = Resource.Loading
        _currentUserResponse.value = repository.getCurrentUser(authToken)
    }

    fun addProductToCart(cart: Cart) = viewModelScope.launch {
        repository.addProductToCart(cart)
    }

    fun updateProductToCart(cart: Cart) = viewModelScope.launch {
        repository.updateProductInCart(cart)
    }

    fun deleteProductFromCart(cart: Cart) = viewModelScope.launch {
        repository.deleteProductFromCart(cart)
    }

    fun deleteAllProductsFromCart() = viewModelScope.launch {
        repository.deleteAllProductsFromCart()
    }

    fun getAllProductsInCart() = repository.getAllProductsInCart()

    suspend fun getAuthToken() = repository.getAuthToken()
}