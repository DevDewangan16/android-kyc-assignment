package com.example.android_kyc_assignment.ui.screens.accounts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.android_kyc_assignment.di.AppModule
import com.example.android_kyc_assignment.ui.screens.accounts.components.CategoryChips
import com.example.android_kyc_assignment.ui.screens.accounts.components.CustomerCard
import com.example.android_kyc_assignment.ui.screens.accounts.components.CustomTabRow
import com.example.android_kyc_assignment.ui.screens.accounts.components.SearchBar
import com.example.android_kyc_assignment.ui.theme.Black
import com.example.android_kyc_assignment.ui.theme.OffWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountsScreen(
    onCustomerClick: (Int) -> Unit
) {
    val viewModel: AccountsViewModel = viewModel(
        factory = AccountsViewModelFactory(AppModule.getUserRepository())
    )
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadCustomers()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Accounts",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Black,
                    titleContentColor = Color.White
                )
            )
        },
        containerColor = OffWhite
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .padding(top = 12.dp)
        ) {
            // Search Bar
            SearchBar(
                query = uiState.searchQuery,
                onQueryChange = viewModel::onSearchQueryChanged
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Category Chips
            CategoryChips(
                selectedCategory = uiState.selectedCategory,
                onCategorySelected = viewModel::onCategorySelected
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Tabs
            CustomTabRow(
                selectedIndex = uiState.selectedTab,
                onTabSelected = viewModel::onTabSelected
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Content
            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = Black,
                            strokeWidth = 3.dp
                        )
                    }
                }
                uiState.error != null && uiState.customers.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "😕",
                                fontSize = 48.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = uiState.error ?: "Error",
                                color = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = { viewModel.loadCustomers() },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Black,
                                    contentColor = Color.White
                                )
                            ) {
                                Text("Retry")
                            }
                        }
                    }
                }
                uiState.customers.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "📭",
                                fontSize = 48.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "No customers found",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(uiState.customers) { customer ->
                            CustomerCard(
                                customer = customer,
                                onClick = { onCustomerClick(customer.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}