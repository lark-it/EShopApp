package com.example.eshopapp.presentation.cart

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.eshopapp.R
import com.example.eshopapp.domain.model.Cart
import com.example.eshopapp.presentation.profile.Address
import com.example.eshopapp.presentation.profile.AddressBottomSheet
import com.example.eshopapp.utils.toMoneyFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    cartVm: CartViewModel,
    clearCart: () -> Unit,
    onGoToHome: () -> Unit,
    onOpenAddressScreen: () -> Unit,
    onDeleteAddress: (Address) -> Unit,
    onCreateAddress: () -> Unit,
    onEditAddress: (Address) -> Unit
) {
    val state by cartVm.uiState.collectAsState()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Корзина")
                },
                actions = {
                    IconButton(onClick = { clearCart() }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ){ innerPadding ->
        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    CircularProgressIndicator()
                }
            }

            state.errorMessage != null -> Text(state.errorMessage.toString())

            state.cartItems.isNotEmpty() -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    items(
                        items = state.cartItems,
                        key = { it.productId }
                    ) { cartItem ->
                        ProductInCart(
                            item = cartItem,
                            onDecrease = { id -> cartVm.decrease(id) },
                            onIncrease = { id -> cartVm.increase(id) },
                            onDeleteById = { id -> cartVm.deleteById(id) }
                        )
                        HorizontalDivider(thickness = 2.dp)
                    }
                    item {
                        AddressCard(
                            cartVm = cartVm,
                            state = state,
                            onOpenAddressScreen = onOpenAddressScreen,
                            onDeleteAddress = onDeleteAddress,
                            onCreateAddress = onCreateAddress,
                            onEditAddress = onEditAddress
                        )
                        HorizontalDivider(thickness = 2.dp)
                    }
                    item {
                        ResultCard(
                            state = state,
                            onMakeOrder = {
                                cartVm.makeOrder()
                            }
                        )
                    }
                }
            }

            else -> {
                Column(
                    modifier = Modifier.fillMaxSize().padding(innerPadding),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Кажется корзина пустая")
                    Button(onClick = onGoToHome) {
                        Text("Перейти в каталог")
                    }
                }
            }
        }
    }
}

@Composable
fun AddressCard(
    cartVm: CartViewModel,
    state: CartUiState,
    onOpenAddressScreen: () -> Unit,
    onDeleteAddress: (Address) -> Unit,
    onCreateAddress: () -> Unit,
    onEditAddress: (Address) -> Unit
) {
    var showAddressSheet by remember { mutableStateOf(false) }
    val selectedAddress = state.selectedAddress
    val text = when{
        state.allAddresses.isEmpty() -> "Добавить адрес"
        selectedAddress == null -> "Выбрать адрес"
        else -> cartVm.buildAddressText(selectedAddress)
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Icon(
                Icons.Default.Place,
                contentDescription = null
            )
            Text("Адрес доставки")
        }
        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    showAddressSheet = true
                },
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = text,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null
                )
            }
        }
        if(showAddressSheet){
            AddressBottomSheet(
                allAddresses = state.allAddresses,
                selectedAddress = selectedAddress,
                onAddressSelected = { address ->
                    cartVm.onAddressSelected(address)
                },
                onDismiss = { showAddressSheet = false },
                onOpenAddressScreen = onOpenAddressScreen,
                onDeleteAddress = onDeleteAddress,
                onCreateAddress = onCreateAddress,
                onEditAddress = onEditAddress
            )
        }
    }
}
@Composable
fun ProductInCart(
    item: Cart,
    onDecrease: (Int) -> Unit,
    onIncrease: (Int) -> Unit,
    onDeleteById: (Int) -> Unit
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)) {
            AsyncImage(
                model = item.image,
                contentDescription = null,
                placeholder = painterResource(R.drawable.img_placeholder),
                error = painterResource(R.drawable.img_error),
                modifier = Modifier.size(64.dp)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp)
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(item.title)
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = null,
                        modifier = Modifier.clickable(onClick = { onDeleteById(item.productId) })
                    )
                }
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier,
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { onDecrease(item.productId) }) {
                            Text("-")
                        }
                        Text(item.quantity.toString())
                        IconButton(onClick = { onIncrease(item.productId) }) {
                            Text("+")
                        }
                    }

                    Text((item.price * item.quantity).toMoneyFormat())
                }
            }
        }
    }
}

@Composable
fun ResultCard(
    state: CartUiState,
    onMakeOrder: () -> Unit
){
    val totalCount = state.totalCount

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Товаров")
                Text(text = "$totalCount шт")
            }
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Сумма")
                Text(state.totalPrice.toMoneyFormat())
            }
            Button(
                onClick = {
                    onMakeOrder()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = state.selectedAddress != null
            ) {
                Text("оформить заказ")
            }
        }
    }
}