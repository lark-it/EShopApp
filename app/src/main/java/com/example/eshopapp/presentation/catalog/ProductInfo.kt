package com.example.eshopapp.presentation.catalog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.eshopapp.R
import com.example.eshopapp.domain.model.Product
import com.example.eshopapp.domain.model.Review
import com.example.eshopapp.presentation.home.HomeViewModel
import com.example.eshopapp.presentation.home.ProductUiState

@Composable
fun ProductInfo(
    viewModel: HomeViewModel = hiltViewModel(),
    productId: Int,
    onBackClick: () -> Unit
) {
    LaunchedEffect(productId) {
        viewModel.loadProductInfo(productId)
    }

    val state = viewModel.productState.collectAsState().value

    when (state){
        is ProductUiState.Loading -> {
            Text("Загрузка")
        }
        is ProductUiState.Content -> {
            val product = state.product
            ProductInfoContent(
                product,
                onBackClick
            )
        }
        is ProductUiState.Error -> {
            Text(text = state.message)
            Button(
                onClick = { viewModel.loadProductInfo(productId) }){
                Text("Повторить")
            }
        }

    }

}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductInfoContent(
    product: Product,
    onBackClick:() -> Unit
){
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(product.title) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
        ){
            item {
                AsyncImage(
                    model = product.image,
                    contentDescription = null,
                    placeholder = painterResource(R.drawable.img_placeholder),
                    error = painterResource(R.drawable.img_error),
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                )
            }
            item {
                Text(
                    product.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    product.price.toString(),
                    style = MaterialTheme.typography.titleLarge
                )
            }
            item {
                Text(
                    "Описание",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    product.description,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            item{
                Text(
                    "Рейтинг",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    "⭐" + product.rating.toString(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            item{
                Text("Тэги", style = MaterialTheme.typography.titleMedium)
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    product.tags.forEach { tag ->
                        AssistChip(
                            onClick = { },
                            label = {
                                Text(tag)
                            }
                        )
                    }
                }
            }
            item{
                product.reviews.forEach { review ->
                    ReviewCard(review)
                }
            }
        }
    }
}
@Composable
fun ReviewCard(review: Review){
    Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(review.name)
            Text(review.rating.toString())
            Text(review.comment)
        }
    }

}