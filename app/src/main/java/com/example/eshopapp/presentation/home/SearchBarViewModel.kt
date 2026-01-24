package com.example.eshopapp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eshopapp.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.String

@HiltViewModel
class SearchBarViewModel @Inject constructor(
    private val repo: ProductRepository
) : ViewModel(){
    private val _uiState = MutableStateFlow<SearchUiState>(
        SearchUiState(
            query = "",
            expanded = false,
            suggestions = emptyList(),
            isLoading = false,
            error = null
        )
    )
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private var suggestionsJob: Job? = null

    fun onQueryChange(text: String){
        val q = text

        _uiState.value = _uiState.value.copy(
            query = q,
            expanded = q.isNotBlank(),
            error = null
        )

        if (q.isBlank()) {
            suggestionsJob?.cancel()
            _uiState.value = _uiState.value.copy(
                suggestions = emptyList(),
                isLoading = false
            )
            return
        }
        suggestionsJob?.cancel()

        suggestionsJob = viewModelScope.launch {
            delay(300)

            _uiState.value = _uiState.value.copy(isLoading = true)

            runCatching {
                repo.searchProducts(
                    query = q,
                    limit = 5
                )
            }.onSuccess { titles ->
                _uiState.value = _uiState.value.copy(
                    suggestions = titles,
                    isLoading = false
                )
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(
                    suggestions = emptyList(),
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun onExpandedChange(expanded: Boolean) {
        val q = _uiState.value.query
        _uiState.value = _uiState.value.copy(
            expanded = expanded && q.isNotBlank()
        )
    }

    fun clear() {
        _uiState.value = _uiState.value.copy(
            query = "",
            suggestions = emptyList(),
            expanded = false,
            error = null
        )
    }
}