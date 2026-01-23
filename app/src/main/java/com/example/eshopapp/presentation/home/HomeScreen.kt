package com.example.eshopapp.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.eshopapp.domain.model.Product
import com.example.eshopapp.presentation.category.CategoryCard
import com.example.eshopapp.presentation.category.CategoryCardUi


@Composable
fun HomeScreen(
    onProductClick: (Int) -> Unit,
    onCategoryClick: (String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val productRows = state.products.chunked(2)

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item{
            SimpleSearchBar()
        }
        item{
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text(
                    "Популярные категории:",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    "Больше:",
                    style = MaterialTheme.typography.titleSmall
                )
            }
            Spacer(Modifier.height(16.dp))

            when{
                state.categoriesLoading -> {
                    CircularProgressIndicator()
                }

                state.categoriesError != null -> {
                    Text(state.categoriesError ?: "")
                    Button(onClick = { viewModel.loadCategories() }) {
                        Text("Повторить")
                    }
                }

                else -> {
                    PopularCategory(
                        state.categories,
                        onCategoryClick
                    )
                }
            }
        }
        item {
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    "Рекомендуем:",
                    style = MaterialTheme.typography.titleLarge
                )
            }
            when {
                state.productsLoading -> {
                    CircularProgressIndicator()
                }

                state.productsError != null -> {
                    Text(state.productsError ?: "")
                    Button(onClick = { viewModel.loadProducts() }) {
                        Text("Повторить")
                    }
                }

                else -> {

                }
            }
        }
        if (!state.productsLoading && state.productsError == null) {
            items(productRows) { row ->
                RecommendedRow(
                    onProductClick = onProductClick,
                    products = row
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleSearchBar(){
    var query by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        DockedSearchBar(
            modifier = Modifier.align(Alignment.TopCenter),
            inputField = {
                SearchBarDefaults.InputField(
                    query = query,
                    onQueryChange = { query = it },
                    onSearch = { },
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    placeholder = { Text("Поиск товаров...") },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    },
                    trailingIcon = {
                        Icon(Icons.Default.Clear, contentDescription = "Clear")
                    }
                )
            },
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {

        }
    }

}
@Composable
fun PopularCategory(
    categories: List<CategoryCardUi>,
    onCategoryClick: (String) -> Unit
){
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(
            items = categories
        ){ category ->
            CategoryCard(
                category = category,
                onClick = { onCategoryClick(category.slug) },
                modifier = Modifier
                    .width(120.dp)
                    .aspectRatio(0.6f)
            )
        }
    }
}

@Composable
fun RecommendedRow(
    onProductClick: (Int) -> Unit,
    products: List<Product>
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ProductCard(
            onProductClick,
            product = products[0],
            modifier = Modifier.weight(1f)
        )
        if (products.size > 1) {
            ProductCard(
                onProductClick,
                product = products[1],
                modifier = Modifier.weight(1f)
            )
        } else {
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}
