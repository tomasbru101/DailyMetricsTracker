package com.example.dailymetricstracker.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dailymetricstracker.ui.screens.MainScreen
import com.example.dailymetricstracker.ui.screens.MetricManagementScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            MainScreen(onNavigateToMetricManagement = {
                navController.navigate("metric_management")
            })
        }
        composable("metric_management") {
            MetricManagementScreen(onNavigateBack = {
                navController.popBackStack()
            })
        }
    }
} 