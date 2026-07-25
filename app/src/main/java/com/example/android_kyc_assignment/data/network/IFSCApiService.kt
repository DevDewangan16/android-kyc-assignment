package com.example.android_kyc_assignment.data.network
import com.example.android_kyc_assignment.data.model.IFSCResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface IFSCApiService {
    @GET("{ifscCode}")
    suspend fun getIFSCDetails(
        @Path("ifscCode") ifscCode: String
    ): IFSCResponse
}