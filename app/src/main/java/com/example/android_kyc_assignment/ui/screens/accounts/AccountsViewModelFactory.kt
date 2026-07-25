package com.example.android_kyc_assignment.ui.screens.accounts
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android_kyc_assignment.data.repository.UserRepository

class AccountsViewModelFactory(
    private val userRepository: UserRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AccountsViewModel::class.java)) {
            return AccountsViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}