package com.example.eshopapp.utils

fun Long.toMoneyFormat(): String{
    val left = this / 100
    val right = this % 100
    return "$left.$right $"
}