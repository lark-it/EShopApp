package com.example.eshopapp.home

import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.eshopapp.R
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.FloatingActionButtonDefaults.elevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.VerticalDivider
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import com.example.eshopapp.ui.theme.EShopAppTheme


@Composable
fun HomeScreen() {
    val fakePopularCategories = remember {
        listOf(
            CategoryUiModel(1, "Смартфоны", R.drawable.ic_phone),
            CategoryUiModel(2, "Ноутбуки", R.drawable.ic_laptop),
            CategoryUiModel(3, "Продукты", R.drawable.ic_grocery),
            CategoryUiModel(4, "Парфюм", R.drawable.ic_perfume),
            CategoryUiModel(5, "Мебель", R.drawable.ic_sofa),
        )
    }
    val fakeRecommendedProducts = remember {
        listOf(
            ProductUiModel(1, "поко х200", 200, R.drawable.ic_phone),
            ProductUiModel(2, "макбук",800, R.drawable.ic_laptop),
            ProductUiModel(3, "пакет", 1400,R.drawable.ic_grocery),
            ProductUiModel(4, "вонючка",588, R.drawable.ic_perfume),
            ProductUiModel(5, "табуретка", 2000,R.drawable.ic_sofa),
        )
    }
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item{
            SimpleSearchBar()
        }
        item{
            PopularCategory(fakePopularCategories)
            Spacer(Modifier.height(16.dp))
        }
        item{
            RecommendedList(fakeRecommendedProducts)
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
        SearchBar(
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
fun PopularCategory(categories: List<CategoryUiModel>){
    Column(
        modifier = Modifier.padding(top = 16.dp)
    ) {
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
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(
                items = categories,
                key = {it.id}
            ){ fakeCategory ->
                PopularCategoryCard(
                    category = fakeCategory
                )
            }
        }
    }
}

@Composable
fun PopularCategoryCard(category: CategoryUiModel){
    Card(
        modifier = Modifier
            .width(120.dp)
            .height(160.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
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
                    .height(120.dp)
            )
        }
    }
}

@Composable
fun RecommendedList(fakeProducts: List<ProductUiModel>){
    Column(modifier = Modifier) {
        Box(Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
        ){
            Text(
                "Рекомендуем:",
                style = MaterialTheme.typography.titleLarge
            )
        }
        val rows = fakeProducts.chunked(2)
        rows.forEach { rowItems ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ProductCard(
                    product = rowItems[0],
                    modifier = Modifier.weight(1f)
                )
                if (rowItems.size > 1) {
                    ProductCard(
                        product = rowItems[1],
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}
@Composable
fun ProductCard(
    product: ProductUiModel,
    modifier: Modifier = Modifier
){
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Image(
                painter = painterResource(product.image),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            )
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    product.price.toString() + "₽",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    product.title,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Button(onClick = {}) {
                    Text("В корзину")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview(){
    EShopAppTheme {
        HomeScreen()
    }
}