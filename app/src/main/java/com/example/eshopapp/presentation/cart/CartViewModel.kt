package com.example.eshopapp.presentation.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eshopapp.data.local.CartItemEntity
import com.example.eshopapp.data.repository.CartRepository
import com.example.eshopapp.domain.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val repo: CartRepository
) : ViewModel() {
    val uiState: StateFlow<CartUiState> =
        repo.observeCartItems()
            .map { items ->
                CartUiState(
                    items = items,
                    totalCount = items.sumOf { it.quantity },
                    totalPrice = items.sumOf { it.price * it.quantity }
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = CartUiState(
                    items = emptyList(),
                    totalCount = 0,
                    totalPrice = 0.0
                )
            )

    fun addToCart(product: Product) {
        viewModelScope.launch {
            repo.addToCart(
                CartItemEntity(
                    productId = product.id,
                    title = product.title,
                    price = product.price,
                    image = product.image,
                    quantity = 1
                )
            )
        }
    }

    fun increase(productId: Int){
        viewModelScope.launch {
            repo.increment(productId)
        }
    }

    fun decrease(productId: Int){
        viewModelScope.launch {
            repo.decrement(productId)
        }
    }
}