package com.example.eshopapp.presentation.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eshopapp.data.repository.CatalogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Semaphore
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

    fun getCategories(){
        _uiState.value = CategoryUiState.Loading

        viewModelScope.launch {
            try {
                val categories = repo.getCategories()

                val cards = categories.map {
                    CategoryCardUi(
                        slug = it.slug,
                        name = it.name,
                        imageUrl = null
                    )
                }
                _uiState.value = CategoryUiState.Content(cards)

                loadCategoryImage(cards)
            } catch (e: Exception){
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
    fun loadCategoryImage(cards: List<CategoryCardUi>){
        viewModelScope.launch {
            cards.forEach { card ->
                val topProduct = repo.getTopProductInCategory(card.slug)
                val image = topProduct.image

                val current = _uiState.value
                if (current is CategoryUiState.Content) {
                    val updated = current.categories.map {
                        if (it.slug == card.slug) {
                            it.copy(imageUrl = image)
                        } else it
                    }
                    _uiState.value = CategoryUiState.Content(updated)
                }
            }
        }
    }
}