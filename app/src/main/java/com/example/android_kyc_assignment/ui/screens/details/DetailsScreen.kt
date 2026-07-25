package com.example.android_kyc_assignment.ui.screens.details

import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.android_kyc_assignment.domain.model.AccountType
import com.example.android_kyc_assignment.ui.screens.details.components.*
import com.example.android_kyc_assignment.ui.theme.Black
import com.example.android_kyc_assignment.ui.theme.OffWhite
import com.example.android_kyc_assignment.ui.theme.White
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    customerId: Int,
    onNavigateToCamera: (Int) -> Unit,
    onKycComplete: () -> Unit,
    viewModel: DetailsViewModel
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    val cameraPermissionState = rememberPermissionState(
        Manifest.permission.CAMERA
    )

    var showPermissionDialog by remember { mutableStateOf(false) }
    var showPermissionDeniedDialog by remember { mutableStateOf(false) }

    LaunchedEffect(customerId) {
        viewModel.loadCustomer(customerId)
    }

    LaunchedEffect(uiState.kycUpdated) {
        if (uiState.kycUpdated) {
            kotlinx.coroutines.delay(300)
            onKycComplete()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Account Details",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onKycComplete) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Black,
                    titleContentColor = Color.White
                )
            )
        },
        containerColor = OffWhite
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp, bottom = 16.dp)
        ) {
            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = Black,
                            strokeWidth = 3.dp
                        )
                    }
                }
                uiState.error != null && uiState.customerDetail == null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "😕",
                                fontSize = 48.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = uiState.error ?: "Error",
                                color = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = { viewModel.loadCustomer(customerId) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Black,
                                    contentColor = Color.White
                                )
                            ) {
                                Text("Retry")
                            }
                        }
                    }
                }
                uiState.customerDetail != null -> {
                    val customer = uiState.customerDetail!!

                    val photoPath = if (customer.isKycVerified && customer.selfiePath != null) {
                        customer.selfiePath!!
                    } else {
                        customer.photo
                    }

                    // Profile Header
                    ProfileHeader(
                        name = customer.name,
                        photo = photoPath,
                        isKycVerified = customer.isKycVerified
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Account Type Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = White
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 1.dp
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "🏦 ${customer.accountType.name} Account",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = Black
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // KYC Section
                    if (!customer.isKycVerified) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFFFF3E0)
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "⏳ KYC Pending",
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFFE65100)
                                    )
                                    Text(
                                        text = "Complete KYC to verify your identity",
                                        fontSize = 12.sp,
                                        color = Color(0xFFBF360C)
                                    )
                                }
                                Button(
                                    onClick = {
                                        when {
                                            cameraPermissionState.status.isGranted -> {
                                                onNavigateToCamera(customerId)
                                            }
                                            cameraPermissionState.status.shouldShowRationale -> {
                                                showPermissionDialog = true
                                            }
                                            else -> {
                                                showPermissionDeniedDialog = true
                                            }
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFFD32F2F),
                                        contentColor = Color.White
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier
                                        .width(100.dp)
                                        .height(36.dp),
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    Text(
                                        text = "Do KYC",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    } else {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFE8F5E9)
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "✅ KYC Verified",
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF2E7D32)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Personal Info
                    PersonalInfoSection(
                        dateOfBirth = customer.dateOfBirth,
                        nationality = customer.nationality,
                        address = customer.address,
                        contact = customer.contact,
                        email = customer.email
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Account Info
                    AccountInfoSection(
                        accountNumber = customer.accountNumber,
                        balance = customer.balance,
                        currency = customer.currency
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Bank Info
                    BankInfoSection(
                        bankName = customer.bankName ?: "Loading...",
                        branchName = customer.branchName ?: "Loading...",
                        ifscCode = customer.ifscCode,
                        city = customer.city ?: "Loading...",
                        state = customer.state ?: "Loading..."
                    )
                }
            }
        }
    }

    // Permission Dialog
    if (showPermissionDialog) {
        AlertDialog(
            onDismissRequest = { showPermissionDialog = false },
            title = {
                Text(
                    "Camera Permission",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text("Camera permission is needed to capture your selfie for KYC verification.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showPermissionDialog = false
                        cameraPermissionState.launchPermissionRequest()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Black,
                        contentColor = Color.White
                    )
                ) {
                    Text("Grant")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showPermissionDialog = false }
                ) {
                    Text("Cancel", color = Color.Gray)
                }
            }
        )
    }

    if (showPermissionDeniedDialog) {
        AlertDialog(
            onDismissRequest = { showPermissionDeniedDialog = false },
            title = {
                Text(
                    "Permission Denied",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text("Camera permission is permanently denied. Please enable it from settings.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showPermissionDeniedDialog = false
                        val intent = android.content.Intent(
                            android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            android.net.Uri.parse("package:${context.packageName}")
                        )
                        context.startActivity(intent)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Black,
                        contentColor = Color.White
                    )
                ) {
                    Text("Open Settings")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showPermissionDeniedDialog = false }
                ) {
                    Text("Cancel", color = Color.Gray)
                }
            }
        )
    }
}