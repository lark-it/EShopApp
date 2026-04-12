package com.example.eshopapp.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.eshopapp.domain.model.Product
import com.example.eshopapp.presentation.cart.CartViewModel
import com.example.eshopapp.presentation.category.CategoryCard
import com.example.eshopapp.presentation.category.CategoryCardUi
import com.example.eshopapp.presentation.favorite.FavoriteViewModel

@Composable
fun HomeScreen(
    onProductClick: (Int) -> Unit,
    onCategoryClick: (String) -> Unit,
    onSearch: (String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
    cartVm: CartViewModel,
    favoriteVm: FavoriteViewModel
) {
    val state by viewModel.uiState.collectAsState()
    val productRows = state.products.chunked(2)

    val cartState by cartVm.uiState.collectAsState()
    val quantityById = remember(cartState.cartItems) {
        cartState.cartItems.associate { it.productId to it.quantity }
    }

    val favoriteIds by favoriteVm.favoriteIds.collectAsState(initial = emptySet())

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item{
            Box(modifier = Modifier.padding(horizontal = 12.dp)){
                SimpleSearchBar(
                    onSearch = onSearch
                )
            }
        }
        item{
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    "Популярные категории",
                    style = MaterialTheme.typography.titleLarge
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
                    "Рекомендуем",
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
        when {
            state.productsLoading -> {
                item { CircularProgressIndicator() }
            }
            state.productsError != null -> {
                item {
                    Text(state.productsError ?: "")
                    Button(onClick = { viewModel.loadProducts() }) {
                        Text("Повторить")
                    }
                }
            }
            else -> {
                items(productRows) { row ->
                    RecommendedRow(
                        onProductClick = onProductClick,
                        products = row,
                        quantityById  = quantityById ,
                        onAddToCart = {product -> cartVm.addToCart(product)},
                        onIncrease = { productId -> cartVm.increase(productId) },
                        onDecrease = { productId -> cartVm.decrease(productId) },
                        favoriteIds = favoriteIds,
                        onFavoriteClick = { productId -> favoriteVm.toggleFavorite(productId) }
                    )
                }
            }
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
//                    .aspectRatio(0.6f)
            )
        }
    }
}

@Composable
fun RecommendedRow(
    onProductClick: (Int) -> Unit,
    products: List<Product>,
    quantityById : Map<Int, Int>,
    onAddToCart: (Product)-> Unit,
    onIncrease: (Int)-> Unit,
    onDecrease: (Int)-> Unit,
    favoriteIds: Set<Int>,
    onFavoriteClick: (Int) -> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val q0 = quantityById[products[0].id] ?: 0

        ProductCard(
            onProductClick,
            product = products[0],
            quantityById = q0,
            onAddToCart,
            onIncrease,
            onDecrease,
            modifier = Modifier.weight(1f),
            isFavorite = products[0].id in favoriteIds,
            onFavoriteClick = onFavoriteClick
        )
        if (products.size > 1) {
            val q1 = quantityById[products[1].id] ?: 0

            ProductCard(
                onProductClick,
                product = products[1],
                quantityById = q1,
                onAddToCart,
                onIncrease,
                onDecrease,
                modifier = Modifier.weight(1f),
                isFavorite = products[1].id in favoriteIds,
                onFavoriteClick = onFavoriteClick
            )
        } else {
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}