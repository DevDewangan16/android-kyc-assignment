package com.example.android_kyc_assignment.data.network
import com.example.android_kyc_assignment.data.model.UserResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("users")
    suspend fun getUsers(
        @Query("limit") limit: Int = 100,
        @Query("skip") skip: Int = 0
    ): UserResponse
}