package com.example.eshopapp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eshopapp.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchResultsViewModel @Inject constructor(
    private val repo: ProductRepository
) : ViewModel(){
    private val _resultState = MutableStateFlow<SearchResultsUiState>(
        SearchResultsUiState(
            query = "",
            items = emptyList(),
            isLoading = false,
            error = null
        )
    )
    val resultState: StateFlow<SearchResultsUiState> = _resultState.asStateFlow()

    fun onProductSearch(text: String){
        _resultState.update{
            it.copy(
                isLoading = true
            )
        }

        viewModelScope.launch {
            runCatching {
                repo.searchProducts(
                    query = text,
                    limit = 20
                )
            }.onSuccess { products ->
                _resultState.update {
                    it.copy(
                        items = products,
                        isLoading = false
                    )
                }
            }.onFailure { e ->
                _resultState.update {
                    it.copy(
                        query = "",
                        items = emptyList(),
                        isLoading = false,
                        error = null
                    )
                }
            }
        }
    }
}