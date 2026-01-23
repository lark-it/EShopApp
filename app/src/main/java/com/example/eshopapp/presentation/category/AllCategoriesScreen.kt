package com.example.eshopapp.presentation.category

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.eshopapp.presentation.home.SimpleSearchBar

@Composable
fun AllCategoriesScreen(
    viewModel: CategoryViewModel = hiltViewModel(),
    onCategoryClick: (String) -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            SimpleSearchBar()
        }
        item(span = { GridItemSpan(maxLineSpan) }) {
            Text("Все категории")
        }
        when(val s = state){
            is CategoryUiState.Loading -> {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    CircularProgressIndicator()
                }
            }
            is CategoryUiState.Content -> {
                items(s.categories) { category ->
                    CategoryCard(
                        category = category,
                        onClick = { onCategoryClick(category.slug) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            is CategoryUiState.Error -> {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Text(s.message)
                    Button(onClick = { viewModel.getCategories() }){
                        Text("Повторить")
                    }
                }
            }
        }
    }
}
