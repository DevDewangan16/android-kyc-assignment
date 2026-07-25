package com.example.android_kyc_assignment.domain.model
enum class AccountType {
    SAVINGS,
    CURRENT,
    NRI,
    UNKNOWN
}

data class Customer(
    val id: Int,
    val name: String,
    val avatar: String,
    val accountNumber: String,
    val balance: Double,
    val currency: String,
    val ifscCode: String,
    val isKycVerified: Boolean = false,
    var selfiePath: String? = null,
    val accountType: AccountType = AccountType.UNKNOWN
)

data class CustomerDetail(
    val id: Int,
    val name: String,
    val photo: String,
    val dateOfBirth: String,
    val nationality: String = "Indian",
    val address: String,
    val contact: String,
    val email: String,
    val accountNumber: String,
    val balance: Double,
    val currency: String,
    val ifscCode: String,
    val bankName: String? = null,
    val branchName: String? = null,
    val city: String? = null,
    val state: String? = null,
    val isKycVerified: Boolean = false,
    var selfiePath: String? = null,
    val accountType: AccountType = AccountType.UNKNOWN
)