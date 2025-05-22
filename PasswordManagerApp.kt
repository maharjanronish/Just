package com.example.passwordmanager

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.passwordmanager.ui.AddPasswordScreen
import com.example.passwordmanager.ui.LoginScreen
import com.example.passwordmanager.ui.PasswordListScreen
import com.example.passwordmanager.util.EncryptionUtil

@Composable
fun PasswordManagerApp() {
    val navController = rememberNavController()
    val context = LocalContext.current

    EncryptionUtil.init(context)

    NavHost(navController, startDestination = if (SessionManager.isUnlocked()) "list" else "login") {
        composable("login") { LoginScreen(navController) }
        composable("list") { PasswordListScreen(navController) }
        composable("add") { AddPasswordScreen(navController) }
    }
}