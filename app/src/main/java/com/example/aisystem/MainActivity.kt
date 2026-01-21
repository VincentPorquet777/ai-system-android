package com.example.aisystem

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.aisystem.feature.debug.logger.DebugLogger
import com.example.aisystem.feature.debug.logger.LogLevel
import com.example.aisystem.navigation.NavGraph
import com.example.aisystem.navigation.Screen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var debugLogger: DebugLogger

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        debugLogger.log(LogLevel.INFO, "MainActivity", "App started")

        setContent {
            MaterialTheme {
                MainScreen(debugLogger)
            }
        }
    }
}

@Composable
fun MainScreen(debugLogger: DebugLogger) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Hide bottom bar on chat screen
    val showBottomBar = currentRoute != null && !currentRoute.startsWith("chat/")

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    NavigationBarItem(
                        selected = currentRoute == Screen.ConversationList.route,
                        onClick = {
                            navController.navigate(Screen.ConversationList.route) {
                                popUpTo(Screen.ConversationList.route) { inclusive = true }
                            }
                        },
                        icon = { Icon(Icons.Default.Chat, "Chats") },
                        label = { Text("Chats") }
                    )
                    NavigationBarItem(
                        selected = currentRoute == Screen.Settings.route,
                        onClick = {
                            navController.navigate(Screen.Settings.route) {
                                launchSingleTop = true
                            }
                        },
                        icon = { Icon(Icons.Default.Settings, "Settings") },
                        label = { Text("Settings") }
                    )
                    NavigationBarItem(
                        selected = currentRoute == Screen.Debug.route,
                        onClick = {
                            navController.navigate(Screen.Debug.route) {
                                launchSingleTop = true
                            }
                        },
                        icon = { Icon(Icons.Default.BugReport, "Debug") },
                        label = { Text("Debug") }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavGraph(
            navController = navController,
            modifier = Modifier.padding(paddingValues)
        )
    }
}
