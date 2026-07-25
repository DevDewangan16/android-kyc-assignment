package com.example.android_kyc_assignment.ui.screens.details.components
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun KYCSection(
    isKycVerified: Boolean,
    onDoKycClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isKycVerified)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.errorContainer
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
                    text = if (isKycVerified) "KYC Completed" else "KYC Pending",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = if (isKycVerified)
                        "Your account is fully verified"
                    else
                        "Complete KYC to access all features",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            if (!isKycVerified) {
                Button(
                    onClick = onDoKycClick,
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text("Do KYC")
                }
            }
        }
    }
}