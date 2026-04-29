package com.example.myapplication.ui.product_list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.myapplication.ui.product.ProductItem
import com.example.myapplication.ui.viewmodel.ProductViewModel

@Composable
fun ProductListScreen(viewModel: ProductViewModel) {
    val products by viewModel.products.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    PullToRefreshBox(
        isRefreshing = isLoading,
        onRefresh = {
            viewModel.refreshData()
        }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "Our amazing products!",
                fontSize = 30.sp,
                modifier = Modifier.padding(8.dp)
            )
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .weight(1f)
                )
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(products) { product ->
                        ProductItem(
                            product = product,
                            onFavoriteClick = { viewModel.toggleFavorite(product) })
                    }
                }
            }
        }
    }
}