package com.example.eshopapp.presentation.profile

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.eshopapp.data.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repo: ProfileRepository
) : ViewModel() {
    val allAddresses: StateFlow<List<Address>> = repo.getAllAddresses()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    var address by mutableStateOf(Address())
        private set
    fun onStreetChanged(value: String){
        address = address.copy(street = value)
    }
    fun onApartmentChanged(value: String){
        address = address.copy(apartment = value)
    }
    fun onEntranceChanged(value: String){
        address = address.copy(entrance = value)
    }
    fun onFloorChanged(value: String){
        address = address.copy(floor = value)
    }
    fun onCommentChanged(value: String){
        address = address.copy(comment = value)
    }

    fun saveAddress(){
        viewModelScope.launch{
            if (address.id == null){
                repo.saveAddress(address)
                address = Address()
            } else {
                repo.updateAddress(address)
                address = Address()
            }

        }
    }
    fun deleteAddress(address: Address){
        viewModelScope.launch {
            repo.deleteAddress(address)
        }
    }
    fun startCreateAddress() {
        address = Address()
    }
    fun startEditAddress(address: Address) {
        this.address = address
    }
}