package com.example.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Product
import com.example.domain.model.User
import com.example.domain.repository.ProductRepository
import com.example.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)

    val user: StateFlow<User?> = _user.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)

    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    val favoriteProducts: StateFlow<List<Product>> = productRepository.getFavorites()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        getUserData()
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
        getUserData()
    }
}
