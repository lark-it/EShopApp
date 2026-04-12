package com.example.eshopapp.presentation.favorite

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.eshopapp.domain.model.Product
import com.example.eshopapp.presentation.cart.CartViewModel
import com.example.eshopapp.presentation.category.CatalogUiState
import com.example.eshopapp.presentation.home.ProductCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreen(
    vm: FavoriteViewModel = hiltViewModel(),
    cartVm: CartViewModel,
    onProductClick: (Int) -> Unit,
) {
    val state by vm.uiState.collectAsState()

    val cartState by cartVm.uiState.collectAsState()
    val quantityById = remember(cartState.cartItems) {
        cartState.cartItems.associate { it.productId to it.quantity }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Избранное")
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            when {
                state.loading -> CircularProgressIndicator()

                state.error != null -> {
                    Text(state.error!!)
                    Button(onClick = { vm.retryLoad()}) {
                        Text("Повторить")
                    }
                }
                state.items.isNotEmpty() -> {
                    LazyVerticalGrid(
                        modifier = Modifier.fillMaxSize(),
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = state.items,
                            key = { it.id }
                        ) { product ->
                            val q = quantityById[product.id] ?: 0
                            ProductCard(
                                onProductClick = { onProductClick(product.id) },
                                product,
                                quantityById = q,
                                onAddToCart = { product -> cartVm.addToCart(product) },
                                onIncrease = { id -> cartVm.increase(id) },
                                onDecrease = { id -> cartVm.decrease(id) },
                                isFavorite = product.id in vm.favoriteIds.collectAsState().value,
                                onFavoriteClick = { productId -> vm.toggleFavorite(productId) }
                            )
                        }
                    }
                }

                else -> Text("Товаров в избранном пока что нет")
            }
        }
    }
}