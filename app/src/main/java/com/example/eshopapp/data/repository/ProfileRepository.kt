package com.example.eshopapp.data.repository

import androidx.compose.runtime.collectAsState
import com.example.eshopapp.data.local.AddressDao
import com.example.eshopapp.data.local.AddressEntity
import com.example.eshopapp.data.mapper.toDomain
import com.example.eshopapp.data.mapper.toEntity
import com.example.eshopapp.presentation.profile.Address
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProfileRepository @Inject constructor( private val dao: AddressDao ){
    fun getAllAddresses(): Flow<List<Address>> = dao.getAllAddresses()
        .map { addresses ->
            addresses.map {
                it.toDomain()
            }
        }

    suspend fun saveAddress(address: Address) {
        dao.saveAddress(address.toEntity())
    }

    suspend fun updateAddress(address: Address) {
        dao.updateAddress(address.toEntity())
    }

    suspend fun deleteAddress(address: Address){
        dao.deleteAddress(address.toEntity())
    }
}