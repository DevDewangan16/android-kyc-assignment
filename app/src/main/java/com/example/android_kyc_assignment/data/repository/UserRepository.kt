package com.example.android_kyc_assignment.data.repository

import android.content.Context
import com.example.android_kyc_assignment.data.database.DatabaseHelper
import com.example.android_kyc_assignment.data.model.User
import com.example.android_kyc_assignment.data.network.RetrofitClient
import com.example.android_kyc_assignment.domain.model.AccountType
import com.example.android_kyc_assignment.domain.model.Customer
import com.example.android_kyc_assignment.utils.CacheManager
import com.example.android_kyc_assignment.utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.Random

class UserRepository(private val context: Context) {
    private val apiService = RetrofitClient.apiService
    private val databaseHelper = DatabaseHelper(context)
    private val cacheManager = CacheManager(context)

    suspend fun getCustomers(forceRefresh: Boolean = false): Flow<List<Customer>> = flow {
        // Check local database first (for KYC status)
        val dbCustomers = databaseHelper.getAllCustomers()

        if (dbCustomers.isNotEmpty() && !forceRefresh) {
            // If we have data in DB, use it (preserves KYC status)
            emit(dbCustomers)
        }

        // Fetch from API if needed (for new data)
        try {
            val response = apiService.getUsers(limit = 100)
            val apiCustomers = response.users.map { user ->
                convertToCustomer(user)
            }

            // Merge with existing data to preserve KYC status
            val mergedCustomers = if (dbCustomers.isNotEmpty()) {
                apiCustomers.map { apiCustomer ->
                    val existing = dbCustomers.find { it.id == apiCustomer.id }
                    if (existing != null && existing.isKycVerified) {
                        // Preserve KYC status, selfie path, and account type
                        existing.copy(
                            accountType = apiCustomer.accountType // Update account type from API
                        )
                    } else {
                        apiCustomer
                    }
                }
            } else {
                apiCustomers
            }

            // Save merged data to cache and database
            cacheManager.saveCustomers(mergedCustomers)
            databaseHelper.insertCustomers(mergedCustomers)

            emit(mergedCustomers)
        } catch (e: Exception) {
            // If API fails, use database data
            if (dbCustomers.isNotEmpty()) {
                emit(dbCustomers)
            } else {
                throw e
            }
        }
    }

    suspend fun searchCustomers(query: String): List<Customer> {
        return if (query.isBlank()) {
            databaseHelper.getAllCustomers()
        } else {
            databaseHelper.searchCustomers(query)
        }
    }

    suspend fun getCustomerById(id: Int): Customer? {
        // Check database first (has latest KYC status)
        val dbCustomer = databaseHelper.getCustomerById(id)
        if (dbCustomer != null) return dbCustomer

        // Then check cache
        val cached = cacheManager.getCustomerById(id)
        if (cached != null) return cached

        return null
    }

    suspend fun updateCustomer(customer: Customer) {
        // Update in database
        databaseHelper.updateCustomer(customer)
        // Update in cache
        cacheManager.updateCustomer(customer)
    }

    private fun convertToCustomer(user: User): Customer {
        val random = Random(user.id.toLong())
        val ifscCodes = Constants.IFSC_CODES
        val ifscCode = ifscCodes[random.nextInt(ifscCodes.size)]
        val balance = 10000 + (random.nextDouble() * 990000)

        // Assign account type based on user data
        val accountType = when {
            user.bank.cardType.contains("savings", ignoreCase = true) -> AccountType.SAVINGS
            user.bank.cardType.contains("current", ignoreCase = true) -> AccountType.CURRENT
            user.bank.cardType.contains("nri", ignoreCase = true) -> AccountType.NRI
            else -> {
                // Randomly assign if no match
                val types = listOf(AccountType.SAVINGS, AccountType.CURRENT, AccountType.NRI)
                types[random.nextInt(types.size)]
            }
        }

        return Customer(
            id = user.id,
            name = "${user.firstName} ${user.lastName}",
            avatar = user.image,
            accountNumber = maskAccountNumber(user.bank.iban),
            balance = balance,
            currency = user.bank.currency,
            ifscCode = ifscCode,
            isKycVerified = false,
            selfiePath = null,
            accountType = accountType
        )
    }

    private fun maskAccountNumber(iban: String): String {
        return if (iban.length > 8) {
            "****${iban.takeLast(8)}"
        } else {
            iban
        }
    }
}