package com.example.eshopapp.presentation.profile

data class Address(
    val id: Int? = null,
    val street: String = "",
    val apartment: String = "",
    val entrance: String = "",
    val floor: String = "",
    val comment: String = ""
)