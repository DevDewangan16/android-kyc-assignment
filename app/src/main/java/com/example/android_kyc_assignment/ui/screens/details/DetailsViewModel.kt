package com.example.android_kyc_assignment.ui.screens.details

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_kyc_assignment.data.repository.IFSCRepository
import com.example.android_kyc_assignment.data.repository.UserRepository
import com.example.android_kyc_assignment.domain.model.Customer
import com.example.android_kyc_assignment.domain.model.CustomerDetail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class DetailsViewModel(
    private val userRepository: UserRepository,
    private val ifscRepository: IFSCRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailsUiState())
    val uiState: StateFlow<DetailsUiState> = _uiState.asStateFlow()

    private var currentCustomerId: Int = -1

    fun loadCustomer(customerId: Int) {
        currentCustomerId = customerId
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val customer = userRepository.getCustomerById(customerId)
                if (customer != null) {
                    fetchIFSCDetails(customer)
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Customer not found"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load customer"
                )
            }
        }
    }

    fun refreshCustomer() {
        if (currentCustomerId != -1) {
            loadCustomer(currentCustomerId)
        }
    }

    private suspend fun fetchIFSCDetails(customer: Customer) {
        try {
            ifscRepository.getIFSCDetails(customer.ifscCode).collect { ifscResponse ->
                val detail = createCustomerDetail(customer, ifscResponse)
                _uiState.value = _uiState.value.copy(
                    customerDetail = detail,
                    isLoading = false
                )
            }
        } catch (e: Exception) {
            // Use customer without IFSC details
            val detail = createCustomerDetail(customer, null)
            _uiState.value = _uiState.value.copy(
                customerDetail = detail,
                isLoading = false,
                error = "Failed to fetch bank details: ${e.message}"
            )
        }
    }

    private fun createCustomerDetail(customer: Customer, ifscResponse: com.example.android_kyc_assignment.data.model.IFSCResponse?): CustomerDetail {
        return CustomerDetail(
            id = customer.id,
            name = customer.name,
            photo = customer.avatar,
            dateOfBirth = "1985-06-15",
            address = "123 Main Street, Mumbai, Maharashtra 400001",
            contact = "+91 98765 43210",
            email = "${customer.name.lowercase().replace(" ", ".")}@email.com",
            accountNumber = customer.accountNumber,
            balance = customer.balance,
            currency = customer.currency,
            ifscCode = customer.ifscCode,
            bankName = ifscResponse?.bank ?: "Unknown Bank",
            branchName = ifscResponse?.branch ?: "Unknown Branch",
            city = ifscResponse?.city ?: "Unknown City",
            state = ifscResponse?.state ?: "Unknown State",
            isKycVerified = customer.isKycVerified,
            selfiePath = customer.selfiePath,
            accountType = customer.accountType  // <-- ADD THIS LINE
        )
    }

    fun updateKycStatus(bitmap: Bitmap, context: Context) {
        viewModelScope.launch {
            val currentState = _uiState.value
            val customerDetail = currentState.customerDetail ?: return@launch

            try {
                // Show loading state
                _uiState.value = _uiState.value.copy(isLoading = true)

                // Save selfie to internal storage
                val fileName = "selfie_${System.currentTimeMillis()}.jpg"
                val file = File(context.filesDir, fileName)
                FileOutputStream(file).use { outputStream ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
                }

                // Update customer with KYC verified
                val customer = Customer(
                    id = customerDetail.id,
                    name = customerDetail.name,
                    avatar = customerDetail.photo,
                    accountNumber = customerDetail.accountNumber,
                    balance = customerDetail.balance,
                    currency = customerDetail.currency,
                    ifscCode = customerDetail.ifscCode,
                    isKycVerified = true,
                    selfiePath = file.absolutePath,
                    accountType = customerDetail.accountType  // <-- ADD THIS LINE
                )

                // Update in repository (database + cache)
                userRepository.updateCustomer(customer)

                // Update UI state with verified status
                val updatedDetail = customerDetail.copy(
                    isKycVerified = true,
                    selfiePath = file.absolutePath
                )
                _uiState.value = _uiState.value.copy(
                    customerDetail = updatedDetail,
                    kycUpdated = true,
                    isLoading = false
                )

            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = _uiState.value.copy(
                    error = "Failed to save selfie: ${e.message}",
                    isLoading = false
                )
            }
        }
    }

    fun resetKycUpdated() {
        _uiState.value = _uiState.value.copy(kycUpdated = false)
    }

    data class DetailsUiState(
        val customerDetail: CustomerDetail? = null,
        val isLoading: Boolean = false,
        val error: String? = null,
        val kycUpdated: Boolean = false
    )
}