package com.example.myapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.model.Product
import com.example.myapplication.domain.model.User
import com.example.myapplication.domain.repository.ProductRepository
import com.example.myapplication.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    private val _user = MutableStateFlow<User?>(null)

    val user: StateFlow<User?> = _user.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)

    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    val favoriteProducts: StateFlow<List<Product>> = productRepository.getFavorites()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        fetchProducts()
        getUserData()
    }

    private fun fetchProducts() {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                val products = productRepository.getProducts()
                val currentFavorites = productRepository.getFavorites()
                checkIfProductIsFavorite(currentFavorites.first(), products)
                _products.value = checkIfProductIsFavorite(currentFavorites.first(), products)
            } catch (e: CancellationException) {
                throw e
            } catch (e: IOException) {
                _errorMessage.value = "No internet connection. Please try again."

            } catch (e: Exception) {
                _errorMessage.value = "An unexpected error occurred."
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun checkIfProductIsFavorite(
        favoriteProducts: List<Product>,
        products: List<Product>
    ): List<Product> {
        val favoriteProductsIds = favoriteProducts.map { it.id }.toSet()
        return products.map {
            if (it.id in favoriteProductsIds) {
                it.copy(isFavorite = true)
            } else {
                it
            }
        }
    }

    fun toggleFavorite(product: Product) {
        viewModelScope.launch {
            _products.update { products ->
                products.map {
                    if (it.id == product.id) {
                        it.copy(isFavorite = !product.isFavorite)
                    } else {
                        it
                    }
                }
            }
            productRepository.toggleFavorite(product)
        }
    }

    private fun getUserData() {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                val user = userRepository.getUser(8)
                _user.value = user

            } catch (e: CancellationException) {
                throw e

            } catch (e: IOException) {
                _errorMessage.value = "No internet connection. Please try again."
            } catch (e: Exception) {
                _errorMessage.value = "An unexpected error occurred."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    fun refreshData() {
        fetchProducts()
        getUserData()
    }
}
