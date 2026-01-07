package com.example.eshopapp.catalog

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.eshopapp.R
import com.example.eshopapp.home.CategoryUiModel
import com.example.eshopapp.home.ProductUiModel
import com.example.eshopapp.ui.theme.EShopAppTheme

@Composable
fun ProductInfo() {
    val product = ProductUiModel(1, "поко х200", 200, R.drawable.ic_phone)

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Image(
                painter = painterResource(product.image),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth()
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
    }
}

@Preview(showBackground = true)
@Composable
fun ProductInfoPreview() {
    EShopAppTheme{
        ProductInfo()
    }
}