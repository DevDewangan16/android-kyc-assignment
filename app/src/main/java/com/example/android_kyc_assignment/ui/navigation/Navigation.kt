package com.example.android_kyc_assignment.ui.navigation
import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.android_kyc_assignment.di.AppModule
import com.example.android_kyc_assignment.ui.camera.CameraScreen
import com.example.android_kyc_assignment.ui.screens.accounts.AccountsScreen
import com.example.android_kyc_assignment.ui.screens.details.DetailsScreen
import com.example.android_kyc_assignment.ui.screens.details.DetailsViewModel
import com.example.android_kyc_assignment.ui.screens.details.DetailsViewModelFactory

sealed class Screen(val route: String) {
    object Accounts : Screen("accounts")
    object Details : Screen("details/{customerId}") {
        fun passId(id: Int): String = "details/$id"
    }
    object Camera : Screen("camera/{customerId}") {
        fun passId(id: Int): String = "camera/$id"
    }
}

@SuppressLint("RememberReturnType")
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current

    // Initialize dependencies
    remember { AppModule.initialize(context) }

    // Create shared ViewModel
    val sharedViewModel: DetailsViewModel = viewModel(
        factory = DetailsViewModelFactory(
            AppModule.getUserRepository(),
            AppModule.getIFSCRepository()
        )
    )

    NavHost(
        navController = navController,
        startDestination = Screen.Accounts.route
    ) {
        composable(Screen.Accounts.route) {
            AccountsScreen(
                onCustomerClick = { customerId ->
                    navController.navigate(Screen.Details.passId(customerId))
                }
            )
        }

        composable(
            route = Screen.Details.route,
            arguments = listOf(navArgument("customerId") { type = NavType.IntType })
        ) { backStackEntry ->
            val customerId = backStackEntry.arguments?.getInt("customerId") ?: return@composable

            sharedViewModel.resetKycUpdated()

            DetailsScreen(
                customerId = customerId,
                onNavigateToCamera = { id ->
                    navController.navigate(Screen.Camera.passId(id))
                },
                onKycComplete = {
                    navController.popBackStack()
                },
                viewModel = sharedViewModel
            )
        }

        composable(
            route = Screen.Camera.route,
            arguments = listOf(navArgument("customerId") { type = NavType.IntType })
        ) { backStackEntry ->
            val customerId = backStackEntry.arguments?.getInt("customerId") ?: return@composable

            CameraScreen(
                customerId = customerId,
                onSelfieCaptured = { bitmap ->
                    // Update KYC status with the captured selfie
                    sharedViewModel.updateKycStatus(bitmap, context)
                    // Navigate back to details
                    navController.popBackStack()
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}