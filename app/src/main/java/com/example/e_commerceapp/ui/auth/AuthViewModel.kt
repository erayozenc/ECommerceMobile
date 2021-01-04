package com.example.e_commerceapp.ui.auth

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerceapp.repository.AuthRepository
import com.example.e_commerceapp.responses.auth.AuthResponse
import com.example.e_commerceapp.util.Resource
import kotlinx.coroutines.launch

class AuthViewModel @ViewModelInject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _registerResponse : MutableLiveData<Resource<AuthResponse>> = MutableLiveData()
    val registerResponse : LiveData<Resource<AuthResponse>>
        get() =_registerResponse

    private val _loginResponse : MutableLiveData<Resource<AuthResponse>> = MutableLiveData()
    val loginResponse : LiveData<Resource<AuthResponse>>
        get() =_loginResponse

    fun register(
        name: String,
        email: String,
        password: String
    ) = viewModelScope.launch {
        _registerResponse.value = Resource.Loading
        _registerResponse.value = repository.register(name,email,password)
    }

    fun login(
        email: String,
        password: String
    ) = viewModelScope.launch {
        _loginResponse.value = Resource.Loading
        _loginResponse.value = repository.login(email,password)
    }

    fun logout() = viewModelScope.launch {
        repository.logout()
    }

    suspend fun saveAuthToken(token: String)  {
        repository.saveAuthToken(token)
    }

    fun deleteAuthToken() = viewModelScope.launch {
        repository.deleteAuthToken()
    }
}