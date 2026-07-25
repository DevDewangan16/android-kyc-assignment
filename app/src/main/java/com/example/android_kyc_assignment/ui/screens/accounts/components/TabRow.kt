package com.example.android_kyc_assignment.ui.screens.accounts.components

import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.android_kyc_assignment.ui.theme.Black

@Composable
fun CustomTabRow(
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    ScrollableTabRow(
        selectedTabIndex = selectedIndex,
        containerColor = Color.Transparent,
        contentColor = Black,
        edgePadding = 0.dp,
        indicator = { tabPositions ->
            TabRowDefaults.PrimaryIndicator(
                modifier = Modifier.tabIndicatorOffset(tabPositions[selectedIndex]),
                height = 3.dp,
                color = Black
            )
        },
        divider = {
            Divider(
                color = Color(0xFFE0E0E0),
                thickness = 1.dp
            )
        }
    ) {
        listOf("Verified", "Pending").forEachIndexed { index, title ->
            Tab(
                selected = selectedIndex == index,
                onClick = { onTabSelected(index) },
                text = {
                    Text(
                        title,
                        color = if (selectedIndex == index) Black else Color.Gray,
                        style = MaterialTheme.typography.titleSmall
                    )
                },
                selectedContentColor = Black,
                unselectedContentColor = Color.Gray
            )
        }
    }
}