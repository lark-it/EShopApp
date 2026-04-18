package com.example.eshopapp.presentation.profile

import android.R
import android.graphics.drawable.Icon
import android.view.MenuItem
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.eshopapp.domain.model.Order

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onOpenAddressScreen: () -> Unit,
    onOpenOrdersScreen: () -> Unit,
    viewModel: ProfileViewModel
) {
    var showAddressSheet by remember { mutableStateOf(false) }
    val allAddresses by viewModel.allAddresses.collectAsState()
    var selectedAddress by remember { mutableStateOf<Address?>(null) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Профиль")
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 12.dp)
        ) {
            UserCard()
            MenuItem(
                icon = Icons.Default.ShoppingCart,
                title = "Мои заказы",
                onClick = { onOpenOrdersScreen() }
            )

            MenuItem(
                icon = Icons.Default.LocationOn,
                title = "Мои адреса",
                onClick = { showAddressSheet = true }
            )

            MenuItem(
                icon = Icons.Default.Settings,
                title = "Настройки",
                onClick = {  }
            )

            MenuItem(
                icon = Icons.AutoMirrored.Filled.ExitToApp,
                title = "Выход",
                onClick = {  }
            )
        }
    }
    if (showAddressSheet){
      AddressBottomSheet(
          allAddresses = allAddresses,
          selectedAddress = selectedAddress,
          onAddressSelected = { address ->
              selectedAddress = address
          },
          onDismiss = { showAddressSheet = false },
          onOpenAddressScreen = onOpenAddressScreen,
          onDeleteAddress = { address ->
              viewModel.deleteAddress(address)
          },
          onCreateAddress = {
              viewModel.startCreateAddress()
          },
          onEditAddress = { address ->
              viewModel.startEditAddress(address)
          }
      )
    }
}

@Composable
fun UserCard(){
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 2.dp,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(com.example.eshopapp.R.drawable.img_profile),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(88.dp)
                    .clip(CircleShape)
            )
            Column() {
                Text("Иван Иванович")
                Text("@ivan")
            }
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = null
            )
        }
    }
}

@Composable
fun MenuItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit,
//    modifier: Modifier = Modifier
){
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 2.dp,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(36.dp)
            )
            Text(
                modifier = Modifier.padding(start = 12.dp),
                text = title
            )
            Spacer(Modifier.weight(1f))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressBottomSheet(
    allAddresses: List<Address>,
    selectedAddress: Address?,
    onAddressSelected: (Address) -> Unit,
    onDismiss: () -> Unit,
    onOpenAddressScreen: () -> Unit,
    onDeleteAddress: (Address) -> Unit,
    onCreateAddress: () -> Unit,
    onEditAddress: (Address) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
        confirmValueChange = {
            if (it == SheetValue.Hidden) {
                onDismiss()
            }
            true
        }
    )
    var openedMenuAddressId by remember { mutableStateOf<Int?>(null) }
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ){
                Text("Выберите адрес доставки")
            }

            allAddresses.forEach { address ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedAddress == address,
                        onClick = {
                            onAddressSelected(address)
                            onDismiss()
                        }
                    )
                    Text(
                        text = address.street + address.apartment,
                        modifier = Modifier.weight(1f)
                    )

                    Box {
                        IconButton(
                            onClick = { openedMenuAddressId = address.id }
                        ) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "more"
                            )
                        }
                        DropdownMenu(
                            expanded = openedMenuAddressId == address.id,
                            onDismissRequest = { openedMenuAddressId = null }
                        ) {
                            DropdownMenuItem(
                                text = { Text("редактировать")},
                                onClick = {
                                    onEditAddress(address)
                                    onOpenAddressScreen()
                                    openedMenuAddressId = null
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("удалить")},
                                onClick = {
                                    onDeleteAddress(address)
                                    openedMenuAddressId = null
                                }
                            )
                        }
                    }

                }
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    onCreateAddress()
                    onOpenAddressScreen()
                }
            ) {
                Text("Добавить адрес доставки")
            }
        }
    }
}