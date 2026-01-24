package com.example.eshopapp.presentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleSearchBar(
    vm: SearchBarViewModel = hiltViewModel(),
    onSearch: (String) -> Unit
){
    val state by vm.uiState.collectAsState()

    DockedSearchBar(
        modifier = Modifier.fillMaxWidth(),
        inputField = {
            SearchBarDefaults.InputField(
                query = state.query,
                onQueryChange = { text ->
                    vm.onQueryChange(text)
                },
                onSearch = { onSearch(state.query) },
                expanded = state.expanded && state.suggestions.isNotEmpty(),
                onExpandedChange = {
                    vm.onExpandedChange(it)
                },
                placeholder = { Text("Поиск товаров...") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                },
                trailingIcon = {
                    Icon(Icons.Default.Clear, contentDescription = "Clear")
                }
            )
        },
        expanded = state.expanded && state.suggestions.isNotEmpty(),
        onExpandedChange = { vm.onExpandedChange(it) }
    ) {
        state.suggestions.forEach { s ->
            Text(
                text = s.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSearch(s.title) }
                    .padding(12.dp)
            )
        }
    }
}