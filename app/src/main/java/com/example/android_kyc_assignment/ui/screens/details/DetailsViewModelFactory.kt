package com.example.android_kyc_assignment.ui.screens.details
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android_kyc_assignment.data.repository.IFSCRepository
import com.example.android_kyc_assignment.data.repository.UserRepository

class DetailsViewModelFactory(
    private val userRepository: UserRepository,
    private val ifscRepository: IFSCRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailsViewModel::class.java)) {
            return DetailsViewModel(
                userRepository = userRepository,
                ifscRepository = ifscRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}