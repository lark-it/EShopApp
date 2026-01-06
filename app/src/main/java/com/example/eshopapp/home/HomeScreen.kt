package com.example.eshopapp.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign


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
    Column(modifier = Modifier.fillMaxSize()) {
        SimpleSearchBar()
        PopularCategory(fakePopularCategories)
        RecommendedList(fakeRecommendedProducts)
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
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text("Популярные категории:")
            Text("Больше:")
        }
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(
                items = categories,
                key = {it.id}
            ){ fakeCategory ->
                CategoryCard(
                    category = fakeCategory
                )
            }
        }
    }
}

@Composable
fun CategoryCard(category: CategoryUiModel){
    Card(
        modifier = Modifier
            .width(120.dp)
            .height(160.dp),
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
                    .height(120.dp)
            )
        }
    }
}

@Composable
fun RecommendedList(fakeProducts: List<ProductUiModel>){
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ){
        item(span = { GridItemSpan(maxLineSpan) }) {
            Text("Рекомендуем:")
        }
        items(
            items = fakeProducts,
            key = { it.id }
        ) { product ->
            ProductCard(product = product)
        }

    }
}
@Composable
fun ProductCard(product: ProductUiModel){
    Card(
        modifier = Modifier
            .width(150.dp)
            .height(250.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Image(
            painter = painterResource(product.image),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .width(150.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(product.price.toString() + "₽")
            Text(product.title)
            Button(onClick = {}) {
                Text("В корзину")
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview(){
    HomeScreen()
}