package com.example.android_kyc_assignment.utils
import android.content.Context
import android.content.SharedPreferences
import com.example.android_kyc_assignment.data.model.IFSCResponse
import com.example.android_kyc_assignment.domain.model.Customer
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Date

class CacheManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(
        Constants.SHARED_PREFS_NAME, Context.MODE_PRIVATE
    )
    private val gson = Gson()

    fun saveCustomers(customers: List<Customer>) {
        val json = gson.toJson(customers)
        prefs.edit().putString("customers", json).apply()
        prefs.edit().putLong("customers_timestamp", Date().time).apply()
    }

    fun getCachedCustomers(): List<Customer>? {
        val timestamp = prefs.getLong("customers_timestamp", 0)
        if (isExpired(timestamp)) return null

        val json = prefs.getString("customers", null)
        json?.let {
            val type = object : TypeToken<List<Customer>>() {}.type
            return gson.fromJson(it, type)
        }
        return null
    }

    fun getCustomerById(id: Int): Customer? {
        val customers = getCachedCustomers() ?: return null
        return customers.find { it.id == id }
    }

    fun updateCustomer(updatedCustomer: Customer) {
        val customers = getCachedCustomers()?.toMutableList() ?: return
        val index = customers.indexOfFirst { it.id == updatedCustomer.id }
        if (index != -1) {
            customers[index] = updatedCustomer
            saveCustomers(customers)
        } else {
            // If not found, add it
            customers.add(updatedCustomer)
            saveCustomers(customers)
        }
    }

    fun saveIFSC(ifscCode: String, details: IFSCResponse) {
        val json = gson.toJson(details)
        val key = "ifsc_$ifscCode"
        prefs.edit().putString(key, json).apply()
        prefs.edit().putLong("${key}_timestamp", Date().time).apply()
    }

    fun getCachedIFSC(ifscCode: String): IFSCResponse? {
        val key = "ifsc_$ifscCode"
        val timestamp = prefs.getLong("${key}_timestamp", 0)
        if (isExpired(timestamp)) return null

        val json = prefs.getString(key, null)
        json?.let {
            return gson.fromJson(it, IFSCResponse::class.java)
        }
        return null
    }

    private fun isExpired(timestamp: Long): Boolean {
        val now = Date().time
        val diff = now - timestamp
        return diff > (Constants.CACHE_EXPIRY_HOURS * 60 * 60 * 1000)
    }
}