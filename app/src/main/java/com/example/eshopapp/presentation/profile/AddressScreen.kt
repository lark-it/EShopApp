package com.example.eshopapp.presentation.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressScreen(
    viewModel: ProfileViewModel,
    onClosed: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = onClosed) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                title = {
                    Text("Адрес")
                }
            )
        }
    ) { innerPadding ->
        val address = viewModel.address
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 8.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = address.street,
                onValueChange = { viewModel.onStreetChanged(it) },
                placeholder = { Text("Например: Ленина, 10") },
                label = { Text("Улица и дом") },
                singleLine = true
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = address.apartment,
                    onValueChange = { viewModel.onApartmentChanged(it) },
                    label = { Text("Кв/офис") },
                    singleLine = true
                )
                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = address.entrance,
                    onValueChange = { viewModel.onEntranceChanged(it) },
                    label = { Text("Подъезд") },
                    singleLine = true
                )
                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = address.floor,
                    onValueChange = { viewModel.onFloorChanged(it) },
                    label = { Text("Этаж") },
                    singleLine = true
                )
            }
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = address.comment,
                onValueChange = { viewModel.onCommentChanged(it) },
                label = { Text("Комментарий") }
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(onClick = {
                    viewModel.saveAddress()
                    onClosed()
                }) {
                    Text("Сохранить")
                }
            }
        }
    }
}