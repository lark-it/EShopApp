package com.example.eshopapp.presentation.catalog

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.eshopapp.R
import com.example.eshopapp.domain.model.Category
import com.example.eshopapp.presentation.home.SimpleSearchBar

@Composable
fun AllCategoriesScreen(
    viewModel: CatalogViewModel = hiltViewModel(),
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
                        onClick = { onCategoryClick(category.name) },
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
