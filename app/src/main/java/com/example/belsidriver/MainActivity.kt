package com.example.belsidriver

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.belsidriver.presentation.navigation.AppNavigation
import com.example.belsidriver.ui.theme.BELSIDriverTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BELSIDriverTheme {
                val navController = rememberNavController()
                AppNavigation(navController = navController)
            }
        }
    }
}
