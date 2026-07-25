package com.example.android_kyc_assignment.ui.screens.details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.android_kyc_assignment.ui.theme.Black
import com.example.android_kyc_assignment.ui.theme.White
import java.io.File

@Composable
fun ProfileHeader(
    name: String,
    photo: String,
    isKycVerified: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Photo with border
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(
                    if (isKycVerified)
                        Color(0xFF00C853).copy(alpha = 0.3f)
                    else
                        Color(0xFFD32F2F).copy(alpha = 0.3f)
                )
                .padding(4.dp)
        ) {
            val imageModel = if (photo.startsWith("/")) {
                File(photo)
            } else {
                photo
            }

            AsyncImage(
                model = imageModel,
                contentDescription = "Profile photo",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Name
        Text(
            text = name,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Black
        )

        // KYC Status
        Surface(
            shape = RoundedCornerShape(4.dp),
            color = if (isKycVerified)
                Color(0xFF00C853).copy(alpha = 0.15f)
            else
                Color(0xFFD32F2F).copy(alpha = 0.15f),
            modifier = Modifier.padding(top = 4.dp)
        ) {
            Text(
                text = if (isKycVerified) "✓ KYC Verified" else "⏳ KYC Pending",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = if (isKycVerified)
                    Color(0xFF00C853)
                else
                    Color(0xFFD32F2F),
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
            )
        }
    }
}