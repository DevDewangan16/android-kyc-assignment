package com.example.android_kyc_assignment.ui.screens.accounts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_kyc_assignment.data.repository.UserRepository
import com.example.android_kyc_assignment.domain.model.AccountType
import com.example.android_kyc_assignment.domain.model.Customer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AccountsViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AccountsUiState())
    val uiState: StateFlow<AccountsUiState> = _uiState.asStateFlow()

    private var allCustomers: List<Customer> = emptyList()

    init {
        loadCustomers()
    }

    fun loadCustomers() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                userRepository.getCustomers(forceRefresh = false).collect { customers ->
                    allCustomers = customers
                    filterCustomers()
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load customers"
                )
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        filterCustomers()
    }

    fun onTabSelected(index: Int) {
        _uiState.value = _uiState.value.copy(selectedTab = index)
        filterCustomers()
    }

    fun onCategorySelected(category: String) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
        filterCustomers()
    }

    private fun filterCustomers() {
        val query = _uiState.value.searchQuery
        val isVerified = _uiState.value.selectedTab == 0
        val category = _uiState.value.selectedCategory

        var filtered = allCustomers.filter { it.isKycVerified == isVerified }

        // Apply category filter
        when (category) {
            "Savings" -> filtered = filtered.filter { it.accountType == AccountType.SAVINGS }
            "Current" -> filtered = filtered.filter { it.accountType == AccountType.CURRENT }
            "NRI" -> filtered = filtered.filter { it.accountType == AccountType.NRI }
            "All" -> { /* Show all, no filter */ }
        }

        // Apply search query filter
        if (query.isNotBlank()) {
            filtered = filtered.filter {
                it.name.contains(query, ignoreCase = true) ||
                        it.accountNumber.contains(query, ignoreCase = true)
            }
        }

        _uiState.value = _uiState.value.copy(
            customers = filtered,
            error = null
        )
    }

    data class AccountsUiState(
        val customers: List<Customer> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null,
        val searchQuery: String = "",
        val selectedTab: Int = 0,
        val selectedCategory: String = "All"
    )
}