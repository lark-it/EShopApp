package com.example.eshopapp.catalog

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
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.eshopapp.R
import com.example.eshopapp.home.CategoryUiModel
import com.example.eshopapp.home.SimpleSearchBar

@Composable
fun AllCategoriesScreen() {
    val fakePopularCategories = remember {
        listOf(
            CategoryUiModel(1, "Смартфоны", R.drawable.ic_phone),
            CategoryUiModel(2, "Ноутбуки", R.drawable.ic_laptop),
            CategoryUiModel(3, "Продукты", R.drawable.ic_grocery),
            CategoryUiModel(4, "Парфюм", R.drawable.ic_perfume),
            CategoryUiModel(5, "Мебель", R.drawable.ic_sofa),
        )
    }
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
        items(
            items = fakePopularCategories,
            key = { it.id }
        ) { category ->
            CategoryCard(category)
        }
    }
}
@Composable
fun CategoryCard(category: CategoryUiModel){
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier
                .height(40.dp)
                .fillMaxWidth()
                .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = category.title,
                    maxLines = 2,
                    textAlign = TextAlign.Center
                )
            }
            Image(
                painter = painterResource(id = category.image),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}
@Preview
@Composable
fun AllCategoriesScreenPreview(){
    AllCategoriesScreen()
}