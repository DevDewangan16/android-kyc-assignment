package com.example.android_kyc_assignment.data.repository

import com.example.android_kyc_assignment.data.model.IFSCResponse
import com.example.android_kyc_assignment.data.network.RetrofitClient
import com.example.android_kyc_assignment.utils.CacheManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class IFSCRepository(private val cacheManager: CacheManager) {
    private val apiService = RetrofitClient.ifscApiService

    suspend fun getIFSCDetails(ifscCode: String): Flow<IFSCResponse> = flow {
        // Check cache first
        val cached = cacheManager.getCachedIFSC(ifscCode)
        if (cached != null) {
            emit(cached)
        }

        try {
            val response = apiService.getIFSCDetails(ifscCode)
            cacheManager.saveIFSC(ifscCode, response)
            emit(response)
        } catch (e: Exception) {
            // If API fails and we don't have cache, rethrow
            if (cached == null) {
                throw e
            }
        }
    }
}