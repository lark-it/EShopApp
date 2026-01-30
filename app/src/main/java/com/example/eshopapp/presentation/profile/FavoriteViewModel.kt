package com.example.eshopapp.presentation.profile

import androidx.lifecycle.ViewModel
import com.example.eshopapp.data.repository.FavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val repo: FavoriteRepository
): ViewModel() {

}