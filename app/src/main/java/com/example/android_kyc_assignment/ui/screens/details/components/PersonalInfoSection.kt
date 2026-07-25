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
fun PersonalInfoSection(
    dateOfBirth: String,
    nationality: String,
    address: String,
    contact: String,
    email: String
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
                text = "Personal Information",
                fontWeight = FontWeight.SemiBold,
                color = Black,
                fontSize = 15.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            InfoRow(label = "Date of Birth", value = dateOfBirth)
            InfoRow(label = "Nationality", value = nationality)
            InfoRow(label = "Address", value = address)
            InfoRow(label = "Contact", value = contact)
            InfoRow(label = "Email", value = email)
        }
    }
}