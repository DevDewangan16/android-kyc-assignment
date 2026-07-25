package com.example.android_kyc_assignment.ui.screens.details.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.android_kyc_assignment.ui.theme.Black
import com.example.android_kyc_assignment.ui.theme.White

@Composable
fun AccountInfoSection(
    accountNumber: String,
    balance: Double,
    currency: String
) {
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Account Details",
                fontWeight = FontWeight.SemiBold,
                color = Black,
                fontSize = 15.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Account Number",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = accountNumber,
                        fontSize = 14.sp,
                        color = Black,
                        fontWeight = FontWeight.Medium
                    )
                }
                Column(horizontalAlignment = androidx.compose.ui.Alignment.End) {
                    Text(
                        text = "Balance",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "${currency} ${String.format("%.2f", balance)}",
                        fontSize = 16.sp,
                        color = Black,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}