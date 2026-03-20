package com.example.eshopapp.presentation.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eshopapp.data.mapper.toUi
import com.example.eshopapp.data.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CategoryViewModel @Inject constructor(
    val repo: CategoryRepository
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

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val categories = repo.getCategories().map { it.toUi() }
                _uiState.value = CategoryUiState.Content(categories)

                categories.forEach { category ->
                    launch(Dispatchers.IO) {
                        val imageUrl = runCatching {
                            repo.getCategoryImage(category.slug)
                        }.getOrNull()

                        if (imageUrl != null){
                            _uiState.update { state ->
                                when (state) {
                                    is CategoryUiState.Content -> {
                                        val updatedCategories = state.categories.map { currentCategory ->
                                            if (currentCategory.slug == category.slug) {
                                                currentCategory.copy(imageUrl = imageUrl)
                                            } else {
                                                currentCategory
                                            }
                                        }
                                        CategoryUiState.Content(updatedCategories)
                                    }
                                    else -> state
                                }
                            }
                        }
                    }
                }
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