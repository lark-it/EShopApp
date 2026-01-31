package com.example.eshopapp.presentation.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eshopapp.data.repository.FavoriteRepository
import com.example.eshopapp.data.repository.ProductRepository
import com.example.eshopapp.domain.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val repo: FavoriteRepository,
    private val productRepo: ProductRepository,
): ViewModel() {
    val favoriteIds: StateFlow<Set<Int>> =
        repo.favoriteProducts()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptySet()
            )

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<FavoriteUiState> =
        favoriteIds.flatMapLatest { ids ->
            flow {
                emit(FavoriteUiState(loading = true))

                val products = coroutineScope {
                    ids.map { id ->
                        async { productRepo.getProductInfo(id) }
                    }.awaitAll()
                }

                emit(FavoriteUiState(items = products, loading = false))
            }.catch { e ->
                emit(FavoriteUiState(error = e.message, loading = false))
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            FavoriteUiState()
        )

    fun toggleFavorite(productId: Int) {
        viewModelScope.launch {
            val isFav = favoriteIds.value.contains(productId)
            if (isFav) repo.deleteFromFavorite(productId)
            else repo.addToFavorite(productId)
        }
    }
}