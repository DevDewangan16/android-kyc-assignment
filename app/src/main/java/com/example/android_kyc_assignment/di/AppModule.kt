package com.example.android_kyc_assignment.di

import android.content.Context
import com.example.android_kyc_assignment.data.repository.IFSCRepository
import com.example.android_kyc_assignment.data.repository.UserRepository
import com.example.android_kyc_assignment.utils.CacheManager

object AppModule {
    private lateinit var userRepository: UserRepository
    private lateinit var ifscRepository: IFSCRepository
    private lateinit var cacheManager: CacheManager

    fun initialize(context: Context) {
        cacheManager = CacheManager(context)
        userRepository = UserRepository(context)
        ifscRepository = IFSCRepository(cacheManager)
    }

    fun getUserRepository(): UserRepository = userRepository
    fun getIFSCRepository(): IFSCRepository = ifscRepository
}