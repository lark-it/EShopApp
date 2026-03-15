package com.example.eshopapp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eshopapp.data.mapper.toUi
import com.example.eshopapp.data.repository.CategoryRepository
import com.example.eshopapp.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: ProductRepository,
    private val catalogRepo: CategoryRepository

) : ViewModel() {
    private val _uiState = MutableStateFlow<HomeUiState>(
        HomeUiState(
            emptyList(),
            productsLoading = false,
            productsError = null,
            emptyList(),
            categoriesLoading = false,
            categoriesError = null
        )
    )
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _productState = MutableStateFlow<ProductUiState>(ProductUiState.Loading)
    val productState: StateFlow<ProductUiState> = _productState.asStateFlow()

    init {
        loadProducts()
        loadCategories()
    }

    fun loadProducts() {
        _uiState.update { it.copy(
            productsLoading = true
        ) }

        viewModelScope.launch {
            try {
                val products = repo.getProducts()
                _uiState.update { it.copy(
                    products = products,
                    productsLoading = false
                ) }
            } catch (e: Exception){
                _uiState.update { it.copy(
                    productsLoading = false,
                    productsError = e.message ?: "Не удалось загрузить товары"
                ) }
            }
        }
    }

    fun loadCategories() {
        _uiState.update { it.copy(
            categoriesLoading = true
        ) }

        viewModelScope.launch {
            try {
                val categories = catalogRepo.getCategoriesWithImages(6).map { it.toUi() }
                _uiState.update { it.copy(
                    categories = categories,
                    categoriesLoading = false
                ) }
            } catch (e: Exception){
                _uiState.update { it.copy(
                    categoriesLoading = false,
                    categoriesError = e.message ?: "Не удалось загрузить категории"
                ) }
            }
        }
    }

    fun loadProductInfo(id: Int){
        viewModelScope.launch {
            _productState.value = ProductUiState.Loading
            try {
                val product = repo.getProductInfo(id)
                _productState.value = ProductUiState.Content(product)
            } catch (e: Exception){
                _productState.value = ProductUiState.Error(
                    message = e.message ?: "не удалось загрузить товар"
                )
            }
        }
    }
}