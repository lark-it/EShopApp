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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.eshopapp.R
import com.example.eshopapp.presentation.home.ProductCard
import com.example.eshopapp.presentation.home.SimpleSearchBar
import com.example.eshopapp.ui.theme.EShopAppTheme


@Composable
fun CatalogScreen() {
//    val fakeRecommendedProducts = remember {
//        listOf(
//            ProductUiModel(1, "поко х200", 200, R.drawable.ic_phone),
//            ProductUiModel(2, "макбук",800, R.drawable.ic_laptop),
//            ProductUiModel(3, "пакет", 1400,R.drawable.ic_grocery),
//            ProductUiModel(4, "вонючка",588, R.drawable.ic_perfume),
//            ProductUiModel(5, "табуретка", 2000,R.drawable.ic_sofa),
//        )
//    }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        SimpleSearchBar()
        Spacer(Modifier.height(16.dp))
        FiltersHeader()
//        AllCategoryProducts(fakeRecommendedProducts)
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
//@Composable
//fun AllCategoryProducts(fakeRecommendedProducts:List<ProductUiModel>){
//    LazyVerticalGrid(
//        modifier = Modifier.fillMaxSize(),
//        columns = GridCells.Fixed(2),
//        contentPadding = PaddingValues(16.dp),
//        horizontalArrangement = Arrangement.spacedBy(12.dp),
//        verticalArrangement = Arrangement.spacedBy(12.dp)
//    ) {
//        items(
//            items = fakeRecommendedProducts,
//            key = { it.id }
//        ) { product ->
//            ProductCard(product)
//        }
//    }
//}


@Preview(showBackground = true)
@Composable
fun CatalogScreenPreview(){
    EShopAppTheme{
        CatalogScreen()
    }
}