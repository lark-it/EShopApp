package com.example.eshopapp.presentation.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eshopapp.data.local.CartItemEntity
import com.example.eshopapp.data.repository.CartRepository
import com.example.eshopapp.data.repository.OrderRepository
import com.example.eshopapp.data.repository.ProfileRepository
import com.example.eshopapp.domain.model.Cart
import com.example.eshopapp.domain.model.Order
import com.example.eshopapp.domain.model.OrderItem
import com.example.eshopapp.domain.model.Product
import com.example.eshopapp.presentation.profile.Address
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

data class CartUiState(
    val cartItems: List<Cart> = emptyList(),
    val totalCount: Int = 0,
    val totalPrice: Double = 0.0,
    val allAddresses: List<Address> = emptyList(),
    val selectedAddress: Address? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
    // Потом реализую
    //    val deliveryDate: String = "",
    //    val deliveryTimeSlot: String = "",
    //    val comment: String? = null,
)
@HiltViewModel
class CartViewModel @Inject constructor(
    private val repo: CartRepository,
    private val profileRepo: ProfileRepository,
    private val orderRepo: OrderRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<CartUiState>(
        CartUiState(
            cartItems = emptyList(),
            allAddresses = emptyList(),
            selectedAddress = null,
            isLoading = false,
            errorMessage = null
        )
    )
    val uiState = _uiState.asStateFlow()

    init {
        observeCartData()
    }
    fun observeCartData(){
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            repo.observeCartItems().collect { items  ->
                _uiState.update {
                    it.copy(
                        cartItems = items,
                        totalCount = items.sumOf { item -> item.quantity },
                        totalPrice = items.sumOf { item -> item.price * item.quantity },
                        isLoading = false,
                        errorMessage = null
                    )
                }
            }
        }

        viewModelScope.launch {
            profileRepo.getAllAddresses().collect { items ->
                _uiState.update {
                    it.copy(
                        allAddresses = items
                    )
                }
            }
        }
    }

    fun onAddressSelected(address: Address){
        _uiState.update {
            it.copy(
                selectedAddress = address
            )
        }
    }

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

    fun clearCart(){
        viewModelScope.launch {
            repo.clearCart()
        }
    }

    fun deleteById(id: Int){
        viewModelScope.launch {
            repo.deleteById(id)
        }
    }
    fun buildAddressText(address: Address): String{
        val parts = mutableListOf<String>()

        if (address.street.isNotBlank()) parts += address.street
        if (address.apartment.isNotBlank()) parts += "кв. ${address.apartment}"
        if (address.entrance.isNotBlank()) parts += "под. ${address.entrance}"
        if (address.floor.isNotBlank()) parts += "эт. ${address.floor}"

        return parts.joinToString(", ")
    }

    fun makeOrder(){
        val state = uiState.value
        val selectedAddress = state.selectedAddress ?: return
        if (state.cartItems.isEmpty()) return

        val order = Order(
            orderNumber = generateOrderNumber(),
            createdAt = System.currentTimeMillis(),
            items = state.cartItems.map { cartItem ->
                OrderItem(
                    productId = cartItem.productId,
                    title = cartItem.title,
                    price = cartItem.price,
                    image = cartItem.image,
                    quantity = cartItem.quantity
                )
            },
            totalCount = state.totalCount,
            totalPrice = state.totalPrice,
            addressText = buildAddressText(selectedAddress)
        )
        viewModelScope.launch {
            orderRepo.insertOrder(order)
            repo.clearCart()
        }
    }
    private fun generateOrderNumber(): String {
        val date = java.text.SimpleDateFormat("yyyyMMdd", java.util.Locale.getDefault())
            .format(java.util.Date())
        val random = (100000..999999).random()
        return "ORD-$date-$random"
    }
}