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
fun BankInfoSection(
    bankName: String,
    branchName: String,
    ifscCode: String,
    city: String,
    state: String
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
                text = "Bank Information",
                fontWeight = FontWeight.SemiBold,
                color = Black,
                fontSize = 15.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            InfoRow(label = "Bank", value = bankName)
            InfoRow(label = "Branch", value = branchName)
            InfoRow(label = "IFSC Code", value = ifscCode)
            InfoRow(label = "City", value = city)
            InfoRow(label = "State", value = state)
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            color = Color.Gray
        )
        Text(
            text = value,
            fontSize = 14.sp,
            color = Black,
            fontWeight = FontWeight.Medium
        )
        Divider(
            color = Color(0xFFF0F0F0),
            thickness = 1.dp,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}