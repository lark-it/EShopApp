package com.example.eshopapp.presentation.cart

import androidx.lifecycle.ViewModel
import com.example.eshopapp.data.repository.CartRepository
import com.example.eshopapp.domain.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<CartUiState>(
        CartUiState(
            items = emptyList(),
            totalCount = 0,
            totalPrice = 0.0,
            error = null
        )
    )
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    fun addToCart(product: Product){
        val currentItems = _uiState.value.items.toMutableList()

        val existingItem = currentItems.find { it.product.id == product.id }

        if (existingItem == null) {
            currentItems.add(CartItem(product, 1))
        } else {
            val index = currentItems.indexOf(existingItem)
            currentItems[index] =
                existingItem.copy(quantity = existingItem.quantity + 1)
        }

        _uiState.update { it.copy(
            items = currentItems,
            totalCount = currentItems.sumOf { it.quantity },
            totalPrice = currentItems.sumOf { it.product.price * it.quantity }
        )}
    }
}