package com.example.eshopapp.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.eshopapp.presentation.cart.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchProductScreen(
    viewModel: SearchResultsViewModel = hiltViewModel(),
    cartVm: CartViewModel,
    query: String,
    onProductClick: (Int) -> Unit,
    onBackClick: () -> Unit
) {
    val state by viewModel.resultState.collectAsState()

    val cartState by cartVm.uiState.collectAsState()
    val quantityById = remember(cartState.items) {
        cartState.items.associate { it.productId to it.quantity }
    }

    LaunchedEffect(query) {
        viewModel.onProductSearch(query)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                windowInsets = WindowInsets(0, 0, 0, 0),
                title = {
                    Text(
                        query
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Default.Search,
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
            if (state.isLoading){
                CircularProgressIndicator()
            }
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
                        product = product,
                        quantityById = q,
                        onAddToCart = {product -> cartVm.addToCart(product) },
                        onIncrease = { id -> cartVm.increase(id) },
                        onDecrease = { id -> cartVm.decrease(id) }
                    )
                }
            }
        }
    }
}
