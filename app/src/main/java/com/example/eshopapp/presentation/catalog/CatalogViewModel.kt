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
import javax.inject.Inject


@HiltViewModel
class CatalogViewModel @Inject constructor(
    val repo: CatalogRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<CategoryUiState>(
        CategoryUiState.Loading
    )
    val uiState: StateFlow<CategoryUiState> = _uiState.asStateFlow()

    init {
        getCategories()
    }

    fun getCategories(){
        _uiState.update { CategoryUiState.Loading }

        viewModelScope.launch {
            try {
                val categories = repo.getCategories()
                _uiState.update {
                    CategoryUiState.Content(categories)
                }
            } catch (e: Exception){
                _uiState.update {
                    CategoryUiState.Error(e.message ?: "беда")
                }
            }
        }
    }
}