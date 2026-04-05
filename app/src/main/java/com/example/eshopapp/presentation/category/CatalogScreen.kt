package com.example.eshopapp.presentation.category

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.eshopapp.domain.model.Product
import com.example.eshopapp.presentation.cart.CartViewModel
import com.example.eshopapp.presentation.favorite.FavoriteViewModel
import com.example.eshopapp.presentation.home.ProductCard


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    viewModel: CategoryViewModel = hiltViewModel(),
    cartVm: CartViewModel,
    category: String,
    onProductClick: (Int) -> Unit,
    onBackClick: () -> Unit,
    favoriteVm: FavoriteViewModel
) {
    val state by viewModel.catalogState.collectAsState()

    val cartState by cartVm.uiState.collectAsState()
    val quantityById = remember(cartState.items) {
        cartState.items.associate { it.productId to it.quantity }
    }

    val favoriteIds = favoriteVm.favoriteIds.collectAsState().value

    LaunchedEffect(category) {
        viewModel.getCategoryProducts(category)
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        category
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ){ innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val s = state) {
                is CatalogUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ){
                        CircularProgressIndicator()
                    }

                }
                is CatalogUiState.Content -> {
                    AllCategoryProducts(
                        products = s.products,
                        onProductClick,
                        quantityById = quantityById,
                        onAddToCart = {product -> cartVm.addToCart(product) },
                        onIncrease = {id -> cartVm.increase(id) },
                        onDecrease = {id -> cartVm.decrease(id) },
                        favoriteIds = favoriteIds,
                        onFavoriteClick = { productId -> favoriteVm.toggleFavorite(productId)}
                    )
                }
                is CatalogUiState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ){
                        Text("Ошибка: ${s.message}")
                        Button(onClick = { viewModel.getCategoryProducts(category) }) {
                            Text("Повторить")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AllCategoryProducts(
    products: List<Product>,
    onProductClick:(Int) -> Unit,
    quantityById: Map<Int, Int>,
    onAddToCart: (Product) -> Unit,
    onIncrease: (Int) -> Unit,
    onDecrease: (Int) -> Unit,
    favoriteIds: Set<Int>,
    onFavoriteClick: (Int) -> Unit
){
    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = products,
            key = { it.id }
        ) { product ->
            val q = quantityById[product.id] ?: 0
            ProductCard(
                onProductClick = { onProductClick(product.id) },
                product,
                quantityById = q,
                onAddToCart = { onAddToCart(product) },
                onIncrease,
                onDecrease,
                isFavorite = product.id in favoriteIds,
                onFavoriteClick = onFavoriteClick
            )
        }
    }
}