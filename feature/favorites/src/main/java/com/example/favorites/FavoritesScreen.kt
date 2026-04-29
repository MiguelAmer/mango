package com.example.favorites

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.designsystem.customviews.ProductItem

@Composable
fun FavoritesScreen(viewModel: FavoritesViewModel) {
    val favoriteProducts by viewModel.favoriteProducts.collectAsState()

    Column(Modifier.fillMaxSize()) {
        Text(
            text = "Saved items",
            fontSize = 30.sp,
            modifier = Modifier.padding(8.dp)
        )

        Text(
            text = getTextForSubtitle(favoriteProducts.size),
            fontSize = 16.sp,
            modifier = Modifier.padding(8.dp)
        )

        LazyColumn {
            items(favoriteProducts) { product ->
                ProductItem(
                    product = product,
                    onFavoriteClick = { viewModel.toggleFavorite(product) })
            }
        }

    }

}

private fun getTextForSubtitle(numberOfItems: Int): String {
    return when (numberOfItems) {
        0 -> "No favorites yet."
        1 -> "You have $numberOfItems product in your wishlist."
        else -> "You have $numberOfItems products in your wishlist."
    }
}