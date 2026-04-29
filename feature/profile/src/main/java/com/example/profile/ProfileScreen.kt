package com.example.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.designsystem.customviews.UserOrderInformationComposable
import com.example.designsystem.utils.toTitleCase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(viewModel: ProfileViewModel) {
    val favoriteProducts by viewModel.favoriteProducts.collectAsState()
    val user by viewModel.user.collectAsState()

    // 1. Grab your loading state from the ViewModel
    val isLoading by viewModel.isLoading.collectAsState()

    // 2. Wrap the entire screen content in the PullToRefreshBox
    PullToRefreshBox(
        isRefreshing = isLoading,
        onRefresh = {
            // 3. Trigger your refresh function
            viewModel.refreshData()
        }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                // 4. CRITICAL: Make the column scrollable so the pull gesture is detected!
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .padding(top = 50.dp, bottom = 30.dp)
                    .size(50.dp)
            )

            Text(
                text = "@${user?.username.toString()}",
                fontSize = 40.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = user?.name.toString(),
                fontSize = 40.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = user?.email.toString(),
                fontSize = 25.sp,
                maxLines = 1,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 30.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                UserOrderInformationComposable(
                    icon = Icons.Default.FavoriteBorder,
                    number = favoriteProducts.size,
                    text = "Favorite items"
                )

                UserOrderInformationComposable(
                    icon = Icons.Default.Archive,
                    number = 5,
                    text = "Total orders"
                )
            }

            Text(
                text = "Street: ${user?.address?.streetName?.toTitleCase()}"
            )

            Text(
                text = "City: ${user?.address?.city?.toTitleCase()}"
            )

            Text(
                text = "Zip Code: ${user?.address?.zipCode?.toTitleCase()}"
            )
        }
    }
}