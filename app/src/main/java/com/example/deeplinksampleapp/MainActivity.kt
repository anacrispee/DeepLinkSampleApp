package com.example.deeplinksampleapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import com.example.deeplinksampleapp.ui.theme.DeepLinkSampleAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DeepLinkSampleAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()
                    BaseScreen(
                        modifier = Modifier.padding(innerPadding),
                        content = {
                            AppNavGraph(
                                navController = navController
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun AppNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(route = Screen.Home.route) {
            HomeScreen()
        }
        composable(
            route = Screen.Profile.route,
            deepLinks = listOf(navDeepLink { uriPattern = "myapp://profile/{userId}" })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: "Unknown"
            ProfileScreen(userId = userId)
        }
    }
}

@Composable
private fun HomeScreen() {
    Text(text = "Home", modifier = Modifier.padding(16.dp))
}

@Composable
private fun ProfileScreen(userId: String?) {
    Text(text = "Profile: $userId", modifier = Modifier.padding(16.dp))
}

@Composable
private fun BaseScreen(
    content: @Composable () -> Unit,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        content()
    }
}

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Profile : Screen("profile/{userId}")
}
