package com.example.myapplication.ui.profile

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.viewmodel.ProductViewModel

@Composable
fun ProfileScreen(viewModel: ProductViewModel) {
    val favoriteProducts by viewModel.favoriteProducts.collectAsState()
    val user by viewModel.user.collectAsState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
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

@Composable
fun UserOrderInformationComposable(icon: ImageVector, number: Int, text: String) {

    Column(
        modifier = Modifier
            .size(width = 160.dp, height = 120.dp)
            .border(width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(8.dp)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Icon(
            imageVector = icon,
            modifier = Modifier.size(20.dp),
            contentDescription = null
        )
        Text(
            text = number.toString(),
            fontSize = 30.sp
        )
        Text(
            text = text.uppercase(),
            fontSize = 16.sp
        )
    }
}

fun String.toTitleCase(): String {
    return this.split(" ").joinToString(" ") { word ->
        word.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase() else it.toString()
        }
    }
}