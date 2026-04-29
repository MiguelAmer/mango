package com.example.myapplication

import com.example.domain.model.Product
import com.example.domain.repository.ProductRepository
import com.example.productlist.ProductViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class ProductViewModelTest {

    private lateinit var productRepository: ProductRepository

    private lateinit var viewModel: ProductViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        productRepository = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init fetches data successfully and maps favorites`() = runTest {
        val mockProducts = listOf(
            Product(id = 1, title = "Product 1", isFavorite = false, price = 9.0, image = "imageUrl1"),
            Product(
                id = 2,
                title = "Product 2",
                isFavorite = false,
                price = 9.0,
                image = "imageUrl2"
            )
        )
        val mockFavorites = listOf(
            Product(id = 1, title = "Product 1", isFavorite = false, price = 9.0, image = "imageUrl1"),
        )

        coEvery { productRepository.getProducts() } returns mockProducts
        coEvery { productRepository.getFavorites() } returns flowOf(mockFavorites)

        viewModel = ProductViewModel(productRepository)

        advanceUntilIdle()

        assertFalse(viewModel.isLoading.value)
        assertNull(viewModel.errorMessage.value)

        val uiProducts = viewModel.products.value
        assertEquals(2, uiProducts.size)
        assertTrue(uiProducts.find { it.id == 1 }?.isFavorite == true)
        assertFalse(uiProducts.find { it.id == 2 }?.isFavorite == true)
    }

    @Test
    fun `init handles IOException and sets error message`() = runTest {
        coEvery { productRepository.getProducts() } throws IOException("No internet")
        coEvery { productRepository.getFavorites() } returns flowOf(emptyList())

        viewModel = ProductViewModel(productRepository)
        advanceUntilIdle()

        assertEquals("No internet connection. Please try again.", viewModel.errorMessage.value)
        assertFalse(viewModel.isLoading.value)
    }

    @Test
    fun `toggleFavorite updates UI optimistically and calls repository`() = runTest {
        val initialProduct = Product(id = 1, title = "Product 1", isFavorite = false, price = 9.0, image = "imageUrl1")

        coEvery { productRepository.getProducts() } returns listOf(initialProduct)
        coEvery { productRepository.getFavorites() } returns flowOf(emptyList())

        viewModel = ProductViewModel(productRepository)
        advanceUntilIdle()

        viewModel.toggleFavorite(initialProduct)
        advanceUntilIdle()

        val updatedProducts = viewModel.products.value
        assertTrue(updatedProducts.first().isFavorite)

        coVerify(exactly = 1) { productRepository.toggleFavorite(initialProduct) }
    }

    @Test
    fun `clearErrorMessage resets the error state to null`() = runTest {
        coEvery { productRepository.getProducts() } throws Exception("Some error")
        viewModel = ProductViewModel(productRepository)
        advanceUntilIdle()

        assertTrue(viewModel.errorMessage.value != null)

        viewModel.clearErrorMessage()

        assertNull(viewModel.errorMessage.value)
    }
}