package com.example.eshopapp.presentation.category

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.eshopapp.R
import com.example.eshopapp.domain.model.Product
import com.example.eshopapp.domain.model.Review
import com.example.eshopapp.presentation.favorite.FavoriteViewModel
import com.example.eshopapp.presentation.home.HomeViewModel
import com.example.eshopapp.presentation.home.ProductUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductInfo(
    viewModel: HomeViewModel = hiltViewModel(),
    favoriteVm: FavoriteViewModel,
    productId: Int,
    onBackClick: () -> Unit
) {
    LaunchedEffect(productId) {
        viewModel.loadProductInfo(productId)
    }

    val state = viewModel.productState.collectAsState().value

    val favoriteList = favoriteVm.favoriteIds.collectAsState()
    val isFavorite = favoriteList.value.contains(productId)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {

                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                },
                actions = {
                    IconToggleButton(
                        checked = isFavorite,
                        onCheckedChange = {
                            favoriteVm.toggleFavorite(productId)
                        }
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Favorite"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        when (state){
            is ProductUiState.Loading -> {
                Box(
                    Modifier.fillMaxSize().padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is ProductUiState.Error -> {
                Column(
                    Modifier.fillMaxSize().padding(innerPadding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(state.message)
                    Button(onClick = { viewModel.loadProductInfo(productId) }) {
                        Text("Повторить")
                    }
                }
            }

            is ProductUiState.Content -> {
                val product = state.product
                LazyColumn(modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 12.dp)
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
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            product.price.toString(),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Black
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
                        Text(
                            "Рейтинг",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Row() {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color.Yellow
                            )
                            Text(
                                product.rating.toString(),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }
                    item{
                        Text(
                            "Отзывы",
                            style = MaterialTheme.typography.titleMedium
                        )
                        product.reviews.forEach { review ->
                            ReviewCard(review)
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun ReviewCard(review: Review){
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                review.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
            Row {
                for (i in 1..5){
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = if (i<= review.rating) Color.Yellow else Color(0xFFB0BEC5)
                    )
                }
            }
            Text(
                review.comment,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}