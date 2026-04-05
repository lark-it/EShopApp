package com.example.eshopapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "address")
data class AddressEntity(
    @PrimaryKey(autoGenerate = true)
    val addressId: Int = 0,

    val street: String = "",
    val apartment: String = "",
    val entrance: String = "",
    val floor: String = "",
    val comment: String = ""
)