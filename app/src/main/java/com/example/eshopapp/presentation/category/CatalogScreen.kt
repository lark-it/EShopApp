package com.example.eshopapp.presentation.category

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.eshopapp.domain.model.Product
import com.example.eshopapp.presentation.home.ProductCard


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    viewModel: CategoryViewModel = hiltViewModel(),
    category: String,
    onProductClick: (Int) -> Unit,
    onBackClick: () -> Unit
) {
    val state by viewModel.catalogState.collectAsState()

    LaunchedEffect(category) {
        viewModel.getCategoryProducts(category)
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                //костыль
                windowInsets = WindowInsets(0, 0, 0, 0),
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
            FiltersHeader()
            when (val s = state) {
                is CatalogUiState.Loading -> {
                    CircularProgressIndicator()
                }
                is CatalogUiState.Content -> {
                    AllCategoryProducts(
                        products = s.products,
                        onProductClick
                    )
                }
                is CatalogUiState.Error -> {
                    Text("Ошибка: ${s.message}")
                    Button(onClick = { viewModel.getCategoryProducts(category) }) {
                        Text("Повторить")
                    }
                }
            }
        }
    }
}
@Composable
fun FiltersHeader(){
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Text("Фильтр")
        Text("Сортировка")
    }
}
@Composable
fun AllCategoryProducts(
    products: List<Product>,
    onProductClick:(Int)-> Unit
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
            ProductCard(
                onProductClick = { onProductClick(product.id) },
                product
            )
        }
    }
}