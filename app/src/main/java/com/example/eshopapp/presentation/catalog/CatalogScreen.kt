package com.example.eshopapp.presentation.catalog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.eshopapp.R
import com.example.eshopapp.domain.model.Product
import com.example.eshopapp.presentation.home.ProductCard
import com.example.eshopapp.presentation.home.SimpleSearchBar
import com.example.eshopapp.ui.theme.EShopAppTheme


@Composable
fun CatalogScreen(
    viewModel: CatalogViewModel = hiltViewModel(),
    category: String,
    onProductClick: (Int) -> Unit
) {
    val state by viewModel.catalogState.collectAsState()

    LaunchedEffect(category) {
        viewModel.getCategoryProducts(category)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        SimpleSearchBar()
        Spacer(Modifier.height(16.dp))
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