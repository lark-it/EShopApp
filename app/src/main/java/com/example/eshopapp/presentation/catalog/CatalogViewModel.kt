package com.example.eshopapp.presentation.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eshopapp.data.repository.CatalogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import javax.inject.Inject


@HiltViewModel
class CatalogViewModel @Inject constructor(
    val repo: CatalogRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<CategoryUiState>(
        CategoryUiState.Loading
    )
    val uiState: StateFlow<CategoryUiState> = _uiState.asStateFlow()

    private val _catalogState = MutableStateFlow<CatalogUiState>(
        CatalogUiState.Loading
    )
    val catalogState = _catalogState

    init {
        getCategories()
    }

    fun getCategories() {
        _uiState.value = CategoryUiState.Loading

        viewModelScope.launch {
            try {
                val categories = repo.getCategoriesWithImages()
                _uiState.value = CategoryUiState.Content(categories)

            } catch (e: Exception) {
                _uiState.value = CategoryUiState.Error(e.message ?: "беда")
            }
        }
    }

    fun getCategoryProducts(category: String){
        _catalogState.update { CatalogUiState.Loading }

        viewModelScope.launch {
            try {
                val products = repo.getCategoryProducts(category)
                _catalogState.update {
                    CatalogUiState.Content(products)
                }
            } catch (e: Exception){
                _catalogState.update { CatalogUiState.Error(e.message ?: "Ошибка") }
            }
        }
    }
}