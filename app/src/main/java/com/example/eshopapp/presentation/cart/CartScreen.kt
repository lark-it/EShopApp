package com.example.eshopapp.presentation.cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.eshopapp.R
import com.example.eshopapp.domain.model.Cart

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    cartVm: CartViewModel,
    clearCart: () -> Unit,
    onGoToHome: () -> Unit
) {
    val state by cartVm.uiState.collectAsState()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Корзина")
                },
                actions = {
                    IconButton(onClick = { clearCart() }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ){ innerPadding ->
        when {
            state.error != null -> Text(state.error.toString())

            state.items.isNotEmpty() -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    items(
                        items = state.items,
                        key = { it.productId }
                    ) { cartItem ->
                        ProductInCart(
                            item = cartItem
                        )
                    }
                    item {
                        ResultCard(state = state)
                    }
                }
            }

            else -> {
                Column(
                    modifier = Modifier.fillMaxSize().padding(innerPadding),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Кажется корзина пустая")
                    Button(onClick = onGoToHome) {
                        Text("Перейти в каталог")
                    }
                }
            }
        }
    }
}

@Composable
fun ProductInCart(
    item: Cart
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Row(modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)) {
            AsyncImage(
                model = item.image,
                contentDescription = null,
                placeholder = painterResource(R.drawable.img_placeholder),
                error = painterResource(R.drawable.img_error),
                modifier = Modifier.size(64.dp)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp)
            ) {
                Text(item.title)
                Text(item.price.toString())
                Text("${item.quantity} шт")
            }
        }
    }
}

@Composable
fun ResultCard(
    state: CartUiState
){
    val totalCount = state.totalCount
    val totalPrice = String.format("%.2f", state.totalPrice)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            Text(
                "Ваша корзина"
            )
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Товары ($totalCount)"
                )
                Text(
                    totalPrice
                )
            }

        }
    }
}