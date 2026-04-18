package com.example.eshopapp.presentation.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.eshopapp.R
import com.example.eshopapp.domain.model.Order
import com.example.eshopapp.utils.toDisplayFormat
import com.example.eshopapp.utils.toMoneyFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(
    viewModel: ProfileViewModel,
    onBackClick: () -> Unit
) {
    val allOrders by viewModel.allOrders.collectAsState()
    var selectedOrder by remember { mutableStateOf<Order?>(null) }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Список заказов"
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding).fillMaxSize()
        ) {
            items(allOrders){ item ->
                OrdersCard(
                    order = item,
                    onDetailsClick = {
                        selectedOrder = item
                    }
                )
            }
        }
        selectedOrder?.let { order ->
            OrderBottomSheet(
                order = order,
                onDismiss = { selectedOrder = null }
            )
        }
    }
}

@Composable
fun OrdersCard(
    order: Order,
    onDetailsClick: () -> Unit
){
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = order.orderNumber
                )
                Text(
                    text = order.createdAt.toDisplayFormat()
                )
            }
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),

            ) {
                items(order.items){ item ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AsyncImage(
                            model = item.image,
                            contentDescription = null,
                            placeholder = painterResource(R.drawable.img_placeholder),
                            error = painterResource(R.drawable.img_error),
                            modifier = Modifier.size(128.dp)
                        )
                        Text(
                            text = item.price.toMoneyFormat()
                        )
                    }
                }
            }
            HorizontalDivider(thickness = 2.dp)
            Row(
                modifier = Modifier.fillMaxWidth(),

            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Товаров")
                    Text(
                        order.totalCount.toString()
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Цена")
                    Text(
                        order.totalPrice.toMoneyFormat()
                    )
                }
                Spacer(Modifier.weight(1f))
                Button(onClick = { onDetailsClick() }) {
                    Text("подробнее")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderBottomSheet(
    order: Order,
    onDismiss: () -> Unit
){
    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = "Заказ ${order.orderNumber}")
            Text(text = "Дата: ${order.createdAt.toDisplayFormat()}")
            Text(text = "Адрес: ${order.addressText}")
            Text(text = "Сумма: ${order.totalPrice.toMoneyFormat()}")
            Text(text = "Количество товаров: ${order.totalCount}")

            Spacer(modifier = Modifier.size(12.dp))

            order.items.forEach { item ->
                Text(text = "${item.title} x${item.quantity} = ${(item.price * item.quantity).toMoneyFormat()}")
            }
        }
    }
}