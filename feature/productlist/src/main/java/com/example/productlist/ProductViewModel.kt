package com.example.productlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Product
import com.example.domain.model.User
import com.example.domain.repository.ProductRepository
import com.example.domain.repository.UserRepository
import com.example.domain.usecase.GetProductsWithFavoritesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    private var baseProducts: List<Product> = emptyList()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)

    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()



    init {
        fetchProducts()
    }

    private fun fetchProducts() {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                baseProducts = productRepository.getProducts()

                _products.value = baseProducts

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

        viewModelScope.launch {
            productRepository.getFavorites().collect { favorites ->
                if (baseProducts.isNotEmpty()) {
                    val favoriteIds = favorites.map { it.id }.toSet()
                    _products.value = baseProducts.map { product ->
                        product.copy(isFavorite = favoriteIds.contains(product.id))
                    }
                }
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

    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    fun refreshData() {
        fetchProducts()
    }
}
