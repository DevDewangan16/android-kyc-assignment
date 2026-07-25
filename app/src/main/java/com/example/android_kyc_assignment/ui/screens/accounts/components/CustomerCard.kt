package com.example.android_kyc_assignment.ui.screens.accounts.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.android_kyc_assignment.domain.model.AccountType
import com.example.android_kyc_assignment.domain.model.Customer
import com.example.android_kyc_assignment.ui.theme.Black
import com.example.android_kyc_assignment.ui.theme.LightGray
import com.example.android_kyc_assignment.ui.theme.White

@Composable
fun CustomerCard(
    customer: Customer,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(12.dp),
                clip = false
            )
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Avatar with border
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(
                        if (customer.isKycVerified)
                            Color(0xFF00C853).copy(alpha = 0.2f)
                        else
                            Color(0xFFD32F2F).copy(alpha = 0.2f)
                    )
                    .padding(2.dp)
            ) {
                AsyncImage(
                    model = customer.avatar,
                    contentDescription = "Customer avatar",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Name
            Text(
                text = customer.name,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                maxLines = 1,
                color = Black
            )

            // Account Number
            Text(
                text = customer.accountNumber,
                fontSize = 11.sp,
                color = Color.Gray,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Account Type Badge
            Surface(
                shape = RoundedCornerShape(4.dp),
                color = when (customer.accountType) {
                    AccountType.SAVINGS -> Color(0xFFE8F5E9)
                    AccountType.CURRENT -> Color(0xFFE3F2FD)
                    AccountType.NRI -> Color(0xFFFFF3E0)
                    else -> LightGray
                },
                modifier = Modifier.padding(vertical = 2.dp)
            ) {
                Text(
                    text = customer.accountType.name,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Medium,
                    color = when (customer.accountType) {
                        AccountType.SAVINGS -> Color(0xFF2E7D32)
                        AccountType.CURRENT -> Color(0xFF1565C0)
                        AccountType.NRI -> Color(0xFFE65100)
                        else -> Color.Gray
                    },
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Balance
            Text(
                text = "${customer.currency} ${String.format("%.2f", customer.balance)}",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Black
            )

            Spacer(modifier = Modifier.height(6.dp))

            // KYC Status Badge
            Surface(
                shape = RoundedCornerShape(4.dp),
                color = if (customer.isKycVerified)
                    Color(0xFF00C853).copy(alpha = 0.15f)
                else
                    Color(0xFFD32F2F).copy(alpha = 0.15f)
            ) {
                Text(
                    text = if (customer.isKycVerified) "✓ Verified" else "⏳ Pending",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (customer.isKycVerified)
                        Color(0xFF00C853)
                    else
                        Color(0xFFD32F2F),
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                )
            }
        }
    }
}